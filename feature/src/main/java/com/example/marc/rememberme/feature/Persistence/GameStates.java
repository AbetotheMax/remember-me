package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Marc on 3/28/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = Games.class, parentColumns = "GAME_ID", childColumns = "GAME_ID"), tableName = "GAME_STATES")
public class GameStates {

    @ColumnInfo(name = "GAME_ID")
    private int gameId;

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "STATE_ID")
    private int stateId;

    @ColumnInfo(name = "STATE_DESC")
    private String stateDescription;

    public void setGameId(int id) {

        this.gameId = id;

    }

    public int getGameId() {

        return this.gameId;

    }

    public void setStateId(int id) {

        this.stateId = id;

    }

    public int getStateId() {

        return this.stateId;

    }

    public void setStateDescription(String description) {

        this.stateDescription = description;

    }

    public String getStateDescription() {

        return this.stateDescription;

    }

}
