package com.main.Caller;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date fromPicker(Long value){
        return value == null ? null : new Date(value);
    }

    public static Date fromPickerInt(int value){
        return value == 0 ? null : new Date(value);
    }

    @TypeConverter
    public static Long fromPicker(Date date){
        return date == null ? null : date.getTime();
    }
}
