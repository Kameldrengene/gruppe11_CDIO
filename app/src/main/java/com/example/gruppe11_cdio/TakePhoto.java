package com.example.gruppe11_cdio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
SOURCE : https://github.com/eddydn/AndroidCamera2API/blob/master/app/src/main/java/edmt/dev/androidcamera2api/MainActivity.java
 */

public class TakePhoto extends AppCompatActivity implements View.OnClickListener {

    String currentPhotoPath = "";
    ImageView capture;
    CameraView camera;
    SharedPreferences prefs;
    Dialog dialog;
    View view, info;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo2);

        prefs = getSharedPreferences("com.example.gruppe11_cdio", MODE_PRIVATE);

        capture = findViewById(R.id.capture);
        capture.setOnClickListener(this);

        camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);
        camera.setUseDeviceOrientation(false);
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                //A Picture was taken
                File file = createImageFile();
                result.toFile(file, new FileCallback() {
                    @Override
                    public void onFileReady(@Nullable File file) {
                        finishThis();
                    }
                });
            }
        });

        info = findViewById(R.id.info);
        info.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Show first time message
        if (prefs.getBoolean("firstrun", true)) {
            showDialog();
            prefs.edit().putBoolean("firstrun", false).commit();
        }

    }

    private void showDialog() {
        dialog = new Dialog(this);
        view = getLayoutInflater().inflate(R.layout.popup_camera, null);
        ok = view.findViewById(R.id.alertButton);
        ok.setOnClickListener(this);
        dialog.setContentView(view);
        dialog.show();
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    private void finishThis() {
        Intent returnIntent = new Intent();
        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        returnIntent.putExtra("result", currentPhotoPath);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v == ok) dialog.dismiss();
        if(v == info) showDialog();
        if(v == capture) camera.takePictureSnapshot();
    }
}
