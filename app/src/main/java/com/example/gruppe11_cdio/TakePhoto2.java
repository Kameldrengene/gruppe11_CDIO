package com.example.gruppe11_cdio;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.view.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto2 extends AppCompatActivity implements View.OnClickListener {

    Camera camera;
    Button capture;
    FrameLayout frame;
    ShowCamera showCamera;
    String currentPhotoPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo2);
        frame = findViewById(R.id.frameLayout);
        capture = findViewById(R.id.capture);
        capture.setOnClickListener(this);

        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frame.addView(showCamera);
    }

    @Override
    public void onClick(View v) {
        if(camera != null) camera.takePicture(null, null, mPictureCallback);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File outFile = null;
            try {
                outFile = createImageFile();
                if(outFile != null) {

                    //Save picture
                    FileOutputStream fos = new FileOutputStream(outFile);
                    fos.write(data);
                    fos.close();
                    finishThis();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void finishThis() {
        Intent returnIntent = new Intent();
        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        returnIntent.putExtra("result", currentPhotoPath);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
