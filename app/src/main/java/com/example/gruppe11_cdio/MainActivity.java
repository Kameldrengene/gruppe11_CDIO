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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button spil, reglerOkButton,regler;
    View reglerView;
    Dialog alertDialog;
    TextView title,body;
    final int USER_IMAGE_CODE = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

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

            //Ensure permission before continuing
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CAMERA_PERMISSION);
            } else {
                //Go a head and start
                Intent i = new Intent(this, TakePhoto.class);
                startActivityForResult(i, USER_IMAGE_CODE);
            }
        }

        if(v==regler) alertDialog.show();
        if(v== reglerOkButton) alertDialog.dismiss();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Mangler tilladelser", Toast.LENGTH_LONG).show();
            } else {
                //Go a head and start
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
