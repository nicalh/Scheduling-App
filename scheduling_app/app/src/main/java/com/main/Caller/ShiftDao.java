package com.main.Caller;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface ShiftDao {
    @Query("SELECT * FROM ShiftInfo")
    LiveData<List<ShiftInfo>> getAllShift();

    @Query("SELECT * FROM ShiftInfo WHERE sid IN (:ShiftIds)")
    List<ShiftInfo> loadAllByIds(int[] ShiftIds);

    //Searches for the date
    @Query("SELECT * FROM ShiftInfo WHERE Date = :cDate")
    List<ShiftInfo> findShiftByDate(Date cDate);

    //Search for all shifts where user id is something
    @Query("SELECT * FROM ShiftInfo WHERE  Staff_1 = :name or Staff_2 = :name or Staff_3 = :name")
    List<ShiftInfo> findShiftByName(String name);

    //Insert single shift
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShift(ShiftInfo Shift);

    //Insert Multiple Shifts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMShifts(ShiftInfo... Shift);

    // Update Single Staff
    @Update
    public void updateShift(ShiftInfo Shift);

    @Query("DELETE FROM ShiftInfo WHERE sid = :sid")
    public void deleteShift(int sid);

    @Delete
    public void deleteShiftbyShift(ShiftInfo Shift);
}
