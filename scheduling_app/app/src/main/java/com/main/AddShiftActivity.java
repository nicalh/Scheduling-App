package com.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.main.Caller.DateConverter;
import com.main.Caller.LocalDB;
import com.main.Caller.ShiftInfo;
import com.main.Caller.StaffInfo;
import com.main.databinding.ActivityAddShiftBinding;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       AddShiftActivity.java
    Purpose:    Activity to add a new shift in the schedule.
*/
public class AddShiftActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    /*
    Used for displaying this fragment.
     */
    DBViewModel sDBViewModel;
    private Spinner ShiftType;
    private Spinner Staff1;
    private Spinner Staff2;
    private Spinner Staff3;
    private String staffMem;

    private @NonNull ActivityAddShiftBinding binding;
    private CalendarView datePicker;
    private Date dateStart = null;
    private String dayOfWeek;
    private List<String> staffChoice = new ArrayList<>();
    private String no_emp_selected = "No selection";
    private int clicks = 0;

    Boolean firstTrained = null;
    Boolean secondTrained = null;

    public AddShiftActivity() {
    }

    /*
        Function:       onCreate
        Parameters:     Bundle savedInstanceState
        Returns:        void
        Purpose:        - to handle the initialization of the add shift activity
                        - handles the add shift logic

        Author:         Taylor Bennett
        Log (2021-10-27) BENNETT:   - Created the activity class
                                    - added the toolbar edits
                                    - added listeners for the cancel button
                                    - added listener for the submit button, but it
                                    currently does nothing.
                                    - return to mainActivity function
        Log (2021-10-29) BENNETT:   added the submit button handling area
        */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        binding = ActivityAddShiftBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // add in the AppBar with title "Add Shift"
        binding.addshiftToolbar.setTitle(R.string.title_add_shift);
        binding.addshiftToolbar.setTitleTextColor(getResources().getColor(R.color.design_default_color_on_primary));

        // Maybe a useful thing.... you can pass items onto new activities from
        // the intent construction in the thing that calls this activity

        TextView warning = (TextView)findViewById(R.id.addstaff_conflictdisplay);

        //Handles the date picker.
        //Setting up the datePicker such that when a date is selected it updates the staff 1 and staff 2
        //options with those that match the date chosen and the type, ie. if its a monday only those available on monday will show in the spinners
        datePicker = (CalendarView) findViewById(R.id.addshift_datepicker);

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView datePicker, int year, int month, int day) {
                clicks++;
                dateStart = new Date(year - 1900, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CANADA);
                dayOfWeek = sdf.format(dateStart);
                staffChoice.clear();
                sDBViewModel = new ViewModelProvider(AddShiftActivity.this).get(DBViewModel.class);
                sDBViewModel.getAllStaff().observe(AddShiftActivity.this, new Observer<List<StaffInfo>>() {

                    @Override
                    public void onChanged(@Nullable final List<StaffInfo> staffInfo) {
                        //update the cached staff in the adaptor
                        staffChoice.add(no_emp_selected);
                        for (int i = 0; i < staffInfo.size(); i++) {
                            int[] avb = staffInfo.get(i).getAvailability();
                            if ((dayOfWeek.equals("Monday")) && ((avb[1] == 1) || (avb[1] == 2) || (avb[1] == 3)) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                staffChoice.add(staffMem);
                            } else if ((dayOfWeek.equals("Tuesday")) && ((avb[2] == 1) || (avb[2] == 2) || (avb[2] == 3)) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                staffChoice.add(staffMem);
                            } else if ((dayOfWeek.equals("Wednesday")) && ((avb[3] == 1) || (avb[3] == 2) || (avb[3] == 3)) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                staffChoice.add(staffMem);
                            } else if ((dayOfWeek.equals("Thursday")) && ((avb[4] == 1) || (avb[4] == 2) || (avb[4] == 3)) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                staffChoice.add(staffMem);
                            } else if ((dayOfWeek.equals("Friday")) && ((avb[5] == 1) || (avb[5] == 2) || (avb[5] == 3)) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                staffChoice.add(staffMem);
                            } else if ((dayOfWeek.equals("Saturday")) && (avb[6] == 3) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                staffChoice.add(staffMem);
                            } else if ((dayOfWeek.equals("Sunday")) && (avb[0] == 3) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                staffChoice.add(staffMem);
                            }
                        }
                    }
                });

                // spin up the staff spinners
                spinUpShift(Staff1, staffChoice, R.id.addshift_staff1spinner);
                spinUpShift(Staff2, staffChoice, R.id.addshift_staff2spinner);
                spinUpShift(Staff3, staffChoice, R.id.addshift_staff3spinner);
            }
        });

        // spin up the shift type spinner
        spinUpSpinner(ShiftType,R.array.shift_type, R.id.addshift_typespinner);

        // handle the submit button
        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                // TODO - fix bug where user can change the shift type and still add the new shift under the prev shift type

                Spinner addshiftType = findViewById(R.id.addshift_typespinner);
                Spinner addStaff1 = findViewById(R.id.addshift_staff1spinner);
                Spinner addStaff2 = findViewById(R.id.addshift_staff2spinner);
                Spinner addStaff3 = findViewById(R.id.addshift_staff3spinner);
                List<Spinner> staffSpinners = new ArrayList<Spinner>();
                staffSpinners.add(addStaff1);
                staffSpinners.add(addStaff2);
                staffSpinners.add(addStaff3);

                // first make sure shift does not exist for selected date
                List<ShiftInfo> shifts = LocalDB.getDatabase(getApplicationContext()).getShiftDao().findShiftByDate(dateStart);
                boolean morningShift = false, afternoonShift = false, weekendShift = false;
                for (int i = 0; i < shifts.size(); i++) {
                    if (shifts.get(i).shiftType.equals("Morning")) {
                        morningShift = true;
                    }
                    if (shifts.get(i).shiftType.equals("Afternoon")) {
                        afternoonShift = true;
                    }
                    if (shifts.get(i).shiftType.equals("Weekend")) {
                        weekendShift = true;
                    }
                }

                // make sure spinners have been populated
                if (clicks > 1) {
//                for (int i=0; i<staffSpinners.size(); i++) {
//                    for (int j=0; j<staffSpinners.size(); j++) {
//                        if (staffSpinners)
//                    }
//                }

                    if ((addshiftType.getSelectedItem().toString().equals("Morning") && !morningShift) || (addshiftType.getSelectedItem().toString().equals("Afternoon") && !afternoonShift) || (addshiftType.getSelectedItem().toString().equals("Weekend") && !weekendShift)) {
                        // next make sure there are no repeats in selected staff
                        if (!(Repeats(staffSpinners))) {

                            // check if wrong shift type is entered for the day of week selected
                            if ((addshiftType.getSelectedItem().toString().equals("Morning") && ((dateStart.getDay() == 0) || (dateStart.getDay() == 6))) || (addshiftType.getSelectedItem().toString().equals("Afternoon") && ((dateStart.getDay() == 0) || (dateStart.getDay() == 6))) || (addshiftType.getSelectedItem().toString().equals("Weekend") && ((dateStart.getDay() > 0) && (dateStart.getDay() < 6)))) {
                                warning.setText("Only weekend shifts can be added to Saturday and Sunday!");
                                warning.setVisibility(View.VISIBLE);
                            } else {

                                // add shift to the DB
                                warning.setVisibility(View.INVISIBLE);
                                String shiftType = addshiftType.getSelectedItem().toString();
                                String staff1 = addStaff1.getSelectedItem().toString();
                                String staff2 = addStaff2.getSelectedItem().toString();
                                String staff3 = addStaff3.getSelectedItem().toString();
                                ShiftInfo Shift = new ShiftInfo(dateStart, shiftType, staff1, staff2, staff3);
                                LocalDB.getDatabase(getApplicationContext()).getShiftDao().insertShift(Shift);
                                returnToMainActivity();

                                // ***** checking if employees are trained is moved to the notifications tab *****

//                            // check if at least one of the staff are trained to open if shift is morning
//                            if (addshiftType.getSelectedItem().toString().equals("Morning") && (firstEmTrained.trained_open.equals("Trained") | secondEmTrained.trained_open.equals("Trained"))) {
//
//                            }
//
//                            // check if at least one of the staff are trained to close if shift is afternoon
//                            else if (addshiftType.getSelectedItem().toString().equals("Afternoon") && (firstEmTrained.trained_close.equals("Trained") || secondEmTrained.trained_close.equals("Trained"))) {
//                                warning.setVisibility(View.INVISIBLE);
//                                String shiftType = addshiftType.getSelectedItem().toString();
//                                String staff1 = addStaff1.getSelectedItem().toString();
//                                String staff2 = addStaff2.getSelectedItem().toString();
//                                ShiftInfo Shift = new ShiftInfo(dateStart, shiftType, staff1, staff2);
//                                LocalDB.getDatabase(getApplicationContext()).getShiftDao().insertShift(Shift);
//                                returnToMainActivity();
//                            }
//
//                            // make sure there are staff to open and close on weekend shifts
//                            else if (addshiftType.getSelectedItem().toString().equals("Weekend") && ((firstEmTrained.trained_open.equals("Trained") || secondEmTrained.trained_open.equals("Trained")) && (firstEmTrained.trained_close.equals("Trained") || secondEmTrained.trained_close.equals("Trained")))) {
//                                warning.setVisibility(View.INVISIBLE);
//                                String shiftType = addshiftType.getSelectedItem().toString();
//                                String staff1 = addStaff1.getSelectedItem().toString();
//                                String staff2 = addStaff2.getSelectedItem().toString();
//                                ShiftInfo Shift = new ShiftInfo(dateStart, shiftType, staff1, staff2);
//                                LocalDB.getDatabase(getApplicationContext()).getShiftDao().insertShift(Shift);
//                                returnToMainActivity();
//                            }
//                            // set warning text if no employee is trained to work
//                            else {
//                                warning.setText("Please ensure at least one employee is trained for this shift!");
//                                warning.setVisibility(View.VISIBLE);
//                            }
                            }
                        }
                        // set warning text if no employees are selected
                        else {
                            warning.setText("Please ensure different employees are selected!");
                            warning.setVisibility(View.VISIBLE);
                        }
                    }
                    // set warning text if shift already exists
                    else {
                        warning.setText("Shift already exists for selected day and time!");
                        warning.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    warning.setText("Please select a date (spinners must be populated).");
                    warning.setVisibility(View.VISIBLE);
                }
            }
        });

        // handle the cancel button
        Button cancel = (Button) findViewById(R.id.addshift_cancelbutton);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                returnToMainActivity();
            }
        });
    }


    /*
     Function:       spinUpSpinner()
     Parameters:     int textArrayResId, int spinnerId
     Returns:        None
     Purpose:        - takes the text array id and finds it for the array adapter
                     - takes the spinnerID and applies the adapter to it
                     - basically this function takes the spinner information and
                     populates the spinner with the information provided.

     Author:         Taylor Bennett
     Log (2021-10-28) BENNETT:   Created this function
     */
    public void spinUpSpinner(Spinner name, int textArrayResId, int spinnerId){
        name = (Spinner) findViewById(spinnerId);
        name.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                textArrayResId, android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);
    }


    public void spinUpShift(Spinner name, List<String> staff, int spinnerID) {
        name = (Spinner) findViewById(spinnerID);
        name.setOnItemSelectedListener(this);
        SpinnerAdaptor adapter = new SpinnerAdaptor(this,
                android.R.layout.simple_spinner_dropdown_item, staff);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch(parent.getId()) {
            case R.id.addshift_staff1spinner:
            case R.id.addshift_staff2spinner:
            case R.id.addshift_typespinner:
                String Choice = (String) parent.getItemAtPosition(pos);
                break;
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public boolean Repeats(List <Spinner> spinnerList) {
        // compare each name selected in the spinner together, make sure no names are the same if they aren't 'No selection'
        for (int i=0; i<spinnerList.size(); i++) {
            for (int j=0; j<spinnerList.size(); j++) {
                // make sure that selection is not 'No selection'
                if (!(spinnerList.get(i).getSelectedItem().toString().equals(no_emp_selected))) {
                    // make sure not comparing the same spinner
                    if (i != j) {
                        if (spinnerList.get(i).getSelectedItem().toString().equals(spinnerList.get(j).getSelectedItem().toString())) {
                            // two are the same
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    /*
     Function:       returnToMainActivity
     Parameters:     None
     Returns:        void
     Purpose:        - navigates back to the mainActivity (actually parent activity)

     Author:         Taylor Bennett
     Log (2021-10-27) BENNETT:   Created this function
     */
    public void returnToMainActivity(){
        NavUtils.navigateUpFromSameTask(this);
    }
}