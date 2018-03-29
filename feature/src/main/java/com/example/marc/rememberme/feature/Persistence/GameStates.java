package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Marc on 3/28/2018.
 */

@Entity(
        tableName = "GAME_STATES",
        primaryKeys = {"GAME_ID", "STATE", "STATUS"},
        foreignKeys = @ForeignKey(entity = Games.class, parentColumns = "GAME_ID", childColumns = "GAME_ID"))
public class GameStates {

    @ColumnInfo(name = "GAME_ID")
    private int gameId;

    @ColumnInfo(name = "STATE")
    private String state;

    @ColumnInfo(name = "STATUS")
    private String status;

    public void setGameId(int id) {

        this.gameId = id;

    }

    public int getGameId() {

        return this.gameId;

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
