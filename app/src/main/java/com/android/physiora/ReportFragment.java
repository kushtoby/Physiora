package com.android.physiora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportFragment extends Fragment {
TextView usernameView, armChange, legChange, balanceChange, coreChange, shoulderChange, handChange;
String userName;
FirebaseUser firebaseUser;
FirebaseDatabase firebaseDatabase;
DatabaseReference ref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_report, container, false);
            armChange = view.findViewById(R.id.tvArmChange);
            legChange = view.findViewById(R.id.tvLegChange);
            balanceChange = view.findViewById(R.id.tvBalanceChange);
            coreChange = view.findViewById(R.id.tvCoreChange);
            shoulderChange = view.findViewById(R.id.tvShoulderChange);
            handChange = view.findViewById(R.id.tvHandChange);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            userName = firebaseUser.getDisplayName();
            usernameView = view.findViewById(R.id.userNameTV);
            usernameView.setText(userName);
            firebaseDatabase = FirebaseDatabase.getInstance();
            ref = firebaseDatabase.getReference();
            ref.child("live").child(userName).child("exercise").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   Map <String, String> armMap = (Map) snapshot.child("arm").child("afterInf").getValue();
                   Map<String, String> balanceMap = (Map) snapshot.child("balance").child("afterInf").getValue();
                   Map <String, String> coreMap = (Map) snapshot.child("core").child("afterInf").getValue();
                   Map<String, String> handMap = (Map) snapshot.child("hand").child("afterInf").getValue();
                   Map <String, String> legMap = (Map) snapshot.child("leg").child("afterInf").getValue();
                   Map<String, String> shoulderMap = (Map) snapshot.child("shoulder").child("afterInf").getValue();
                   if (armMap != null) {
                       String armPercentage = (String) armMap.get("percentage");
                       armChange.setText(armPercentage);
                   }
                    if (balanceMap != null) {
                        String legPercentage = (String) legMap.get("percentage");
                        legChange.setText(legPercentage);
                    }
                    if (coreMap != null) {
                        String corePercentage = (String) coreMap.get("percentage");
                        coreChange.setText(corePercentage);
                    }
                    if (handMap != null) {
                        String handPercentage = (String) handMap.get("percentage");
                        handChange.setText(handPercentage);
                    }
                    if (legMap != null) {
                        String legPercentage = (String) legMap.get("percentage");
                        legChange.setText(legPercentage);
                    }
                    if (shoulderMap != null) {
                        String shoulderPercentage = (String) shoulderMap.get("percentage");
                        shoulderChange.setText(shoulderPercentage);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return view;
        }
}