package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;

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
               instance = Room.databaseBuilder(context.getApplicationContext(), RememberMeDb.class, "REMEMBER_ME_DB").addCallback(callback).build();
            }

            return instance;

        }

        abstract public CardRecallErrorsDao cardRecallErrorsDao();
        abstract public DecksDao decksDao();
        abstract public GameSummaryDao gameSummaryDao();
        abstract public GameHistoryDao gameHistoryDao();

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

}
