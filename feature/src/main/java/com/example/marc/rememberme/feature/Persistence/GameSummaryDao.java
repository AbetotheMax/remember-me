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

    @Insert
    public void insertGameSummary(GameSummary gameSummary);

    @Update
    public void updateGameSummary(GameSummary gameSummary);

}
