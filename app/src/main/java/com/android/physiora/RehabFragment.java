package com.android.physiora;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RehabFragment extends Fragment {
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_rehab, container, false);
        listView = view.findViewById(R.id.exercise_list_view);
        LinkedHashMap<String, String> exerciseNumber = new LinkedHashMap<>();
        exerciseNumber.put("Arms", "5 exercises");
        exerciseNumber.put("Balance", "4 exercises");
        exerciseNumber.put("Core", "1 exercise");
        exerciseNumber.put("Hands", "5 exercises");
        exerciseNumber.put("Legs", "6 exercises");
        exerciseNumber.put("Shoulders", "4 exercises");
        List<LinkedHashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems, R.layout.exercise_list, new String[]{"First Line", "Second Line"}, new int[]{R.id.exercise_name, R.id.exercise_number});
        Iterator iterator = exerciseNumber.entrySet().iterator();
        while (iterator.hasNext()) {
            LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
            Map.Entry pair = (Map.Entry) iterator.next();
            resultMap.put(("First Line"), pair.getKey().toString());
            resultMap.put(("Second Line"), pair.getValue().toString());
            listItems.add(resultMap);
        }
        listView.setAdapter(adapter);
        listView.isClickable();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        bundle.putString("type", "arm");
                        break;
                    case 1:
                        bundle.putString("type", "balance");
                        break;
                    case 2:
                        bundle.putString("type", "core");
                        break;
                    case 3:
                        bundle.putString("type", "hand");
                        break;
                    case 4:
                        bundle.putString("type", "leg");
                        break;
                    case 5:
                        bundle.putString("type", "shoulder");
                        break;
                }
                bundle.putString("source", "RehabFragment");
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment sessionFragment = new SessionFragment();
                sessionFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, sessionFragment).commit();
            }
        });
        return view;
    }
}