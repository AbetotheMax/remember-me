package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import java.util.Date;

/**
 * Created by Marc on 3/29/2018.
 */

@Entity(
        tableName = "GAME_HISTORY",
        primaryKeys = {"SESSION_ID", "ATTEMPT_D", "GAME_STATE", "GAME_STATE_STATUS"},
        foreignKeys = {
                @ForeignKey(
                        entity = Games.class,
                        parentColumns = "GAME_ID",
                        childColumns = "GAME_ID"
                ),
                @ForeignKey(
                        entity = GameStates.class,
                        parentColumns = {"STATE", "STATUS"},
                        childColumns = {"GAME_STATE", "GAME_STATE_STATUS"}
                )
        }
)
public class GameHistory {

    @ColumnInfo(name = "SESSION_ID")
    private int sessionId;

    @ColumnInfo(name = "ATTEMPT_ID")
    private int attemptId;

    @ColumnInfo(name = "GAME_ID")
    private int gameId;

    @ColumnInfo(name = "GAME_STATE")
    private String gameState;

    @ColumnInfo(name = "GAME_STATE_STATUS")
    private String gameStateStatus;

    @ColumnInfo(name = "LAST_POSITION")
    private int lastPosition;

    @ColumnInfo(name = "ERRORS")
    private boolean errors;

    @ColumnInfo(name = "CUMULATIVE_STATE_DURATION")
    private long cumulativeStateDuration;

    @ColumnInfo(name = "LAST_MOD_DATE_TIME")
    private Date lastModDateTime;

    public void setSessionId(int id) {
        this.sessionId = id;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void setAttemptId(int id) {
        this.attemptId = id;
    }

    public int getAttemptId() {
        return this.attemptId;
    }

    public void setGameId(int id) {
        this.gameId = id;
    }

    public int getGameId() {
        return this.gameId;
    }

    public void setGameState(String state) {
        this.gameState = state;
    }

    public String getGameState() {
        return this.gameState;
    }

    public void setGameStateStatus(String status) {
        this.gameStateStatus = status;
    }

    public String getGameStateStatus() {
        return this.gameStateStatus;
    }

    public void setLastPosition(int position) {
        this.lastPosition = position;
    }

    public int getLastPosition() {
        return this.lastPosition;
    }

    public void setErrors(boolean hasErrors) {
        this.errors = hasErrors;
    }

    public boolean getErrors() {
        return this.errors;
    }

    public void setCumulativeStateDuration(long duration) {
        this.cumulativeStateDuration = duration;
    }

    public long getCumulativeStateDuration() {
        return this.cumulativeStateDuration;
    }

    public void setLastModDateTime(Date lastModDateTime) {
        this.lastModDateTime = lastModDateTime;
    }

    public Date getLastModDateTime() {
        return this.lastModDateTime;
    }

}
