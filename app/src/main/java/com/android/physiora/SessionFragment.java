package com.android.physiora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

public class SessionFragment extends Fragment {
    String source, exercise;
    TextView textThree;
    VideoView videoView;
    Uri uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        exercise = getArguments().getString("type");
        source = getArguments().getString("source");
        StringBuilder sb = new StringBuilder();

        final View view = inflater.inflate(R.layout.my_plan, container, false);
        videoView = view.findViewById(R.id.videoView);
        if (source == "RehabFragment") {
            sb.append("Start playing your ").append(exercise).append(" video on your TV");
            textThree = view.findViewById(R.id.text3);
            textThree.setText(sb);
            switch (exercise) {
                case "arm":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.arm);
                    break;
                case "balance":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.balance);
                    break;
                case "core":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.core);
                    break;
                case "hand":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.hand);
                    break;
                case "leg":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.leg);
                    break;
                case "shoulder":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.shoulder);
                    break;
            }

        } else if (source == "MyPlanFragment") {
            switch (exercise) {
                case "runthrough":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.runthrough);
                    break;
                case "armANDshoulder":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.arm);
                  //  uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.armANDshoulder);
                    break;
                case "armANDshoulder2":
                    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.arm);
                //    uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.armANDshoulder2);
                    break;
            }
        } else {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.arm);
            sb.append("Start playing your ").append(exercise).append(" video on your TV");
            textThree = view.findViewById(R.id.text3);
            textThree.setText(sb);

        }
        videoView.setVideoURI(uri);
        videoView.start();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("type", exercise);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment cameraFragment = new CameraFragment();
                cameraFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, cameraFragment).commit();

            }
        }, 15000);
    }
}
