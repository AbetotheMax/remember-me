package com.example.marc.rememberme.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.CardRecallErrors;
import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistory;
import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;
import com.example.marc.rememberme.feature.Persistence.GameSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.marc.rememberme.feature.LearnNewDeck.LAST_GAME_HISTORY;

/**
 * Created by Marc on 4/24/2018.
 */

public class GameReconstructor extends AppCompatActivity {

    public static final String LEARNED_DECK = "com.example.marc.rememberme.feature.DECK";
    public static final String GAME_SUMMARY = "com.example.marc.rememberme.feature.Persistence.GAMESUMMARY";

    private CardRecallPersistenceManager recallManager;
    private GameHistoryOverview overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_transition);
    }

    private void reconstructGame(Context context, GameHistoryOverview overview) {

        recallManager = CardRecallPersistenceManager.getInstance(context);
        Deck deck = recallManager.loadDeckForId(overview.getDeckId());
        GameSummary gameSummary = recallManager.getLastGameSummaryForComponent(overview.getDeckId());
        GameHistory lastGameHistory;

        if(overview.getProgress() == 100) {

            lastGameHistory = recallManager.saveNewGame(deck, overview.getDeckId());


        } else {

            lastGameHistory = gameSummary.convertToGameHistory();

        }

        Intent intent;

        if(overview.getGameState().equals("LEARNING") || overview.getProgress() == 100) {

            intent = new Intent(context, LearnNewDeck.class);

        } else {

            intent = new Intent(context, RecallDeck.class);

        }

        intent.putExtra(LEARNED_DECK, deck);
        intent.putExtra(LAST_GAME_HISTORY, lastGameHistory);
        context.startActivity(intent);


    }

    private void loadTransitionPage(Context context) {


        ViewStub recallDetailsStub = (ViewStub) findViewById(R.id.historyTransitionDetailsStub);
        View recallDetailsInflated = recallDetailsStub.inflate();
        TextView correctSelections = (TextView) recallDetailsInflated.findViewById(R.id.numberCorrect);
        correctSelections.setText(correctSelections.getText() + )

    }

    private List<Map<Card, Card>> loadPlayByPlay(Context context, Deck deck, int sessionId, int attemptId) {

        List<CardRecallErrors> errorList = recallManager.loadErrorsForAttempt(sessionId, attemptId);
        List<Map<Card, Card>> playByPlay = new ArrayList<>();
        List<Card> cards = deck.getCards();

        if(errorList == null || errorList.isEmpty()) {

            for(int i = 0; i < cards.size(); i++) {

                Map<Card, Card> actualVsSelected = new HashMap<>();
                actualVsSelected.put(cards.get(i), cards.get(i));
                playByPlay.add(actualVsSelected);

            }

            return playByPlay;

        } else {

            for(int i = 0; i < cards.size(); i++) {

                Map<Card, Card> actualVsSelected = new HashMap<>();
                Map<Integer, Card> onDeck = new HashMap<>();

                if(i < errorList.size()) {

                    Card selectedCard = new Card(context, Suit.valueOf(errorList.get(i).getCardSuit()), CardNumber.valueOf(errorList.get(i).getCardNumber()));

                    if(errorList.get(i).getCardIndex() == i) {

                        actualVsSelected.put(cards.get(i), selectedCard);
                        playByPlay.add(actualVsSelected);

                    } else {

                        onDeck.put(errorList.get(i).getCardIndex(), selectedCard);

                        if(onDeck.containsKey(i)) {

                            actualVsSelected.put(cards.get(i), onDeck.get(i));
                            onDeck.remove(i);

                        } else {

                            actualVsSelected.put(cards.get(i), cards.get(i));

                        }

                        playByPlay.add(actualVsSelected);

                    }

                } else {

                    actualVsSelected.put(cards.get(i), cards.get(i));
                    playByPlay.add(actualVsSelected);

                }

            }

            return playByPlay;

        }

    }

}
