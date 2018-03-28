package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Marc on 3/28/2018.
 */

@Entity(tableName = "GAME_STATE_STATUSES", foreignKeys = @ForeignKey(entity = GameStates.class, parentColumns = "STATE_ID", childColumns = "GAME_STATE_ID"))
public class GameStateStatuses {

    @ColumnInfo(name = "GAME_STATE_ID")
    private int gameStateId;

    @ColumnInfo(name = "STATUS")
    private String status;

    public void setGameStateId(int id) {

        this.gameStateId = id;

    }

    public int getGameStateId() {

        return this.gameStateId;

    }

    public void setStatus(String status) {

        this.status = status;

    }

    public String getStatus() {

        return this.status;

    }

}
