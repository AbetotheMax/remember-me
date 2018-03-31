package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Marc on 3/31/2018.
 */

public class Converters {

    @TypeConverter
    public static Date toDate(Long date) {

        return date == null ? null : new Date(date);

    }

    @TypeConverter
    public static Long toLong(Date date) {

        return date == null ? null : date.getTime();

    }

}
