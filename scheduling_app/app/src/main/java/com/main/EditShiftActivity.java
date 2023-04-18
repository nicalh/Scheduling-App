package com.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.EntityIterator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.Caller.LocalDB;
import com.main.Caller.ShiftInfo;
import com.main.Caller.StaffInfo;
import com.main.databinding.ActivityEditShiftBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       EditShiftActivity.java
    Purpose:    Activity to edit a selected shift.
*/
public class EditShiftActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /*
    Used for displaying this fragment.
     */
    private @NonNull ActivityEditShiftBinding binding;

    DBViewModel sEditDBViewModel;
    DBViewModel sDBViewModel;
    private String dayOfWeek;
    private List<String> staffChoice = new ArrayList<>();
    private String staffMem;
    private Spinner EditShiftType;
    private Spinner EditStaff1;
    private Spinner EditStaff2;
    private Spinner EditStaff3;
    private String initialStaffMem1 = "";
    private String initialStaffMem2 = "";
    private String initialStaffMem3 = "";
    private String editStaffType;
    private int editStaffSid;
    private boolean namesShowing = false;
    private boolean submitOperational = false;

    private CalendarView editDatePicker;
    private String editDayOfWeek;
    private Date dateStart;
    private List<String> editStaffChoice1 = new ArrayList<>();
    private List<String> editStaffChoice2 = new ArrayList<>();
    private List<String> editStaffChoice3 = new ArrayList<>();
    private String no_emp_selected = "No selection";

    private ShiftInfo shift = null;
    /*
    Function:       onCreate
    Parameters:     Bundle savedInstanceState
    Returns:        void
    Purpose:        - to handle the initialization of the edit shift activity
                    - handles the edit shift logic

    Author:         Taylor Bennett
    Log (2021-10-27) BENNETT:   - Created the activity class
                                - added the toolbar edits
                                - added listeners for the cancel button
                                - return to mainActivity function
    Log (2021-10-29) BENNETT:   added the submit button handling area
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shift);
        binding = ActivityEditShiftBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // add in the AppBar with title "Edit Shift" & Change color
        binding.editshiftToolbar.setTitle(R.string.title_edit_shift);
        binding.editshiftToolbar.setTitleTextColor(getResources().getColor(R.color.design_default_color_on_primary));
        TextView warning = (TextView)findViewById(R.id.addstaff_conflictdisplay);

        //This will deal with setting up the system behind requesting shift data from the database
        editDatePicker = (CalendarView) findViewById(R.id.editshift_datepicker);
        editDatePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(CalendarView datePicker, int year, int month, int day) {
                dateStart = new Date(year - 1900, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Spinner EditShiftType = findViewById(R.id.editshift_shifttypespinner);
                String checkType = EditShiftType.getSelectedItem().toString();
                dayOfWeek = sdf.format(dateStart);
                editStaffChoice1.clear();
                editStaffChoice2.clear();
                editStaffChoice3.clear();
                staffChoice.clear();
                sEditDBViewModel = new ViewModelProvider(EditShiftActivity.this).get(DBViewModel.class);
                sDBViewModel = new ViewModelProvider(EditShiftActivity.this).get(DBViewModel.class);
                sEditDBViewModel.getAllShift().observe(EditShiftActivity.this, new Observer<List<ShiftInfo>>() {

                    @Override
                    public void onChanged(@Nullable final List<ShiftInfo> shiftInfo) {
                        //update the cached staff in the adaptor
                        if (submitOperational) {
                            namesShowing = true;
                        }
                        for (int i = 0; i < shiftInfo.size(); i++) {

                            if((dateStart.toString().equals(shiftInfo.get(i).date.toString())) && (checkType.equals(shiftInfo.get(i).shiftType))) {
                                submitOperational = true;
                                shift = shiftInfo.get(i);
                                editStaffSid  = shiftInfo.get(i).sid;
                                initialStaffMem1 = shiftInfo.get(i).staff1;
                                initialStaffMem2 = shiftInfo.get(i).staff2;
                                initialStaffMem3 = shiftInfo.get(i).staff3;
                                System.out.println("1: "+shiftInfo.get(i).staff1+" 2: "+shiftInfo.get(i).staff2+" 3: "+shiftInfo.get(i).staff3);
                                editStaffType = shiftInfo.get(i).shiftType;
                                editStaffChoice1.add(initialStaffMem1);
                                editStaffChoice2.add(initialStaffMem2);
                                editStaffChoice3.add(initialStaffMem3);
                                staffChoice.clear();
                                sDBViewModel.getAllStaff().observe(EditShiftActivity.this, new Observer<List<StaffInfo>>() {
                                    @Override
                                    public void onChanged(@Nullable final List<StaffInfo> staffInfo) {

                                        //update the cached staff in the adaptor
                                        staffChoice.add(no_emp_selected);
                                        for (int i = 0; i < staffInfo.size(); i++) {
                                            int[] avb = staffInfo.get(i).getAvailability();
                                            if ((dayOfWeek.equals("Monday")) & ((avb[1] == 1) || (avb[1] == 2) || (avb[1] == 3)) && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
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
                                            } else if ((dayOfWeek.equals("Saturday")) && (avb[6] == 3)  && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                                staffChoice.add(staffMem);
                                            } else if ((dayOfWeek.equals("Sunday")) && (avb[0] == 3)  && staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current))) {
                                                staffMem = staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname;
                                                staffChoice.add(staffMem);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                // spin up the staff spinners
                spinUpEditShift(EditStaff1, staffChoice, R.id.editshift_staff1spinner, initialStaffMem1);
                spinUpEditShift(EditStaff2, staffChoice, R.id.editshift_staff2spinner, initialStaffMem2);
                spinUpEditShift(EditStaff3, staffChoice, R.id.editshift_staff3spinner, initialStaffMem3);
            }
        });

        // handle the spinners
        spinUpEditSpinner(EditShiftType,R.array.shift_type, R.id.editshift_shifttypespinner);
        // initially populate spinners with date that is first selected
        spinUpEditShift(EditStaff1, staffChoice, R.id.editshift_staff1spinner, initialStaffMem1);
        spinUpEditShift(EditStaff2, staffChoice, R.id.editshift_staff2spinner, initialStaffMem2);
        spinUpEditShift(EditStaff3, staffChoice, R.id.editshift_staff3spinner, initialStaffMem3);

        // handle the submit button
        Button submit = (Button) findViewById(R.id.editshift_submitbutton);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                //if (shift != null) {

                    Spinner EditShiftType1 = findViewById(R.id.editshift_shifttypespinner);
                    // TODO - fix bug where user can change the shift type and still submit for the prev shift type

                    Spinner EditShift1Sub = findViewById(R.id.editshift_staff1spinner);
                    Spinner EditShift2Sub = findViewById(R.id.editshift_staff2spinner);
                    Spinner EditShift3Sub = findViewById(R.id.editshift_staff3spinner);
                    List<Spinner> staffSpinners = new ArrayList<Spinner>();
                    staffSpinners.add(EditShift1Sub);
                    staffSpinners.add(EditShift2Sub);
                    staffSpinners.add(EditShift3Sub);

                    if (EditShift1Sub.getAdapter().getCount() > 0) {

                        if (namesShowing) {
                            // first make sure staff are selected

                            if (!(Repeats(staffSpinners))) {
                                // edit the shift in the DB
                                warning.setVisibility(View.INVISIBLE);
                                String staff1 = EditShift1Sub.getSelectedItem().toString();
                                String staff2 = EditShift2Sub.getSelectedItem().toString();
                                String staff3 = EditShift3Sub.getSelectedItem().toString();
                                shift.setStaff1(staff1);
                                shift.setStaff2(staff2);
                                shift.setStaff3(staff3);
                                LocalDB.getDatabase(getApplicationContext()).getShiftDao().updateShift(shift);
                                returnToMainActivity();

                                // ***** checking if employees are trained is moved to the notifications tab *****

//                        // check if at least one of the staff are trained to open if shift is morning
//                        if (EditShiftType1.getSelectedItem().toString().equals("Morning") && (firstEmTrained.trained_open.equals("Trained") | secondEmTrained.trained_open.equals("Trained"))) {
//
//                        }
//
//                        // check if at least one of the staff are trained to close if shift is afternoon
//                        else if (EditShiftType1.getSelectedItem().toString().equals("Afternoon") && (firstEmTrained.trained_close.equals("Trained") | secondEmTrained.trained_close.equals("Trained"))) {
//                            warning.setVisibility(View.INVISIBLE);
//                            String staff1 = EditStaff1Sub.getSelectedItem().toString();
//                            String staff2 = EditStaff2Sub.getSelectedItem().toString();
//                            shift.setStaff1(staff1);
//                            shift.setStaff2(staff2);
//                            LocalDB.getDatabase(getApplicationContext()).getShiftDao().updateShift(shift);
//                            returnToMainActivity();
//                        }
//
//                        // make sure there are staff to open and close on weekend shifts
//                        else if (EditShiftType1.getSelectedItem().toString().equals("Weekend") && ((firstEmTrained.trained_open.equals("Trained") || secondEmTrained.trained_open.equals("Trained")) && (firstEmTrained.trained_close.equals("Trained") || secondEmTrained.trained_close.equals("Trained")))) {
//                            warning.setVisibility(View.INVISIBLE);
//                            String staff1 = EditStaff1Sub.getSelectedItem().toString();
//                            String staff2 = EditStaff2Sub.getSelectedItem().toString();
//                            shift.setStaff1(staff1);
//                            shift.setStaff2(staff2);
//                            LocalDB.getDatabase(getApplicationContext()).getShiftDao().updateShift(shift);
//                            returnToMainActivity();
//                        }
//
//                        // set warning text if no employee is trained to work
//                        else {
//                            warning.setText("Please ensure at least one employee is trained for this shift!");
//                            warning.setVisibility(View.VISIBLE);
//                        }
                            }
                            // set warning text if no employees are selected
                            else {
                                warning.setText("Please ensure different employees are selected!");
                                warning.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            //namesShowing = true;
                            warning.setText("Nothing to submit.");
                            warning.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        warning.setText("Nothing to submit.");
                        warning.setVisibility(View.VISIBLE);
                    }
                //}
//                else {
//                    warning.setText("Nothing to submit!");
//                    warning.setVisibility(View.VISIBLE);
                //}
            }
        });

        // handle the delete button
        Button delete = (Button) findViewById(R.id.editshift_removebutton);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                LocalDB.getDatabase(getApplicationContext()).getShiftDao().deleteShift(editStaffSid);
                // actions completed, return to main activity
                returnToMainActivity();
            }
        });

        // handle the cancel button
        Button cancel = (Button) findViewById(R.id.editshift_cancelbutton);
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
    public void spinUpEditSpinner(Spinner name, int textArrayResId, int spinnerId){
        name = (Spinner) findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                textArrayResId, android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);
    }

    public void spinUpEditShift(Spinner name, List<String> staff, int spinnerID, String compareName) {
        name = (Spinner) findViewById(spinnerID);
        name.setOnItemSelectedListener(this);
        SpinnerAdaptor adapter = new SpinnerAdaptor(this,
                android.R.layout.simple_spinner_dropdown_item, staff);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);
        for (int i=0; i<adapter.getCount(); i++) {
            int pos = adapter.getPosition(compareName);
            name.setSelection(pos);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch(parent.getId()) {
            case R.id.editshift_shifttypespinner:
            case R.id.editshift_staff1spinner:
            case R.id.editshift_staff2spinner:
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
     Returns:        None
     Purpose:        - navigates back to the mainActivity (actually parent activity)
     Author:         Taylor Bennett
     Log (2021-10-27) BENNETT:   Created this function
     */
    public void returnToMainActivity(){ NavUtils.navigateUpFromSameTask(this); }
}