package com.main.Caller;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class StaffInfo {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "First_Name")
    public String firstname;

    @ColumnInfo(name = "Last_Name")
    public String lastname;

    @ColumnInfo(name = "Phone_Number")
    public String phoneNum;

    @ColumnInfo(name = "Email_Address")
    public String email;

    @ColumnInfo(name = "Trained_Opening")
    public String trained_open;

    @ColumnInfo(name = "Trained_Closing")
    public String trained_close;

    @ColumnInfo(name = "Employed")
    public String employed;

    @ColumnInfo(name = "Sunday")
    public int sunday;

    @ColumnInfo(name = "Monday")
    public int monday;

    @ColumnInfo(name = "Tuesday")
    public int tuesday;

    @ColumnInfo(name = "Wednesday")
    public int wednesday;

    @ColumnInfo(name = "Thursday")
    public int thursday;

    @ColumnInfo(name = "Friday")
    public int friday;

    @ColumnInfo(name = "Saturday")
    public int saturday;

    //This is the public interface for StaffInfo
    public StaffInfo(String firstname, String lastname, String phoneNum, String email, String trained_open, String trained_close) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNum = phoneNum;
        this.email = email;
        this.trained_open = trained_open;
        this.trained_close = trained_close;
    }

    //This is all the get/set/toString methods for this entity
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFName() {
        return firstname;
    }
    public void setFName(String firstname) {
        this.firstname = firstname;
    }

    public String getLName() {
        return lastname;
    }
    public void setLName(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTrainedOpen() {
        return trained_open;
    }
    public void setTrainedOpen(String trained) {
        this.trained_open = trained;
    }

    public String getTrained_close(){ return trained_close;}
    public void setTrained_close(String trained){ this.trained_close = trained;}

    public String getEmployed() { return employed; }
    public void setEmployed(String employed){ this.employed = employed;}

    public int[] getAvailability() {
        return new int[]{sunday, monday, tuesday, wednesday, thursday, friday, saturday};
    }
    public void setAvailability(int sun, int mon, int tue, int wed, int thu, int fri, int sat) {
        this.sunday    = sun;
        this.monday    = mon;
        this.tuesday   = tue;
        this.wednesday = wed;
        this.thursday  = thu;
        this.friday    = fri;
        this.saturday  = sat;
    }

//    @Override
//    public String toString() {
//        return "StaffInfo{" +
//                "firstname=" + firstname + '\'' +
//                ", lastname=" + lastname + '\'' +
//                ", phoneNumber=" + phoneNum + '\'' +
//                ", email=" + email + '\'' +
//                ", availability=" + availabilty +
//                '}';
//    }
}

