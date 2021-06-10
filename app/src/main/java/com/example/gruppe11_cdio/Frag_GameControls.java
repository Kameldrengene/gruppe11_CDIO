package com.example.gruppe11_cdio;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag_GameControls extends Fragment implements View.OnClickListener {

    Button analyze, camera, edit;
    Controls callBack;
    int USER_IMAGE_CODE = 0;

    //Interface so this fragment can talk to parent activity
    public interface Controls{
        void goAnalyze();
        void goEdit();
        void updateImage(String path);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_game_controls, container, false);

        analyze = view.findViewById(R.id.button);
        camera = view.findViewById(R.id.button2);
        edit = view.findViewById(R.id.button3);

        analyze.setOnClickListener(this);
        camera.setOnClickListener(this);
        edit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v == analyze) {
            callBack.goAnalyze();
        }

        if(v == camera){
            Intent i = new Intent(getContext(), TakePhoto.class);
            startActivityForResult(i, USER_IMAGE_CODE);
        }

        if(v == edit) callBack.goEdit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == USER_IMAGE_CODE && resultCode == Activity.RESULT_OK){
            String path = data.getStringExtra("result");
            callBack.updateImage(path);
        }
    }
}
