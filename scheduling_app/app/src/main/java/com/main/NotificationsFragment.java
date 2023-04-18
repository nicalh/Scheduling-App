package com.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.main.Caller.LocalDB;
import com.main.Caller.ShiftInfo;
import com.main.Caller.StaffInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsFragment binding;
    ListView shiftListView;
    ListView WarnView;
    List<ShiftInfo> listshift;
    DBViewModel shiftMDBViewModel;
    List<String> nextWeek = new ArrayList<String>();
    Date currentDate = new Date();
    List <String> staffDurWeek = new ArrayList<String>();
    List<String> warnings = new ArrayList<>();
    ArrayAdapter<String> warningsAdapter;
    Date today = new Date(System.currentTimeMillis());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // set up adapters and lists
        shiftListView = (ListView) getView().findViewById(R.id.notif_upcoming_shifts_list);
        listshift = new ArrayList<>();
        ShiftAdaptor upcomingShifts = new ShiftAdaptor(getActivity(),listshift);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat warningFormatter= new SimpleDateFormat("dd/MM/yyyy EEEE");
        while (!currentDate.toString().contains("Sun")) { // set this date to the next saturday
            currentDate.setDate(currentDate.getDate() + 1);
        }
        for (int i = 0; i < 7; i++) { // add the coming up week to dates
            currentDate.setDate(currentDate.getDate() + 1);
            nextWeek.add(formatter.format(currentDate));
        }
        WarnView = (ListView) getView().findViewById(R.id.notif_warning_list);
        warningsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, warnings){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                //tv.setPadding(0, 0, 20, 0);
                tv.setPaddingRelative(10, 0, 5, 0);
                return view;
            }
        };

        // TODO - check if any day does not have shifts created for it (morning/afternoon shifts for weekday, weekend shift for weekend)
        // loop through nextWeek, if shift does not exist for day, give warnings (weekend/morning/afternoon)

        // upcoming listview
        shiftMDBViewModel = new ViewModelProvider(NotificationsFragment.this).get(DBViewModel.class);
        shiftMDBViewModel.getAllShift().observe(getViewLifecycleOwner(), new Observer<List<ShiftInfo>>() {
            @Override
            public void onChanged(@Nullable final List<ShiftInfo> shiftInfo) {
                Collections.sort(shiftInfo, new DashboardFragment.SortByDate());
                for (int i=0; i<shiftInfo.size(); i++) {
                    // only get shifts after today
                    if (today.before(shiftInfo.get(i).date)) {
                        // add shifts to upcoming shifts
                        listshift.add(shiftInfo.get(i));
                        shiftListView.setAdapter(upcomingShifts);

                        // check if a shift has something certain with it
                        if (shiftInfo.get(i).shiftType.equals("Morning")) {
                            if (shiftInfo.get(i).getStaffName1().equals("No selection") & shiftInfo.get(i).getStaffName2().equals("No selection") & shiftInfo.get(i).getStaffName3().equals("No selection")) {
                                warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - All shifts are not selected in the morning.");
                            }
                            else if ((shiftInfo.get(i).staff1.equals("No selection") && shiftInfo.get(i).staff2.equals("No selection")) |
                                    (shiftInfo.get(i).staff1.equals("No selection") && shiftInfo.get(i).staff3.equals("No selection")) |
                                    (shiftInfo.get(i).staff2.equals("No selection") && shiftInfo.get(i).staff3.equals("No selection"))){
                                    warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - Has less than 2 employees scheduled in the morning.");
                            }
                        }

                        else if (shiftInfo.get(i).shiftType.equals("Afternoon")) {
                            if (shiftInfo.get(i).getStaffName1().equals("No selection") & shiftInfo.get(i).getStaffName2().equals("No selection") & shiftInfo.get(i).getStaffName3().equals("No selection")) {
                                warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - All shifts are not selected in the afternoon.");
                            }
                            if ((shiftInfo.get(i).staff1.equals("No selection") && shiftInfo.get(i).staff2.equals("No selection")) |
                                    (shiftInfo.get(i).staff1.equals("No selection") && shiftInfo.get(i).staff3.equals("No selection")) |
                                    (shiftInfo.get(i).staff2.equals("No selection") && shiftInfo.get(i).staff3.equals("No selection"))){
                                    warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - Has less than 2 employees scheduled in the afternoon.");
                            }
                        }


                        // get the shifts next week
                        if (nextWeek.contains(formatter.format(shiftInfo.get(i).date))) {
                            // get the staff working this week
                            staffDurWeek.add(shiftInfo.get(i).staff1);
                            staffDurWeek.add(shiftInfo.get(i).staff2);
                            staffDurWeek.add(shiftInfo.get(i).staff3);

                            // find staff from DB
                            String[] staff1_name = shiftInfo.get(i).getStaffName1().split(" ");
                            StaffInfo staff1 = LocalDB.getDatabase(getActivity()).getStaffDao().findByName(staff1_name[0], staff1_name[1]);
                            String[] staff2_name = shiftInfo.get(i).getStaffName2().split(" ");
                            StaffInfo staff2 = LocalDB.getDatabase(getActivity()).getStaffDao().findByName(staff2_name[0], staff2_name[1]);
                            String[] staff3_name = shiftInfo.get(i).getStaffName3().split(" ");
                            StaffInfo staff3 = LocalDB.getDatabase(getActivity()).getStaffDao().findByName(staff3_name[0], staff3_name[1]);

                            boolean trainedToOpen = false;
                            boolean trainedToClose = false;

                            // see if shift has staff trained to open and close
                            if (staff1!=null) {
                                if (staff1.trained_open.equals("Trained")) {
                                    trainedToOpen = true;
                                }
                                if (staff1.trained_close.equals("Trained")) {
                                    trainedToClose = true;
                                }
                            }
                            if (staff2!=null) {
                                if (staff2.trained_open.equals("Trained")) {
                                    trainedToOpen = true;
                                }
                                if (staff2.trained_close.equals("Trained")) {
                                    trainedToClose = true;
                                }
                            }
                            if (staff3!=null) {
                                if (staff3.trained_open.equals("Trained")) {
                                    trainedToOpen = true;
                                }
                                if (staff3.trained_close.equals("Trained")) {
                                    trainedToClose = true;
                                }
                            }

                            if (shiftInfo.get(i).shiftType.equals("Morning")) {
                                if (!trainedToOpen) {
                                    warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - Nobody trained to open");
                                }
                            }
                            else if (shiftInfo.get(i).shiftType.equals("Afternoon")) {
                                if (!trainedToClose) {
                                    warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - Nobody trained to close");
                                }
                            }
                            else {
                                if (!trainedToOpen) {
                                    warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - Nobody trained to open");
                                }
                                if (!trainedToClose) {
                                    warnings.add(warningFormatter.format(shiftInfo.get(i).date) + " " + shiftInfo.get(i).shiftType + " - Nobody trained to close");
                                }
                            }
                        }
                    }
                }

            }
        });


        // warnings listview
        DBViewModel staffMDBViewModel = new ViewModelProvider(NotificationsFragment.this).get(DBViewModel.class);
        staffMDBViewModel.getAllStaff().observe(getViewLifecycleOwner(), new Observer<List<StaffInfo>>() {
            @Override
            public void onChanged(@Nullable final List<StaffInfo> staffInfo) {
                for( int i = 0; i < staffInfo.size(); i++){
                    WarnView.setAdapter(warningsAdapter);
                    if ((!staffDurWeek.contains(staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname)) & (staffInfo.get(i).employed.equals(getResources().getString(R.string.employment_stat_current)))) {
                        warnings.add(staffInfo.get(i).firstname + " " + staffInfo.get(i).lastname + " does not have a shift next week");
                    }
                }
            }
        });
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }


    static class SortByDate implements Comparator<ShiftInfo> {
        @Override
        public int compare(ShiftInfo a, ShiftInfo b) {
            return a.date.compareTo(b.date);
        }
    }


    public List<ShiftInfo> GetShiftsNextWeek() {
        List <ShiftInfo> upcoming = new ArrayList<>();
        Date today = new Date(System.currentTimeMillis());
        // find the next saturday, unless today
        while (!today.toString().contains("Sat")) {
            today.setDate(today.getDate() + 1);
        }
        // add all shifts in the next seven days to upcoming list
        for (int i = 0; i < 7; i++) {
            today.setDate(today.getDate() + 1);
            List<ShiftInfo> shifts = LocalDB.getDatabase(getActivity().getApplicationContext()).getShiftDao().findShiftByDate(today);
            for (int j = 0; j < shifts.size(); j++) {
                upcoming.add(shifts.get(j));
                System.out.println("Adding "+shifts.get(j).shiftType+" "+shifts.get(j).date.toString());
            }
        }

        return upcoming;
    }
}
