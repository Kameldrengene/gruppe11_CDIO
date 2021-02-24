package com.example.gruppe11_cdio;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag_GameEdit extends Fragment implements View.OnClickListener {

    Button save;
    Controls callBack;

    //Interface so this fragment can talk to parent activity
    public interface Controls{
        void goToControls();
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
        View view = inflater.inflate(R.layout.frag_game_edit, container, false);

        save = view.findViewById(R.id.button);
        save.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        if(v == save){
            callBack.goToControls();
        }

    }

}
