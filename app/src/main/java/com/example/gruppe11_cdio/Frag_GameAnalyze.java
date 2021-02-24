package com.example.gruppe11_cdio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

public class Frag_GameAnalyze extends Fragment implements View.OnClickListener {

    Button takeImage;
    int CAMERA_REQUEST_CODE = 0;
    int CAMERA_CODE = 1;

    Controls callBack;

    //Interface so this fragment can talk to parent activity
    public interface Controls{
        void updateImage(Uri uri);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            //We need to be able to call parent to change view when a button is pressed
            callBack = (Controls) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement topbar");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_game_analyser, container, false);

        takeImage = view.findViewById(R.id.button2);
        takeImage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == takeImage){
            takeUserPhoto();
        }
    }

    private void takeUserPhoto(){
        //Check permissions. If fine else go ahead and take photo, otherwise ask for permission
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA))
                Toast.makeText(getContext(), "Mangler kameratilladelse", Toast.LENGTH_SHORT).show();
            else
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                takeUserPhoto();
            else
                Toast.makeText(getContext(), "Mangler kameratilladelse", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_CODE){
            if(data == null || data.getExtras() == null || data.getExtras().get("data") == null) return;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = Functionality.bitmapToUri(getContext(), bitmap);
            callBack.updateImage(uri);
        }
    }


}
