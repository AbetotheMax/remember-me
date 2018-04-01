package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Marc on 3/28/2018.
 */

@Entity(
        tableName = "GAME_STATES",
        primaryKeys = {"GAME_DESC", "STATE", "STATUS"},
        foreignKeys = @ForeignKey(entity = Games.class, parentColumns = "GAME_DESC", childColumns = "GAME_DESC"))
public class GameStates {

    @ColumnInfo(name = "GAME_DESC")
    @NonNull
    private String gameDesc;

    @ColumnInfo(name = "STATE")
    @NonNull
    private String state;

    @ColumnInfo(name = "STATUS")
    @NonNull
    private String status;

    public void setGameDesc(String description) {

        this.gameDesc = description;

    }

    public String getGameDesc() {

        return this.gameDesc;

    }

    public void setState(String state) {

        this.state = state;

    }

    public String getState() {

        return this.state;

    }

    public void setStatus(String status) {

        this.status = status;

    }

    public String getStatus() {

        return this.status;

    }

}
