package com.main.Caller;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ShiftInfo {
    @PrimaryKey(autoGenerate = true)
    public int sid;

    @ColumnInfo(name = "Date")
    public Date date;

    @ColumnInfo(name = "Shift_Type")
    public String shiftType;

    @ColumnInfo(name = "Staff_1")
    public String staff1;

    @ColumnInfo(name = "Staff_2")
    public String staff2;

    @ColumnInfo(name = "Staff_3")
    public String staff3;

    public ShiftInfo(Date date, String shiftType, String staff1, String staff2, String staff3){
        this.date = date;
        this.shiftType = shiftType;
        this.staff1 = staff1;
        this.staff2 = staff2;
        this.staff3 = staff3;
    }

    //This is all the get/set/toString methods for this entity
    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getShiftType() {
        return shiftType;
    }
    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getStaffName1() { return staff1;}
    public void setStaff1(String staff1) {this.staff1 = staff1;}

    public String getStaffName2() { return staff2;}
    public void setStaff2(String staff2) {
        this.staff2 = staff2;
    }

    public String getStaffName3() { return staff3;}
    public void setStaff3(String staff3) {
        this.staff3 = staff3;
    }
}

