package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.security.PrivilegedAction;

/**
 * Created by Marc on 3/28/2018.
 */

@Entity(tableName = "GAMES", indices = {@Index(name = "INDX_GAME_DESC", unique = true, value = "GAME_DESC")})
public class Games {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "GAME_ID")
    private int gameId;

    @ColumnInfo(name = "GAME_DESC")
    private String gameDescription;

    @ColumnInfo(name = "GAME_COMPONENTS")
    private String gameComponents;

    public void setGameId(int id) {

        this.gameId = id;

    }

    public int getGameId() {

        return this.gameId;

    }

    public void setGameDescription(String description) {

        this.gameDescription = description;

    }

    public String getGameDescription() {

        return this.gameDescription;

    }

    public void setGameComponents(String components) {

        this.gameComponents = components;

    }

    public String getGameComponents() {

        return this.gameComponents;

    }

}
