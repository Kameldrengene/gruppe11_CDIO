package com.example.gruppe11_cdio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*
Mikkel Danielsen, s183913
Frederik Koefoed, s195463
Muhammad Talha, s195475
Volkan Isik, s180103
Lasse Strunge, s19548
Mark Mortensen, s174881
 */

public class Frag_GameAnalyze extends Fragment implements View.OnClickListener {

    Button takeImage, back;
    Controls callBack;
    TextView message;
    int USER_IMAGE_CODE = 0;

    //Interface so this fragment can talk to parent activity
    public interface Controls{
        void updateImage(String path);
        void goToControls();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            //We need to be able to call parent to change view when a button is pressed
            callBack = (Controls) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Controls");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_game_analyser, container, false);

        takeImage = view.findViewById(R.id.button2);
        takeImage.setOnClickListener(this);

        back = view.findViewById(R.id.button3);
        back.setOnClickListener(this);

        message = view.findViewById(R.id.textView6);
        message.setText(GameActivity.nextMove);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == takeImage){
            Intent i = new Intent(getContext(), TakePhoto.class);
            startActivityForResult(i, USER_IMAGE_CODE);
        }

        if(v == back)
            callBack.goToControls();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == USER_IMAGE_CODE && resultCode == Activity.RESULT_OK){
            String path = data.getStringExtra("result");
            callBack.updateImage(path);
            callBack.goToControls();
        }
    }
}
