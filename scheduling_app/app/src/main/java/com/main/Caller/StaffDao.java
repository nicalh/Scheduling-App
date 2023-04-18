package com.main.Caller;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StaffDao {
    @Query("SELECT * FROM StaffInfo")
    LiveData<List<StaffInfo>> getAllStaff();

    @Query("SELECT * FROM StaffInfo WHERE uid IN (:userIds)")
    List<StaffInfo> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM StaffInfo WHERE First_Name LIKE :fName AND " +
            "Last_Name LIKE :lName LIMIT 1")
    StaffInfo findByName(String fName, String lName);

    @Query("SELECT * FROM StaffInfo WHERE UID LIKE :UID LIMIT 1")
    StaffInfo findByUID(int UID);

    //Insert single staff
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStaff(StaffInfo Staff);

    //Insert Multiple Staff
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMStaffs(StaffInfo... Staff);

    // Update Single Staff
    @Update
    public void updateUser(StaffInfo Staff);

    @Delete
    void deleteStaff(StaffInfo Staff);
}
