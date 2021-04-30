package com.android.physiora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

    public class MyPlanFragment extends Fragment {
        ListView planListView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.my_plan_activated, container, false);
            planListView = view.findViewById(R.id.plan_list_view);
            LinkedHashMap<String, String> exerciseNumber = new LinkedHashMap<>();
            exerciseNumber.put("Run Through", "");
            exerciseNumber.put("Arm & Shoulder Exercise 1", "10 repetitions");
            exerciseNumber.put("Arm & Shoulder Exercise 2", "2 repetitions");
            List<LinkedHashMap<String, String>> listItems = new ArrayList<>();
            SimpleAdapter planAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.exercise_list, new String[]{"First Line", "Second Line"}, new int[]{R.id.exercise_name, R.id.exercise_number});
            Iterator iterator = exerciseNumber.entrySet().iterator();
            while (iterator.hasNext()) {
                LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
                Map.Entry pair = (Map.Entry) iterator.next();
                resultMap.put(("First Line"), pair.getKey().toString());
                resultMap.put(("Second Line"), pair.getValue().toString());
                listItems.add(resultMap);
            }
            planListView.setAdapter(planAdapter);
            planListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    switch (position) {
                        case 0:
                            bundle.putString("type", "runthrough");
                            break;
                        case 1:
                            bundle.putString("type", "armANDshoulder");
                            break;
                        case 2:
                            bundle.putString("type", "armANDshoulder2");
                            break;
                    }
                    bundle.putString("source", "MyPlanFragment");
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment sessionFragment = new SessionFragment();
                    sessionFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, sessionFragment).commit();
                }
            });
            return view;

        }
    }