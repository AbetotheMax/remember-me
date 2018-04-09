package com.example.marc.rememberme.feature.Persistence;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.marc.rememberme.feature.Deck;
import com.example.marc.rememberme.feature.Card;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Marc on 4/1/2018.
 */

public class CardRecallPersistenceManager {

    private static CardRecallPersistenceManager instance;
    private RememberMeDb db;
    private ExecutorService executor;
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
        int maxDeckId = getMaxDeckId();
        if(maxDeckId != 0) {
            deckId = maxDeckId;
            deckId += 1;
        } else {
            deckId = 1;
        }
        numDecks = deck.getNumDecks();

        Decks decksRecord = new Decks();

        for(int i = 0; i < deck.getCards().size(); i++) {

            decksRecord.setDeckId(deckId);
            decksRecord.setNumDecks(numDecks);
            decksRecord.setCardIndex(i);
            decksRecord.setCardNumber(deck.getCard(i).getCardNumberAsString());
            decksRecord.setCardSuit(deck.getCard(i).getSuitAsString());
            insertDeck(decksRecord);

        }

        lastGameHistoryRecord = new GameHistory();
        int maxSessionId = getMaxSessionId();
        int sessionId;
        if(maxSessionId != 0) {
            sessionId = maxSessionId + 1;
        } else {
            sessionId = 1;
        }

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

        insertGameHistoryRecord(lastGameHistoryRecord);

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

        updateGameState(lastGameHistoryRecord);

    }

    public void saveNewError(int cardIndex, String cardNumberGuessed, String cardSuitGuessed, long duration) {

        CardRecallErrors recallError = new CardRecallErrors();
        recallError.setSessionId(lastGameHistoryRecord.getSessionId());
        recallError.setAttemptId(lastGameHistoryRecord.getAttemptId());
        recallError.setDeckId(deckId);
        recallError.setCardIndex(cardIndex);
        recallError.setCardNumber(getCardNumber(cardIndex));
        recallError.setCardSuit(getCardSuit(cardIndex));
        recallError.setCardNumberGuessed(cardNumberGuessed);
        recallError.setCardSuitGuessed(cardSuitGuessed);

        insertCardRecallError(recallError);

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

    private int getMaxDeckId() {
        int deckId = 0;
        executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new Callable() {
            public Object call() {
                return  db.decksDao().getMaxDeckId();
            }
        });
        try {
            deckId = (Integer) future.get();
            return deckId;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error getting max deck id: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error getting max deck id: " + ee);
        } finally {
            executor.shutdown();
        }
        return deckId;
    }

    private void insertDeck(final Decks decks) {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                db.decksDao().insertDeck(decks);
            }
        });
        executor.shutdown();
    }

    private int getMaxSessionId() {
        int maxSessionId = 0;
        executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new Callable() {
            public Object call() {
                return  db.gameHistoryDao().getMaxSessionId("CARD RECALL");
            }
        });
        try {
            maxSessionId  = (Integer) future.get();
            return maxSessionId ;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error getting max session id: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error getting max session id: " + ee);
        } finally {
            executor.shutdown();
        }
        return maxSessionId ;
    }

    private void insertGameHistoryRecord(final GameHistory gh) {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                db.gameSummaryDao().insertGameSummary(gh.convertToGameSummary());
                db.gameHistoryDao().insertGameState(gh);
            }
        });
        executor.shutdown();
    }

    private void updateGameState(final GameHistory gh) {

        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                db.gameHistoryDao().insertGameState(gh);

                GameSummary summary = gh.convertToGameSummary();
                summary.setCumulativeStateDuration(summary.getCumulativeStateDuration() +
                        db.gameSummaryDao()
                                .getGameSummary(summary.getSessionId(), summary.getAttemptId())
                                .getCumulativeStateDuration()
                );

                db.gameSummaryDao().updateGameSummary(summary);

            }
        });
        executor.shutdown();
    }

    private String getCardNumber(final int cardIndex) {
        String cardNumber = "";

        executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable() {
            public Object call() {
                return  db.decksDao().getCardnumber(deckId, cardIndex);
            }
        });
        try {
            cardNumber  = (String) future.get();
            return cardNumber;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error getting card number for card index " + cardIndex + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error getting card number for card index " + cardIndex + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return cardNumber;
    }

    private String getCardSuit(final int cardIndex) {
        String suit = "";
        executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable() {
            public Object call() {
                return  db.decksDao().getCardSuit(deckId, cardIndex);
            }
        });
        try {
            suit = (String) future.get();
            return suit;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error getting card suit for card index " + cardIndex + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error getting card suit for card index " + cardIndex + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return suit;

    }

    private void insertCardRecallError( final CardRecallErrors recallError) {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                db.cardRecallErrorsDao().insertCardRecallError(recallError);
            }
        });
        executor.shutdown();
    }

}
