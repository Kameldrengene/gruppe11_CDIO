package com.example.gruppe11_cdio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
Mikkel Danielsen, s183913
Frederik Koefoed, s195463
Muhammad Talha, s195475
Volkan Isik, s180103
Lasse Strunge, s19548
Mark Mortensen, s174881
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button spil, instruksOK, rulesOK, instrukser, rules;
    View instrukserView, rulesView;
    Dialog instrukserDialog, rulesDialog;
    TextView titleInstruks, bodyInstruks, titleRules, bodyRules;
    final int USER_IMAGE_CODE = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spil = findViewById(R.id.spil_button);
        instrukser = findViewById(R.id.regler);
        spil.setOnClickListener(this);
        instrukser.setOnClickListener(this);
        rules = findViewById(R.id.regler2);
        rules.setOnClickListener(this);

        instrukserDialog = new Dialog(this);
        instrukserView = getLayoutInflater().inflate(R.layout.popup_1button, null);
        titleInstruks = instrukserView.findViewById(R.id.alertTitle);
        bodyInstruks = instrukserView.findViewById(R.id.alertBody);
        instruksOK = instrukserView.findViewById(R.id.alertButton);
        instruksOK.setOnClickListener(this);
        titleInstruks.setText("Velkommen til 7-kabale!");
        bodyInstruks.setText("\nDenne app kan hjælpe med at analysere en 7-kabale. " +
                "\n\nDu kan starte appen når som helst " +
                "i et spil og tage et billede af kabalen. " +
                "Appen vil da forsøge at afkode billedet, " +
                "og du vil have mulighed for at analysere " +
                "kabalen for det bedste næste træk. " +
                "Efter hvert træk kan du så tage et nyt billede. " +
                "Hvis du vælger at stoppe med at " +
                "bruge hjælpen, kan du gå ud af " +
                "spillet ved at trykke på " +
                "tilbage knappen. Kabalen kan " +
                "startes igen når som helst. ");
        instrukserDialog.setContentView(instrukserView);

        rulesDialog = new Dialog(this);
        rulesView = getLayoutInflater().inflate(R.layout.popup_1button, null);
        titleRules = rulesView.findViewById(R.id.alertTitle);
        bodyRules = rulesView.findViewById(R.id.alertBody);
        rulesOK = rulesView.findViewById(R.id.alertButton);
        rulesOK.setOnClickListener(this);
        titleRules.setText("Regler for 7-kabale");
        bodyRules.setText("\n7-kabalen opstilles med 7 kolonner. I rækkefølge fra venstre" +
                " har kolonnerne 0, 1, 2, … , 6 lukkede kort og alle har 1 åbent" +
                " kort oven på de lukkede. Resten af kortene placeres øverst til" +
                " venstre og spillet starter. Når der bliver bedt om at trække kort," +
                " skal kun et enkelt kort fra bunken vendes og lægges til højre for" +
                " bunken. Hvis der ikke er flere kort I bunken, vendes bunken af åbne" +
                " kort om og dette er den nye bunke.");
        rulesDialog.setContentView(rulesView);

    }

    @Override
    public void onClick(View v) {
        if(v==spil){

            //Ensure permissions before continuing
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                }, REQUEST_CAMERA_PERMISSION);
            } else {
                //Go ahead and start
                Intent i = new Intent(this, TakePhoto.class);
                startActivityForResult(i, USER_IMAGE_CODE);
            }
        }

        if(v == instrukser) instrukserDialog.show();
        if(v == instruksOK) instrukserDialog.dismiss();
        if(v == rules) rulesDialog.show();
        if(v == rulesOK) rulesDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED
            || grantResults[1] != PackageManager.PERMISSION_GRANTED
            || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Mangler tilladelser", Toast.LENGTH_LONG).show();
            } else {
                //Go ahead and start
                Intent i = new Intent(this, TakePhoto.class);
                startActivityForResult(i, USER_IMAGE_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == USER_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra("result");
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("photo", path);
            startActivity(intent);
            GameActivity.fromMain = true;
        }
    }
}
