package com.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.main.Caller.LocalDBRepo;
import com.main.Caller.ShiftInfo;
import com.main.Caller.StaffInfo;

import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private LocalDBRepo mLocalDBRepo;

    private LiveData<List<StaffInfo>> mALLStaff;
    private LiveData<List<ShiftInfo>> mAllShift;
    private List<ShiftInfo> mShiftsID;

    public DBViewModel(@NonNull Application application) {
        super(application);
        mLocalDBRepo = new LocalDBRepo(application);
        mALLStaff = mLocalDBRepo.getAllStaff();
        mAllShift = mLocalDBRepo.getAllShifts();
    }

    LiveData<List<StaffInfo>> getAllStaff(){
        return mALLStaff;
    }

    public void insertStaff(StaffInfo staffInfo){
        mLocalDBRepo.insertStaff(staffInfo);
    }

    LiveData<List<ShiftInfo>> getAllShift(){
        return mAllShift;
    }

    public void insertShift(ShiftInfo shiftInfo){
        mLocalDBRepo.insertShift(shiftInfo);
    }

    List<ShiftInfo> findShiftByID() {return mShiftsID;}
}
