package com.main.Caller;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LocalDBRepo {

    private StaffDao mStaffDao;
    private ShiftDao mShiftDao;

    private LiveData<List<StaffInfo>> mAllStaff;
    private LiveData<List<ShiftInfo>> mAllShifts;
    private List<ShiftInfo> mShiftsID;

    public LocalDBRepo(Application application){
        LocalDB ldb = LocalDB.getDatabase(application);
        mStaffDao = ldb.getStaffDao();
        mShiftDao = ldb.getShiftDao();

        mAllStaff = mStaffDao.getAllStaff();
        mAllShifts = mShiftDao.getAllShift();

    }

    public LiveData<List<StaffInfo>> getAllStaff(){
        return mAllStaff;
    }

    public LiveData<List<ShiftInfo>> getAllShifts(){
        return mAllShifts;
    }

    public void insertStaff(StaffInfo staffInfo){
        new insertStaffAsyncTask(mStaffDao).execute(staffInfo);
    }

    private static class insertStaffAsyncTask extends AsyncTask<StaffInfo, Void, Void>{
        private StaffDao mAsyncTaskStaffDao;

        insertStaffAsyncTask(StaffDao staffDao){
            mAsyncTaskStaffDao = staffDao;
        }

        @Override
        protected Void doInBackground(final StaffInfo... params){
            mAsyncTaskStaffDao.insertStaff(params[0]);
            return null;
        }
    }

    public void insertShift(ShiftInfo shiftInfo){
        new insertShiftAsyncTask(mShiftDao).execute(shiftInfo);
    }

    private static class insertShiftAsyncTask extends AsyncTask<ShiftInfo, Void, Void>{
        private ShiftDao mAsyncTaskShiftDao;

        insertShiftAsyncTask(ShiftDao shiftDao){
            mAsyncTaskShiftDao = shiftDao;
        }

        @Override
        protected Void doInBackground(final ShiftInfo... params){
            mAsyncTaskShiftDao.insertShift(params[0]);
            return null;
        }
    }

}
