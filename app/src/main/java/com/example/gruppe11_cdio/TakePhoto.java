package com.example.gruppe11_cdio;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

//Used with "startActivityForResult()"
public class TakePhoto extends AppCompatActivity {

    int CAMERA_REQUEST_CODE = 0;
    int CAMERA_CODE = 1;
    static boolean inProgress = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takeUserPhoto();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void takeUserPhoto(){
        //Check permissions. If fine else go ahead and take photo, otherwise ask for permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                Toast.makeText(this, "Mangler kameratilladelse", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, CAMERA_CODE);
            inProgress = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                takeUserPhoto();
            else{
                Toast.makeText(this, "Mangler kameratilladelse", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK){
            if(data == null || data.getExtras() == null || data.getExtras().get("data") == null) return;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = Functionality.bitmapToUri(this, bitmap);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", uri.toString());
            setResult(Activity.RESULT_OK, returnIntent);
        }

        inProgress = false;
        finish();
    }

}
