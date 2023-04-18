package com.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.main.Caller.LocalDB;
import com.main.Caller.ShiftInfo;
import com.main.Caller.StaffInfo;
import com.main.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       DashboardFragment.java
    Purpose:    Handles the generation and deletion of the dashboard fragment.
*/
public class DashboardFragment extends Fragment{
    /*
    List of employees.
     */
    ListView employeeListView;
    /*
    Filter used for search bar.
     */
    EditText theFilter;
    /*
    Used for displaying this fragment.
     */
    private FragmentDashboardBinding binding;
    Intent receive;
    StaffInfo staff;
    DBViewModel mDBViewModel;
    DBViewModel secondMDBViewModel;
    CheckBox displayEmployment;
    boolean employment;
    SimpleArrayAdapter itemsAdapter;


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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    /*
        Function:       onViewCreated
        Parameters:     View view
                        Bundle savedInstanceState
        Returns:        void
        Purpose:        - contains scripting associated with fragment

        Author:         Mitchell Driedger
        Log (2021-10-23) DRIEDGER:   - Created this function
                                     - Added array adapter functionality
        Log (2021-10-27) HOGAN:      - Added searchbar functionality
        */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //TODO - populate lists with names and phone numbers from the database

        List names = new ArrayList<String>();
        List phones = new ArrayList<String>();
        List emails = new ArrayList<String>();
        List employed = new ArrayList<String>();
        List trained_open = new ArrayList<String>();
        List trained_close = new ArrayList<String>();
        List upcomingShift = new ArrayList<List>();

        // Populate list with employees
        employeeListView = (ListView) getView().findViewById(R.id.listview);
        itemsAdapter = new SimpleArrayAdapter(getActivity(), names, phones, emails, employed, trained_open, trained_close, upcomingShift);

        //This sets up the ViewModel to be observed
        mDBViewModel = new ViewModelProvider(DashboardFragment.this).get(DBViewModel.class);
        secondMDBViewModel = new ViewModelProvider(DashboardFragment.this).get(DBViewModel.class);

        //example shift
        //LiveData<List<ShiftInfo>> shifts = secondMDBViewModel.getAllShift();
        ///Date date = new Date();

        // live list observer
        mDBViewModel.getAllStaff().observe(getViewLifecycleOwner(), new Observer<List<StaffInfo>>() {
            @Override
            public void onChanged(@Nullable final List<StaffInfo> staffInfo) {
                //update the cached staff in the adaptor
                names.clear();
                phones.clear();
                emails.clear();
                employed.clear();
                trained_open.clear();
                trained_close.clear();
                upcomingShift.clear();
                for (int i = 0; i < staffInfo.size(); i++) {
                    if(!staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_past))){
                        int uid = staffInfo.get(i).uid;
                        names.add(staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname);
                        phones.add(staffInfo.get(i).phoneNum);
                        emails.add(staffInfo.get(i).email);
                        employed.add(staffInfo.get(i).employed);
                        trained_open.add(staffInfo.get(i).trained_open);
                        trained_close.add(staffInfo.get(i).trained_close);

                        // get and sort upcoming shifts
                        List<ShiftInfo> shifts = LocalDB.getDatabase(requireActivity().getApplicationContext()).getShiftDao().findShiftByName(staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname);
                        Date today = new Date(System.currentTimeMillis());
                        List<ShiftInfo> upcoming = new ArrayList<ShiftInfo>();
                        for (int j=0; j<shifts.size(); j++) {
                            if (today.before(shifts.get(j).date)) {
                                upcoming.add(shifts.get(j));
                            }
                        }
                        Collections.sort(upcoming, new SortByDate());

                        // set upcoming shifts
                        List <String> shiftDates = new ArrayList<String>();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - EEEE", Locale.CANADA);
                        for (int j=0; j<upcoming.size(); j++) {
                            shiftDates.add(sdf.format(upcoming.get(j).date) + " " + upcoming.get(j).shiftType);
                        }

                        upcomingShift.add(shiftDates);
                        employeeListView.setAdapter(itemsAdapter);
                    }}
            }
        });

        // Set filter for searching
        theFilter = (EditText) getView().findViewById((R.id.searchFilter));
        // Text listener for searchbar
        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                itemsAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable charSequence) {
            }
        });

        //displayEmployment = (CheckBox) getView().findViewById(R.id.checkEmployment);
        //displayEmployment.setOnClickListener(this);
    }

   /* @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.checkEmployment:
                if(displayEmployment.isChecked()){
                    itemsAdapter.getFilter().filter("t");
                }
                else{
                    itemsAdapter.getFilter();
                    break;
                }

        }
    }*/

    /*
        Function:       onDestroyView
        Parameters:     None
        Returns:        void
        Purpose:        - sets binding to null when view is destroyed

        Author:         Mitchell Driedger
        Log (2021-10-23) DRIEDGER:   - Created this function
        */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }


    static class SortByDate implements Comparator<ShiftInfo> {
        @Override
        public int compare(ShiftInfo a, ShiftInfo b) {
            return a.date.compareTo(b.date);
            //return a.datetime.compareTo(b.datetime);
        }
    }
}

