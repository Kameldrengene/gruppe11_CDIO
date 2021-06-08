package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button spil, reglerOkButton,regler;
    View reglerView;
    Dialog alertDialog;
    TextView title,body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spil = findViewById(R.id.spil_button);
        regler = findViewById(R.id.regler);
        spil.setOnClickListener(this);
        regler.setOnClickListener(this);

        alertDialog = new Dialog(this);
        reglerView = getLayoutInflater().inflate(R.layout.popup1button, null);
        title=reglerView.findViewById(R.id.alertTitle);
        body=reglerView.findViewById(R.id.alertBody);
        reglerOkButton = reglerView.findViewById(R.id.alertButton);
        reglerOkButton.setOnClickListener(this);
        title.setText("Velkommen til 7-kabale");
        body.setText("\nDenne app bruges til at hjælpe med 7-kabale. " +
                "\n\nDu kan starte appen i hvilken " +
                "som helst tidspunkt i et spil. " +
                "Du tager så et billede ved at " +
                "trykke på knappen “Tag billede” " +
                "og appen lærer din situation at " +
                "kende. " +
                "Herefter, tager du et nyt billede " +
                "efter hvert træk du tager. " +
                "Hvis du vælger at stoppe med at " +
                "bruge hjælpen, skal du gå ud af " +
                "spillet ved at trykke på " +
                "tilbage-pilen. Kabalen kan " +
                "startes igen, hvornår som helst. ");
        alertDialog.setContentView(reglerView);

    }

    @Override
    public void onClick(View v) {
        if(v==spil){
            Intent intent = new Intent(this,GameActivity.class);
            startActivity(intent);
        }

        if(v==regler) alertDialog.show();
        if(v== reglerOkButton) alertDialog.dismiss();

    }
}
