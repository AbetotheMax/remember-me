package com.example.marc.rememberme.feature.Persistence;

import android.content.Context;

import com.example.marc.rememberme.feature.Deck;
import com.example.marc.rememberme.feature.Card;

import java.util.Date;

/**
 * Created by Marc on 4/1/2018.
 */

public class CardRecallPersistenceManager {

    private static CardRecallPersistenceManager instance;
    private RememberMeDb db;
    private int deckId;
    private int numDecks;
    private GameHistory lastGameHistoryRecord;

    private CardRecallPersistenceManager(Context context) {

        db = RememberMeDb.getInstance(context);

    }

    public static CardRecallPersistenceManager getInstance(Context context) {

        if(instance == null) {

            instance = new CardRecallPersistenceManager(context);

        }

        return instance;

    }

    public void saveNewGame(Deck deck) {

        deckId = db.decksDao().getMaxDeckId() + 1;
        numDecks = deck.getNumDecks();

        Decks decksRecord = new Decks();

        for(int i = 0; i < deck.getCards().size(); i++) {

            decksRecord.setDeckId(deckId);
            decksRecord.setNumDecks(numDecks);
            decksRecord.setCardIndex(i);
            decksRecord.setCardNumber(deck.getCard(i).getCardNumberAsString());
            decksRecord.setCardSuit(deck.getCard(i).getSuitAsString());
            db.decksDao().insertDeck(decksRecord);

        }

        lastGameHistoryRecord = new GameHistory();
        int sessionId = db.gameHistoryDao().getMaxSessionId("CARD RECALL") + 1;

        lastGameHistoryRecord.setSessionId(sessionId);
        lastGameHistoryRecord.setAttemptId(1);
        lastGameHistoryRecord.setComponentInstanceId(deckId);
        lastGameHistoryRecord.setGameDesc("CARD RECALL");
        lastGameHistoryRecord.setGameState("LEARNING");
        lastGameHistoryRecord.setGameStateStatus("STARTED");
        lastGameHistoryRecord.setLastPosition(0);
        lastGameHistoryRecord.setErrors(false);
        lastGameHistoryRecord.setCumulativeStateDuration(0);
        lastGameHistoryRecord.setLastModDateTime(new Date());

        db.gameSummaryDao().insertGameSummary(lastGameHistoryRecord.convertToGameSummary());
        db.gameHistoryDao().insertGameState(lastGameHistoryRecord);

    }

    public void updateGameState(boolean newAttempt, String gameState, String gameStateStatus, int lastPosition, long duration) {

        if(newAttempt) {

            lastGameHistoryRecord.setAttemptId(lastGameHistoryRecord.getAttemptId() + 1);
            lastGameHistoryRecord.setErrors(false);

        }

        lastGameHistoryRecord.setGameState(gameState);
        lastGameHistoryRecord.setGameStateStatus(gameStateStatus);
        lastGameHistoryRecord.setLastPosition(lastPosition);
        lastGameHistoryRecord.setCumulativeStateDuration(duration);
        lastGameHistoryRecord.setLastModDateTime(new Date());

        db.gameHistoryDao().insertGameState(lastGameHistoryRecord);

        GameSummary summary = lastGameHistoryRecord.convertToGameSummary();
        summary.setCumulativeStateDuration(summary.getCumulativeStateDuration() +
                db.gameSummaryDao()
                        .getGameSummary(summary.getSessionId(), summary.getAttemptId())
                        .getCumulativeStateDuration()
        );

        db.gameSummaryDao().updateGameSummary(summary);

    }

    public void saveNewError(int cardIndex, String cardNumberGuessed, String cardSuitGuessed, long duration) {

        CardRecallErrors recallError = new CardRecallErrors();
        recallError.setSessionId(lastGameHistoryRecord.getSessionId());
        recallError.setAttemptId(lastGameHistoryRecord.getAttemptId());
        recallError.setDeckId(deckId);
        recallError.setCardIndex(cardIndex);
        recallError.setCardNumber(db.decksDao().getCardnumber(deckId, cardIndex));
        recallError.setCardSuit(db.decksDao().getCardSuit(deckId, cardIndex));
        recallError.setCardNumberGuessed(cardNumberGuessed);
        recallError.setCardSuitGuessed(cardSuitGuessed);

        db.cardRecallErrorsDao().insertCardRecallError(recallError);

        if(!lastGameHistoryRecord.getErrors()) {

            lastGameHistoryRecord.setErrors(true);
            updateGameState(false, lastGameHistoryRecord.getGameState(), lastGameHistoryRecord.getGameStateStatus(), cardIndex, duration);

        }

    }

    public int getDeckId() {

        return this.deckId;

    }

    public int getNumDecks() {

        return this.numDecks;

    }

    public GameHistory getLastGameHistoryRecord() {

        return this.lastGameHistoryRecord;

    }

}
