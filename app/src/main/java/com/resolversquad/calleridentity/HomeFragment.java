package com.resolversquad.calleridentity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private View view;
    private CallLogAdapter callLogAdapter;
    private RecyclerView recyclerView;
    private ArrayList<CallLogModel> callLogModels;


    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.call_log_recyclerview);
        callLogModels = new ArrayList<>();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       boolean per= check();
        System.out.println("+++++++++  "+per);
       if (per){
           getContacts();
       }

    }

    private boolean check() {


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
             return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG)) {
                    Toast.makeText(getActivity(), "Read call Log permission is required to function app correctly", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CALL_LOG},
                            1);
                    return true;

                }

            }
            return false;
        }


    }

    private void getContacts() {
        String[] projection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };

        Cursor managedCursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        while (managedCursor.moveToNext()) {
            String name = managedCursor.getString(0); //name
            String number = managedCursor.getString(1); // number
            String type = managedCursor.getString(2); // type
            String date = managedCursor.getString(3); // time
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String dateString = formatter.format(new Date(Long.parseLong(date)));
            String duration = managedCursor.getString(4); // duration
            if (name == null) {
                callLogModels.add(new CallLogModel(
                        "" + name,
                        "" + number,
                        "" + number,
                        "" + dateString
                ));
            } else {
                callLogModels.add(new CallLogModel(
                        "" + name,
                        "" + name,
                        "" + number,
                        "" + dateString
                ));
            }
            /*String dir = null;
            int dircode = Integer.parseInt(type);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }*/
        }
        callLogAdapter = new CallLogAdapter(getContext(), callLogModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(callLogAdapter);
        managedCursor.close();
    }


    @Override
    public void onPause() {
        super.onPause();
        callLogModels.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        callLogModels.clear();
    }
}