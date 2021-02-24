package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gruppe11_cdio.Dialog.KabaleDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button spil,alertButton,regler;
    Dialog alertDialog;
    KabaleDialog kabaleDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spil = findViewById(R.id.spil_button);
        regler = findViewById(R.id.regler);

        spil.setOnClickListener(this);
        regler.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v==spil){
            Intent intent = new Intent(this,GameActivity.class);
            startActivity(intent);
        }

        if(v==regler){

            kabaleDialog = new KabaleDialog(this,1);
            kabaleDialog.setTitle("Velkommen til 7-kabale");
            kabaleDialog.setBody("" +
                    "Denne app bruges til at hjælpe\n" +
                    "med 7-kabale.\n" +
                    "Du kan starte appen i hvilken\n" +
                    "som helst tidspunkt i et spil.\n" +
                    "Du tager så et billede ved at\n" +
                    "trykke på knappen “Tag billede”\n" +
                    "og appen lærer din situation at\n" +
                    "kende.\n" +
                    "Herefter, tager du et nyt billede\n" +
                    "efter hvert træk du tager.\n" +
                    "Hvis du vælger at stoppe med at\n" +
                    "bruge hjælpen, skal du gå ud af\n" +
                    "spillet ved at trykke på\n" +
                    "tilbage-pilen. Kabalen kan\n" +
                    "startes igen, hvornår som helst.");

            kabaleDialog.show();
        }

        if(v==kabaleDialog.getbutton1()){
            kabaleDialog.dismiss();
        }
    }
}
