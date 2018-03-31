package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Marc on 3/31/2018.
 */

@Dao
public interface CardRecallErrorsDao {

    @Query("SELECT * FROM CARD_RECALL_ERRORS WHERE SESSION_ID = :sessionId AND ATTEMPT_ID = :attemptId")
    public List<CardRecallErrors> loadErrorsForAttempt(int sessionId, int attemptId);

    @Query("SELECT COUNT(ERROR_ID) FROM CARD_RECALL_ERRORS WHERE SESSION_ID = :sessionId AND ATTEMPT_ID = :attemptId")
    public int getNumErrorsForAttempt(int sessionId, int attemptId);

    @Insert
    public void insertCardRecallError(CardRecallErrors cardRecallErrors);

}
