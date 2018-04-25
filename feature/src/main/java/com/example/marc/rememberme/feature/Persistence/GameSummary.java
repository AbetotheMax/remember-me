package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Marc on 4/1/2018.
 */

@Entity(tableName = "GAME_SUMMARY", primaryKeys = {"SESSION_ID", "ATTEMPT_ID"})
@TypeConverters(Converters.class)

public class GameSummary implements Parcelable{

    @ColumnInfo(name = "SESSION_ID")
    @NonNull
    private int sessionId;

    @ColumnInfo(name = "ATTEMPT_ID")
    @NonNull
    private int attemptId;

    @ColumnInfo(name = "COMPONENT_INSTANCE_ID")
    private int componentInstanceId;

    @ColumnInfo(name = "GAME_DESC")
    private String gameDesc;

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

    public GameSummary() {}

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

    public void setComponentInstanceId(int id) {this.componentInstanceId = id;}

    public int getComponentInstanceId() {return this.componentInstanceId;}

    public void setGameDesc (String game) {
        this.gameDesc = game;
    }

    public String getGameDesc() {
        return this.gameDesc;
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

    @Override
    public String toString() {
        return "Session ID: " + getSessionId() + ", Attempt ID: " + getAttemptId() + ", Component " +
                "Instance ID: " + getComponentInstanceId() + ", Game Desc: " + getGameDesc() +
                ", Game State: " + getGameState() + ", Game State Status: " + getGameStateStatus() +
                ", Last Position: " + getLastPosition() + ", Errors: " + getErrors() +
                ", Cumulative State Duration: " + getCumulativeStateDuration() +
                ", Last Mod Date Time: " + getLastModDateTime() + ".";
    }

    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(getSessionId());
        dest.writeInt(getAttemptId());
        dest.writeInt(getComponentInstanceId());
        dest.writeString(getGameDesc());
        dest.writeString(getGameState());
        dest.writeString(getGameStateStatus());
        dest.writeInt(getLastPosition());
        dest.writeInt(getErrors() ? 1 : 0);
        dest.writeLong(getCumulativeStateDuration());
        dest.writeLong(getLastModDateTime().getTime());

    }

    public GameSummary(Parcel in) {

        setSessionId(in.readInt());
        setAttemptId(in.readInt());
        setComponentInstanceId(in.readInt());
        setGameDesc(in.readString());
        setGameState(in.readString());
        setGameStateStatus(in.readString());
        setLastPosition(in.readInt());
        setErrors(in.readInt() != 0);
        setCumulativeStateDuration(in.readLong());
        setLastModDateTime(new Date(in.readLong()));

    }

    public static final Parcelable.Creator<GameSummary> CREATOR = new Parcelable.Creator<GameSummary>() {

        public GameSummary createFromParcel(Parcel in) {

            return new GameSummary(in);
        }

        public GameSummary[] newArray(int size) {

            return new GameSummary[size];

        }

    };

}
