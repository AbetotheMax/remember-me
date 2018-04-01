package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Marc on 3/31/2018.
 */

@Dao
public interface GameHistoryDao {

    @Query(
            "SELECT * FROM GAME_HISTORY " +
                    "WHERE SESSION_ID = :sessionId AND " +
                    "ATTEMPT_ID = :attemptId AND " +
                    "GAME_STATE = :gameState AND " +
                    "GAME_STATE_STATUS = :gameStateStatus AND " +
                    "LAST_MOD_DATE_TIME = " +
                        "(SELECT MAX(LAST_MOD_DATE_TIME) FROM GAME_HISTORY WHERE SESSION_ID = :sessionId AND " +
                            "ATTEMPT_ID = :attemptId AND " +
                            "GAME_STATE = :gameState AND " +
                            "GAME_STATE_STATUS = :gameStateStatus" +
                    ")"
    )
    public GameHistory loadGameFromGameState(int sessionId, int attemptId, String gameState, String gameStateStatus);

    @Query("SELECT * FROM GAME_HISTORY WHERE SESSION_ID = :sessionId AND ATTEMPT_ID = :attemptId ORDER BY LAST_MOD_DATE_TIME")
    public List<GameHistory> loadGameHistoryForAttempt(int sessionId, int attemptId);

    @Query("SELECT * FROM GAME_HISTORY WHERE COMPONENT_INSTANCE_ID = :componentInstanceId ORDER BY ATTEMPT_ID, LAST_MOD_DATE_TIME")
    public List<GameHistory> loadGameHistoryForGame(int componentInstanceId);

    @Query("SELECT MAX(SESSION_ID) FROM GAME_HISTORY WHERE GAME_DESC = :gameDesc")
    public int getMaxSessionId(String gameDesc);

    @Insert
    public void insertGameState(GameHistory gameHistory);

}
