package com.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextClock;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.main.Caller.LocalDB;
import com.main.Caller.ShiftInfo;
import com.main.databinding.FragmentHomeBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       HomeFragment.java
    Purpose:    Handles the generation and deletion of the home fragment.
*/
public class HomeFragment extends Fragment {

    LocalDB HomeDBViewModel;
    /*
    Calendar that displays shifts.
     */
    CalendarView calendar;
    TextView morningShiftText;
    TextView morningStaff1;
    TextView morningStaff2;
    TextView morningStaff3;
    TextView afternoonShiftText;
    TextView afternoonStaff1;
    TextView afternoonStaff2;
    TextView afternoonStaff3;
    Date selectedDate;
    /*
    Used for displaying this fragment.
     */
    private FragmentHomeBinding binding;

    /*
        Function:       onCreate
        Parameters:     Bundle savedInstanceState
        Returns:        void
        Purpose:        - to handle the initialization of the dashboard fragment

        Author:         Mitchell Driedger
        Log (2021-10-23) DRIEDGER:   - Created this function
        */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /*
        Function:       onCreateView
        Parameters:     LayoutInflater inflate
                        ViewGroup container
                        Bundle savedInstanceState
        Returns:        View
        Purpose:        - to handle the initialization of the view of the dashboard fragment

        Author:         Mitchell Driedger
        Log (2021-10-23) DRIEDGER:   - Created this function
        */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    /*
        Function:       onViewCreated
        Parameters:     View view
                        Bundle savedInstanceState
        Returns:        void
        Purpose:        - contains scripting associated with fragment

        Author:         Mitchell Driedger
        Log (2021-10-23) DRIEDGER:   - Created this function
                                     - Added calendar functionality
        */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // get text views below calendar
        morningShiftText = (TextView) getView().findViewById(R.id.textView1);
        morningStaff1 = (TextView) getView().findViewById(R.id.morningStaff1);
        morningStaff2 = (TextView) getView().findViewById(R.id.morningStaff2);
        morningStaff3 = (TextView) getView().findViewById(R.id.morningStaff3);
        afternoonShiftText = (TextView) getView().findViewById(R.id.textView2);
        afternoonStaff1 = (TextView) getView().findViewById(R.id.afternoonStaff1);
        afternoonStaff2 = (TextView) getView().findViewById(R.id.afternoonStaff2);
        afternoonStaff3 = (TextView) getView().findViewById(R.id.afternoonStaff3);

        // get date selected from calendar
        calendar = (CalendarView) getView().findViewById(R.id.simpleCalendarView); // get the reference of CalendarView
        calendar.setDate(System.currentTimeMillis(), false, true);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date
                selectedDate = new Date(year-1900, month, dayOfMonth);
                List<ShiftInfo> shifts = LocalDB.getDatabase(getActivity().getApplicationContext()).getShiftDao().findShiftByDate(selectedDate);
                ShiftInfo morningShift = null;
                ShiftInfo afternoonShift = null;
                ShiftInfo weekendShift = null;

                // set shifts if they exist
                for (int i=0; i<shifts.size(); i++) {
                    if (shifts.get(i).shiftType.equals("Morning")) {
                        morningShift = shifts.get(i);
                    }
                    if (shifts.get(i).shiftType.equals("Afternoon")) {
                        afternoonShift = shifts.get(i);
                    }
                    if (shifts.get(i).shiftType.equals("Weekend")) {
                        weekendShift = shifts.get(i);
                    }
                }

                String noShift = "No Shift Scheduled";
                if ((selectedDate.getDay() == 0) || (selectedDate.getDay() == 6)) {
                    morningShiftText.setText(R.string.weekendShift);
                    afternoonShiftText.setText("");
                    afternoonStaff1.setText("");
                    afternoonStaff2.setText("");
                    afternoonStaff3.setText("");
                    if (weekendShift != null) {
                        morningStaff1.setText(weekendShift.getStaffName1());
                        morningStaff2.setText(weekendShift.getStaffName2());
                        morningStaff3.setText(weekendShift.getStaffName3());
                    }
                    else {
                        morningStaff1.setText("");
                        morningStaff2.setText(noShift);
                        morningStaff3.setText("");
                    }
                }
                else {
                    morningShiftText.setText(R.string.morningShift);
                    afternoonShiftText.setText(R.string.afternoonShift);

                    // set text to shift staff if shift exists
                    if (morningShift != null) {
                        morningStaff1.setText(morningShift.getStaffName1());
                        morningStaff2.setText(morningShift.getStaffName2());
                        morningStaff3.setText(morningShift.getStaffName3());
                    } else {
                        morningStaff1.setText("");
                        morningStaff2.setText(noShift);
                        morningStaff3.setText("");
                    }
                    if (afternoonShift != null) {
                        afternoonStaff1.setText(afternoonShift.getStaffName1());
                        afternoonStaff2.setText(afternoonShift.getStaffName2());
                        afternoonStaff3.setText(afternoonShift.getStaffName3());
                    } else {
                        afternoonStaff1.setText("");
                        afternoonStaff2.setText(noShift);
                        afternoonStaff3.setText("");
                    }
                }
            }
        });



    }


    /*
        Function:       onDestroyView
        Parameters:     None
        Returns:        void
        Purpose:        - sets binding to null when view is destroyed

        Author:         Mitchell Driedger
        Log (2021-10-23) DRIEDGER:   - Created this function
        */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}