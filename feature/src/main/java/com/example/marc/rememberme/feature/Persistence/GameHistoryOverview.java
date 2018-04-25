package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marc on 4/17/2018.
 */

public class GameHistoryOverview {

    @ColumnInfo(name = "GAME_DATE")
    private String gameDate;
    @ColumnInfo(name = "DECK_ID")
    private int deckId;
    @ColumnInfo(name = "GAME_STATE")
    private String gameState;
    @ColumnInfo(name = "PROGRESS")
    private int progress;
    @ColumnInfo(name = "ERROR_COUNT")
    private int errorCount;
    @ColumnInfo(name = "ACCURACY")
    private int accuracy;
    @ColumnInfo(name = "DURATION")
    private long duration;
    @ColumnInfo(name = "ACTION_TO_TAKE")
    private String actionToTake;

    public void setGameDate(String gameDate) {this.gameDate = gameDate;}
    
    public void setDeckId(int id) {this.deckId = id;}

    public void setGameState(String gameState) {this.gameState = gameState;}

    public void setProgress(int progress) {this.progress = progress;}

    public void setErrorCount(int errorCount) {this.errorCount= errorCount;}

    public void setAccuracy(int accuracy) {this.accuracy = accuracy;}

    public void setDuration(long duration) {this.duration = duration;}

    public void setActionToTake(String action) {this.actionToTake = action;}

    public String getGameDate() {return gameDate;}

    public int getDeckId() {return deckId;}

    public String getGameState() {return gameState;}

    public int getProgress() {return progress;}

    public int getErrorCount() {return errorCount;}

    public int getAccuracy() {return accuracy;}

    public long getDuration() {return duration;}

    public String getActionToTake() {return actionToTake;}


}
