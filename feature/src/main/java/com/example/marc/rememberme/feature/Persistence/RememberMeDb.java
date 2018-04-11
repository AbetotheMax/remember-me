package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Marc on 3/29/2018.
 */

@Database(
        entities = {
                GameSummary.class,
                GameHistory.class,
                Decks.class,
                CardRecallErrors.class,
                Games.class,
                GameStates.class
        },
        version = 1
)
public abstract class RememberMeDb extends RoomDatabase{

        private static RememberMeDb instance;

        public static RememberMeDb getInstance(Context context) {

            if(instance == null) {
               instance = Room.databaseBuilder(context.getApplicationContext(), RememberMeDb.class, "REMEMBER_ME_DB").build();
               insertGame(instance);
               insertGameStates(instance);
            }

            return instance;

        }

        abstract public CardRecallErrorsDao cardRecallErrorsDao();
        abstract public DecksDao decksDao();
        abstract public GameSummaryDao gameSummaryDao();
        abstract public GameHistoryDao gameHistoryDao();
        abstract public GamesDao gamesDao();
        abstract public GameStatesDao gameStatesDao();

/*
        static RoomDatabase.Callback callback = new RoomDatabase.Callback() {

            public void onCreate(SupportSQLiteDatabase db) {

                String CREATE_GAMES_TABLE = "" +
                        "CREATE TABLE IF NOT EXISTS GAMES " +
                        "(" +
                        "GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "GAME_DESC TEXT UNIQUE NOT NULL, " +
                        "GAME_COMPONENTS TEXT NOT NULL" +
                        ")";

                db.execSQL(CREATE_GAMES_TABLE);
                ContentValues contentValues = new ContentValues();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("GAME_COMPONENTS", "DECKS");
                db.insert("GAMES", OnConflictStrategy.ABORT, contentValues);

                String CREATE_GAME_STATES_TABLE = "" +
                        "CREATE TABLE IF NOT EXISTS GAME_STATES " +
                        "(" +
                        "GAME_DESC TEXT, " +
                        "STATE TEXT NOT NULL, " +
                        "STATUS TEXT NOT NULL," +
                        "PRIMARY KEY(GAME_DESC, STATE, STATUS), " +
                        "FOREIGN KEY (GAME_DESC) REFERENCES GAMES(GAME_DESC)" +
                        ")";

                db.execSQL(CREATE_GAME_STATES_TABLE);
                db.execSQL("DELETE FROM GAME_STATES");
                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "LEARNING");
                contentValues.put("STATUS", "STARTED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "LEARNING");
                contentValues.put("STATUS", "IN PROGRESS");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "LEARNING");
                contentValues.put("STATUS", "RESTARTED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "LEARNING");
                contentValues.put("STATUS", "RESUMED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "LEARNING");
                contentValues.put("STATUS", "CANCELLED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "LEARNING");
                contentValues.put("STATUS", "COMPLETED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "RECALL");
                contentValues.put("STATUS", "STARTED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "RECALL");
                contentValues.put("STATUS", "IN PROGRESS");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "RECALL");
                contentValues.put("STATUS", "RESTARTED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "RECALL");
                contentValues.put("STATUS", "RESUMED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "RECALL");
                contentValues.put("STATUS", "CANCELLED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);

                contentValues.clear();
                contentValues.put("GAME_DESC", "CARD RECALL");
                contentValues.put("STATE", "RECALL");
                contentValues.put("STATUS", "COMPLETED");
                db.insert("GAME_STATES", OnConflictStrategy.ABORT, contentValues);


            }

        };
*/
    private static void insertGame(final RememberMeDb db) {
        final Games game= new Games();
        game.setGameId(0);
        game.setGameDescription("CARD RECALL");
        game.setGameComponents("DECKS");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    db.gamesDao().insertGame(game);
                } catch (Exception e) {
                    Log.e("Error", "Error inserting game.  Exception: " + e);
                }
            }
        });
        executor.shutdown();
    }

    private static void insertGameStates(final RememberMeDb db) {
        final List<GameStates> gameStates = loadGameStates();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                try {
                    db.gameStatesDao().insertGameStates(gameStates);
                } catch (Exception e) {
                    Log.e("Error", "Error inserting game.  Exception: " + e);
                }
            }
        });
        executor.shutdown();
    }

    private static List<GameStates> loadGameStates() {
        List<GameStates> gameStates = new ArrayList<>();
        GameStates gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("LEARNING");
        gameState.setStatus("STARTED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("LEARNING");
        gameState.setStatus("IN PROGRESS");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("LEARNING");
        gameState.setStatus("RESTARTED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("LEARNING");
        gameState.setStatus("RESUMED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("LEARNING");
        gameState.setStatus("CANCELLED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("LEARNING");
        gameState.setStatus("COMPLETED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("RECALL");
        gameState.setStatus("STARTED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("RECALL");
        gameState.setStatus("IN PROGRESS");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("RECALL");
        gameState.setStatus("RESTARTED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("RECALL");
        gameState.setStatus("RESUMED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("RECALL");
        gameState.setStatus("CANCELLED");
        gameStates.add(gameState);

        gameState = new GameStates();
        gameState.setGameDesc("CARD RECALL");
        gameState.setState("RECALL");
        gameState.setStatus("COMPLETED");
        gameStates.add(gameState);

        return gameStates;

    }

}
