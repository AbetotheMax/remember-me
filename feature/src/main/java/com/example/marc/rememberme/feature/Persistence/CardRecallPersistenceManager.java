package com.example.marc.rememberme.feature.Persistence;

import android.content.Context;
import android.util.Log;
import com.example.marc.rememberme.feature.Deck;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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

    private CardRecallPersistenceManager(Context context) {

        db = RememberMeDb.getInstance(context);

    }

    public static CardRecallPersistenceManager getInstance(Context context) {

        if(instance == null) {

            instance = new CardRecallPersistenceManager(context);

        }

        return instance;

    }

    public GameHistory saveNewGame(Deck deck) {
        int deckId;
        int numDecks;
        int maxDeckId = getMaxDeckId();
        if(maxDeckId != 0) {
            deckId = maxDeckId;
            deckId += 1;
        } else {
            deckId = 1;
        }
        numDecks = deck.getNumDecks();

        List<Decks> completeDeck = new ArrayList<>();
        for(int i = 0; i < deck.getCards().size(); i++) {

            Decks decksRecord = new Decks();
            decksRecord.setDeckId(deckId);
            decksRecord.setNumDecks(numDecks);
            decksRecord.setCardIndex(i);
            decksRecord.setCardNumber(deck.getCard(i).getCardNumberAsString());
            decksRecord.setCardSuit(deck.getCard(i).getSuitAsString());
            completeDeck.add(decksRecord);

        }
        insertDeck(completeDeck);

        GameHistory lastGameHistoryRecord = new GameHistory();
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

        return lastGameHistoryRecord;

    }

    public void saveNewError(GameHistory lastGameHistoryRecord, int cardIndex, String cardNumberGuessed, String cardSuitGuessed) {

        int deckId = lastGameHistoryRecord.getComponentInstanceId();

        CardRecallErrors recallError = new CardRecallErrors();
        recallError.setSessionId(lastGameHistoryRecord.getSessionId());
        recallError.setAttemptId(lastGameHistoryRecord.getAttemptId());
        recallError.setDeckId(deckId);
        recallError.setCardIndex(cardIndex);
        recallError.setCardNumber(getCardNumber(deckId, cardIndex));
        recallError.setCardSuit(getCardSuit(deckId, cardIndex));
        recallError.setCardNumberGuessed(cardNumberGuessed);
        recallError.setCardSuitGuessed(cardSuitGuessed);

        insertCardRecallError(recallError);

        if(!lastGameHistoryRecord.getErrors()) {

            lastGameHistoryRecord.setErrors(true);
            updateGameState(lastGameHistoryRecord);

        }

    }

    public int getMaxDeckId() {
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

    private void insertDeck(final List<Decks> decks) {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    db.decksDao().insertDeck(decks);
                } catch (Exception e) {
                    Log.e("Error", "Error inserting deck.  Exception: " + e);
                }
            }
        });
        executor.shutdown();
    }

    public int getMaxSessionId() {
        int maxSessionId = 0;
        executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new Callable() {
            public Object call() {
                return  db.gameSummaryDao().getMaxSessionId("CARD RECALL");
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

    public void updateGameState(final GameHistory gh) {

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

    public GameSummary getGameSummary(final int sessionId, final int attemptId) {
        GameSummary gs = new GameSummary();
        executor = Executors.newSingleThreadExecutor();
        Future<GameSummary> future = executor.submit(new Callable() {
            public Object call() {
                return  db.gameSummaryDao().getGameSummary(sessionId, attemptId);
            }
        });
        try {
            gs  = (GameSummary) future.get();
            return gs;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error getting game summary for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error getting card number for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }

        return gs;
    }

    public String getCardNumber(final int deckId, final int cardIndex) {
        String cardNumber = "";

        executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable() {
            public Object call() {
                return  db.decksDao().getCardNumber(deckId, cardIndex);
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

    public String getCardSuit(final int deckId, final int cardIndex) {
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

    public List<CardRecallErrors> loadErrorsForAttempt(final int sessionId, final int attemptId) {
        List<CardRecallErrors> errorList = new ArrayList<>();

        executor = Executors.newSingleThreadExecutor();
        Future<List<CardRecallErrors>> future = executor.submit(new Callable() {
            public Object call() {
                return  db.cardRecallErrorsDao().loadErrorsForAttempt(sessionId, attemptId);
            }
        });
        try {
            errorList = (List<CardRecallErrors>) future.get();
            return errorList;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error loading card recall errors for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error loading card recall errors for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return errorList;
    }

    public int getNumErrorsForAttempt(final int sessionId, final int attemptId) {
        int numErrors = 0;

        executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new Callable() {
            public Object call() {
                return  db.cardRecallErrorsDao().getNumErrorsForAttempt(sessionId, attemptId);
            }
        });
        try {
            numErrors = (int) future.get();
            return numErrors;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error loading number of errors for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error loading number of errors for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return numErrors;
    }

    public CardRecallErrors loadLastErrorForAttempt(final int sessionId, final int attemptId) {
        CardRecallErrors lastError = new CardRecallErrors();

        executor = Executors.newSingleThreadExecutor();
        Future<CardRecallErrors> future = executor.submit(new Callable() {
            public Object call() {
                return  db.cardRecallErrorsDao().loadLastErrorForAttempt(sessionId, attemptId);
            }
        });
        try {
            lastError = (CardRecallErrors) future.get();
            return lastError;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error loading last card recall error for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error loading last card recall error for session id " + sessionId + " and attempt id " + attemptId + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return lastError;
    }

    public List<String> getDatesOfPriorGames() {

        List<String> gameDates = new ArrayList<>();

        executor = Executors.newSingleThreadExecutor();
        Future<List<String>> future = executor.submit(new Callable() {
            public Object call() {
                return  db.gameSummaryDao().getDatesOfPriorGames();
            }
        });
        try {
            gameDates = (List<String>) future.get();
            return gameDates;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error loading prior game dates.  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error loading prior game dates.  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return gameDates;

    }

    public List<GameSummary> loadGameSummariesForDate(final String gameDate) {
        List<GameSummary> gameSummaries = new ArrayList<>();

        executor = Executors.newSingleThreadExecutor();
        Future<List<GameSummary>> future = executor.submit(new Callable() {
            public Object call() {
                return  db.gameSummaryDao().loadGamesForDate(gameDate);
            }
        });
        try {
            gameSummaries = (List<GameSummary>) future.get();
            return gameSummaries;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error loading game summaries for date " + gameDate + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error loading game summaries for date " + gameDate + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return gameSummaries;
    }

    public List<GameHistoryOverview> loadGameOverviewsForDate(final String gameDate) {
        List<GameHistoryOverview> gameOverviews = new ArrayList<>();

        executor = Executors.newSingleThreadExecutor();
        Future<List<GameHistoryOverview>> future = executor.submit(new Callable() {
            public Object call() {
                return  db.gameSummaryDao().loadGameOverviewsForDate(gameDate);
            }
        });
        try {
            gameOverviews = (List<GameHistoryOverview>) future.get();
            return gameOverviews;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error loading game overviews for date " + gameDate + ".  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error loading game overviews for date " + gameDate + ".  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return gameOverviews;
    }

    public List<GameHistoryOverview> loadGameOverviews() {
        List<GameHistoryOverview> gameOverviews = new ArrayList<>();

        executor = Executors.newSingleThreadExecutor();
        Future<List<GameHistoryOverview>> future = executor.submit(new Callable() {
            public Object call() {
                return  db.gameSummaryDao().loadGameOverviews();
            }
        });
        try {
            gameOverviews = (List<GameHistoryOverview>) future.get();
            return gameOverviews;
        } catch(InterruptedException ie) {
            Log.e("ERROR", "Error loading game overviews.  Error message: " + ie);
        } catch(ExecutionException ee) {
            Log.e("ERROR", "Error loading game overviews.  Error message: " + ee);
        } finally {
            executor.shutdown();
        }
        return gameOverviews;
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
