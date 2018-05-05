package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marc on 4/17/2018.
 */

public class GameHistoryOverview implements Parcelable{

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

    public GameHistoryOverview() {}

    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getGameDate());
        dest.writeInt(getDeckId());
        dest.writeString(getGameState());
        dest.writeInt(getProgress());
        dest.writeInt(getErrorCount());
        dest.writeInt(getAccuracy());
        dest.writeLong(getDuration());
        dest.writeString(getActionToTake());

    }

    public GameHistoryOverview(Parcel in) {

        setGameDate(in.readString());
        setDeckId(in.readInt());
        setGameState(in.readString());
        setProgress(in.readInt());
        setErrorCount(in.readInt());
        setAccuracy(in.readInt());
        setDuration(in.readLong());
        setActionToTake(in.readString());

    }

    public static final Parcelable.Creator<GameHistoryOverview> CREATOR = new Parcelable.Creator<GameHistoryOverview>() {

        public GameHistoryOverview createFromParcel(Parcel in) {

            return new GameHistoryOverview(in);
        }

        public GameHistoryOverview[] newArray(int size) {

            return new GameHistoryOverview[size];

        }

    };


}
