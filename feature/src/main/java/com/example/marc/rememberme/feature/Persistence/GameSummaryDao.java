package com.example.marc.rememberme.feature.Persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Marc on 4/1/2018.
 */

@Dao
public interface GameSummaryDao {

    @Query("SELECT * FROM GAME_SUMMARY WHERE SESSION_ID = :sessionId AND ATTEMPT_ID = :attemptId")
    public GameSummary getGameSummary(int sessionId, int attemptId);

    @Query("SELECT MAX(SESSION_ID) FROM GAME_SUMMARY WHERE GAME_DESC = :gameDesc")
    public int getMaxSessionId(String gameDesc);

    @Query("SELECT DISTINCT DATE(SUBSTR(LAST_MOD_DATE_TIME, 1, 10), 'UNIXEPOCH') FROM GAME_SUMMARY ORDER BY LAST_MOD_DATE_TIME DESC")
    public List<String> getDatesOfPriorGames();

    @Query("SELECT * FROM GAME_SUMMARY WHERE DATE(SUBSTR(LAST_MOD_DATE_TIME, 1, 10), 'UNIXEPOCH') = :gameDate ORDER BY LAST_MOD_DATE_TIME DESC;")
    public List<GameSummary> loadGamesForDate(String gameDate);

    @Query("WITH RAW_DATA AS " +
            "(SELECT " +
            " GS.COMPONENT_INSTANCE_ID AS DECK_ID " +
            ", GS.GAME_STATE " +
            ", GS.LAST_POSITION " +
            ", CAST((GS.LAST_POSITION * 1.0) / (SELECT DECKS.NUM_DECKS * 52.0 FROM DECKS WHERE DECKS.DECK_ID = GS.COMPONENT_INSTANCE_ID) * 100 AS INTEGER) AS PROGRESS " +
            ", (SELECT " +
            "COUNT(ERRORS.ERROR_ID) " +
            "FROM " +
            "CARD_RECALL_ERRORS ERRORS " +
            " WHERE " +
            "ERRORS.DECK_ID = GS.COMPONENT_INSTANCE_ID AND " +
            "ERRORS.SESSION_ID = GS.SESSION_ID AND " +
            "ERRORS.ATTEMPT_ID = GS.ATTEMPT_ID " +
            ") AS ERROR_COUNT " +
            ", GS.CUMULATIVE_STATE_DURATION AS DURATION " +
            ", CASE " +
            "WHEN " +
            "GS.GAME_STATE = 'RECALL' AND " +
            "GS.GAME_STATE_STATUS = 'COMPLETED' " +
            "THEN " +
            "'REPLAY' " +
            "ELSE " +
            "'RESUME' " +
            "END AS ACTION_TO_TAKE " +
            ", GS.LAST_MOD_DATE_TIME " +
            "FROM " +
            "GAME_SUMMARY GS " +
            "WHERE " +
            "DATE(SUBSTR(LAST_MOD_DATE_TIME, 1, 10), 'UNIXEPOCH') = :gameDate " +
            ")" +
            "SELECT " +
            "DECK_ID " +
            ", GAME_STATE " +
            ", PROGRESS " +
            ", ERROR_COUNT " +
            ", CASE " +
            "WHEN " +
            "GAME_STATE = 'RECALL' " +
            "THEN " +
            "IFNULL(100 - CAST((ERROR_COUNT * 1.0) / (LAST_POSITION * 1.0) * 100 AS INTEGER), 0) " +
            "ELSE " +
            "0 " +
            "END AS ACCURACY " +
            ", DURATION " +
            ", ACTION_TO_TAKE " +
            "FROM " +
            "RAW_DATA " +
            "ORDER BY " +
            "LAST_MOD_DATE_TIME DESC")
    public List<GameHistoryOverview> loadGameOverviewsForDate(String gameDate);

    @Query("WITH RAW_DATA AS " +
            "(SELECT " +
            " GS.COMPONENT_INSTANCE_ID AS DECK_ID " +
            ", GS.GAME_STATE " +
            ", GS.LAST_POSITION " +
            ", CAST((GS.LAST_POSITION * 1.0) / (SELECT DECKS.NUM_DECKS * 52.0 FROM DECKS WHERE DECKS.DECK_ID = GS.COMPONENT_INSTANCE_ID) * 100 AS INTEGER) AS PROGRESS " +
            ", (SELECT " +
            "COUNT(ERRORS.ERROR_ID) " +
            "FROM " +
            "CARD_RECALL_ERRORS ERRORS " +
            " WHERE " +
            "ERRORS.DECK_ID = GS.COMPONENT_INSTANCE_ID AND " +
            "ERRORS.SESSION_ID = GS.SESSION_ID AND " +
            "ERRORS.ATTEMPT_ID = GS.ATTEMPT_ID " +
            ") AS ERROR_COUNT " +
            ", GS.CUMULATIVE_STATE_DURATION AS DURATION " +
            ", CASE " +
            "WHEN " +
            "GS.GAME_STATE = 'RECALL' AND " +
            "GS.GAME_STATE_STATUS = 'COMPLETED' " +
            "THEN " +
            "'REPLAY' " +
            "ELSE " +
            "'RESUME' " +
            "END AS ACTION_TO_TAKE " +
            ", GS.LAST_MOD_DATE_TIME " +
            "FROM " +
            "GAME_SUMMARY GS " +
            ")" +
            "SELECT " +
            "DATE(SUBSTR(LAST_MOD_DATE_TIME, 1, 10), 'UNIXEPOCH') AS GAME_DATE" +
            ", DECK_ID " +
            ", GAME_STATE " +
            ", PROGRESS " +
            ", ERROR_COUNT " +
            ", CASE " +
            "WHEN " +
            "GAME_STATE = 'RECALL' " +
            "THEN " +
            "IFNULL(100 - CAST((ERROR_COUNT * 1.0) / (LAST_POSITION * 1.0) * 100 AS INTEGER), 0) " +
            "ELSE " +
            "0 " +
            "END AS ACCURACY " +
            ", DURATION " +
            ", ACTION_TO_TAKE " +
            "FROM " +
            "RAW_DATA " +
            "ORDER BY " +
            "LAST_MOD_DATE_TIME DESC")
    public List<GameHistoryOverview> loadGameOverviews();

    @Insert
    public void insertGameSummary(GameSummary gameSummary);

    @Update
    public void updateGameSummary(GameSummary gameSummary);

}
