package com.android.physiora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static androidx.camera.core.CameraSelector.LENS_FACING_BACK;

public class CameraFragment extends Fragment {
    CameraSelector cameraSelector;
    int count = 0;
    Executor executor;
    public StorageReference mStorageRef, storagePositionRef;
    PreviewView previewView;
    AppCompatActivity activity;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    String userName, type;
    DatabaseReference databaseReference;
    Bundle bundle;
    Map<String, Object> InfMap;
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
            "android.permission.RECORD_AUDIO"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance("https://physiora-ae9f8-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference();
        View view = inflater.inflate(R.layout.fragment_session, container, false);
        previewView = view.findViewById(R.id.previewView);
        activity = (AppCompatActivity) view.getContext();
        userName = firebaseUser.getDisplayName();
        bundle = this.getArguments();
        if (bundle!=null){
            type = bundle.getString("type");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference.child("live").child("Users").child(userName).child("exercise")
                .child(type).child("afterInf").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Map<String, Object> afterInfMap = dataSnapshot.getValue(Map.class);
                if (afterInfMap != null) {
                    InfMap = afterInfMap;
                }
                else {
                    InfMap = new HashMap<>();
                    InfMap.put("maxAngle", "0.0");
                    InfMap.put("minAngle", "0.0");
                    InfMap.put("percentage", "0.0");
                }
                databaseReference.child("live").child("Users").child(userName).child("exercise")
                        .child(type).child("beforeInf").setValue(InfMap);
            }
        });
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS);
        }
    }
    public boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
            }
        }
    }
    private void startCamera() {
        executor = Executors.newSingleThreadExecutor();
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(activity);

        cameraProviderFuture.addListener((Runnable) () -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(activity));
    }
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build();
            cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(LENS_FACING_BACK)
                    .build();

            ImageAnalysis imageAnalysis =
                    new ImageAnalysis.Builder()
                            .setTargetResolution(new Size(640, 480))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build();
                imageAnalysis.setAnalyzer(executor, (ImageProxy image) -> {
                    //setAnalyzer to use Image instead of ImageProxy??
                    @SuppressLint("UnsafeExperimentalUsageError") Image img = image.getImage();
                    count++;
                    if (img != null) {
                        //converting to JPEG
                        byte[] jpegData = ImageUtils.imageToByteArray(img);
                        StringBuilder sb = new StringBuilder();
                        if (count % 10 == 0) {
                            sb.append(userName).append(type).append(count/10).append(".jpg");
                            storagePositionRef = mStorageRef.child(userName).child(type).child(sb.toString());
                            UploadTask uploadTask = storagePositionRef.putBytes(jpegData);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                }
                            });
                        }
                        image.close();
                }});
        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);
    }
}