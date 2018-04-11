package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import java.util.List;

/**
 * Created by Marc on 4/11/2018.
 */

@Dao
public interface GameStatesDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertGameStates(List<GameStates> gameStates);

}
