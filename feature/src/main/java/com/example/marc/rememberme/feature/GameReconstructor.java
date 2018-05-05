package com.example.marc.rememberme.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.CardRecallErrors;
import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistory;
import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;
import com.example.marc.rememberme.feature.Persistence.GameSummary;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.marc.rememberme.feature.LearnNewDeck.LAST_GAME_HISTORY;

/**
 * Created by Marc on 4/24/2018.
 */

public class GameReconstructor extends AppCompatActivity {

    public static final String LEARNED_DECK = "com.example.marc.rememberme.feature.DECK";
    public static final String GAME_SUMMARY = "com.example.marc.rememberme.feature.Persistence.GAMESUMMARY";
    public static final String GAME_OVERVIEW = "com.example.marc.rememberme.feature.Persistence.GAMEHISTORYOVERVIEW";

    private CardRecallPersistenceManager recallManager;
    private GameHistoryOverview overview;
    private Deck deck;
    private GameSummary gameSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_transition);

        Bundle bundle = getIntent().getExtras();
        overview = bundle.getParcelable(GAME_OVERVIEW);

        recallManager = CardRecallPersistenceManager.getInstance(this);
        deck = recallManager.loadDeckForId(overview.getDeckId());
        gameSummary = recallManager.getLastGameSummaryForComponent(overview.getDeckId());
        loadTransitionPage();

    }

    public void reconstructGame(View view) {

        GameHistory lastGameHistory;

        if(overview.getProgress() == 100) {

            lastGameHistory = recallManager.saveNewGame(deck, overview.getDeckId());


        } else {

            lastGameHistory = gameSummary.convertToGameHistory();

        }

        Intent intent;

        if(overview.getGameState().equals("LEARNING") || overview.getProgress() == 100) {

            intent = new Intent(this, LearnNewDeck.class);

        } else {

            intent = new Intent(this, RecallDeck.class);

        }

        intent.putExtra(LEARNED_DECK, deck);
        intent.putExtra(LAST_GAME_HISTORY, lastGameHistory);
        this.startActivity(intent);

    }

    private void loadTransitionPage() {

        TextView playedOn = (TextView) findViewById(R.id.playedOn);
        playedOn.setText((String) playedOn.getText() + " " + overview.getGameDate());
        TextView gameState = (TextView) findViewById(R.id.lastGameState);
        gameState.setText((String) gameState.getText() + " " + overview.getGameState().toLowerCase());
        TextView gameStatus = (TextView) findViewById(R.id.lastStatus);
        gameStatus.setText((String) gameStatus.getText() + " " + gameSummary.getGameStateStatus().toLowerCase());
        TextView timeSpent = (TextView) findViewById(R.id.timeSpent);
        timeSpent.setText((String) timeSpent.getText() + " " + overview.getDuration());
        TextView lastPosition = (TextView) findViewById(R.id.lastPosition);
        lastPosition.setText((String) lastPosition.getText() + " " + (gameSummary.getLastPosition() + 1));
        TextView attemptsMade = (TextView) findViewById(R.id.attemptsMade);
        attemptsMade.setText((String) attemptsMade.getText() + " " + gameSummary.getAttemptId());
        TextView progress = (TextView) findViewById(R.id.progressInState);
        progress.setText((String) progress.getText() + " " + overview.getProgress() + "%");

        ViewStub recallDetailsStub = (ViewStub) findViewById(R.id.historyTransitionDetailsStub);
        View recallDetailsInflated = recallDetailsStub.inflate();
        TextView correctSelections = (TextView) recallDetailsInflated.findViewById(R.id.numberCorrect);

        int correctSelectionsCount = (gameSummary.getLastPosition() + 1) - overview.getErrorCount();
        correctSelections.setText((String) correctSelections.getText() + " " + correctSelectionsCount);
        TextView errorCount = (TextView) recallDetailsInflated.findViewById(R.id.errorCount);
        errorCount.setText((String) errorCount.getText() + " " + overview.getErrorCount());
        TextView accuracyText = (TextView) recallDetailsInflated.findViewById(R.id.accuracy);
        accuracyText.setText((String) accuracyText.getText() + " " + overview.getAccuracy() + "%");
        ExpandableListView playByPlay = (ExpandableListView) recallDetailsInflated.findViewById(R.id.playByPlay);
        PlayByPlayExpandableListAdapter adapter = new PlayByPlayExpandableListAdapter(
                this, "Play by Play", loadPlayByPlay(deck, gameSummary.getSessionId(), gameSummary.getAttemptId(), gameSummary.getLastPosition())
        );
        playByPlay.setAdapter(adapter);

    }

    private List<Map<Card, Card>> loadPlayByPlay(Deck deck, int sessionId, int attemptId, int lastPosition) {

        List<CardRecallErrors> errorList = recallManager.loadErrorsForAttempt(sessionId, attemptId);
        List<Map<Card, Card>> playByPlay = new ArrayList<>();
        List<Card> cards = deck.getCards();

        if(errorList == null || errorList.isEmpty()) {

            for(int i = 0; i <= lastPosition; i++) {

                Map<Card, Card> actualVsSelected = new HashMap<>();
                actualVsSelected.put(cards.get(i), cards.get(i));
                playByPlay.add(actualVsSelected);

            }

            return playByPlay;

        } else {

            Map<Integer, Card> onDeck = new HashMap<>();

            for(int i = 0; i <= lastPosition; i++) {

                Map<Card, Card> actualVsSelected = new HashMap<>();

                if(i < errorList.size()) {

                    Card selectedCard = new Card(this, Suit.valueOf(errorList.get(i).getCardSuitGuessed()), CardNumber.valueOf(errorList.get(i).getCardNumberGuessed()));

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

                    if(onDeck.containsKey(i)) {

                        actualVsSelected.put(cards.get(i), onDeck.get(i));
                        onDeck.remove(i);

                    } else {

                        actualVsSelected.put(cards.get(i), cards.get(i));

                    }

                    playByPlay.add(actualVsSelected);

                }

            }

            return playByPlay;

        }

    }

}
