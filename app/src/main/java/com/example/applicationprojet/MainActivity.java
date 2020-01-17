package com.example.applicationprojet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private TextView Tv_Localisation;
    private Button Btn_ActiverLocalisation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String num;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Btn_ActiverLocalisation = (Button) findViewById(R.id.Btn_ActiverLocalisation);
        Tv_Localisation = (TextView) findViewById(R.id.Tv_Localisation);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //mettre en oeuvre une fin d'instalation de l'pplicatif en cas de licenciment.

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //here i put the condition if he's on he zone of location so the textView change into "Vous etes au travaille"
                //49.823934 && location.getLatitude() > 49.823518 && location.getLongitude() < 3.269333 && location.getLongitude() > 3.268617)
                if (location.getLatitude() < 49.823934 && location.getLatitude() > 49.823518 && location.getLongitude() < 3.269333 && location.getLongitude() > 3.268617) {
                    //Si il est dans la zone de son entreprise alors on envoie un message
                    num = "0688852635";
                    msg = "Arrivé au travail validé";
                    //Si le numéro est supérieur à 4 caractères et que le message n'est pas vide on lance la procédure d'envoi
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(num, null, msg, null, null);
                    finish();
                    System.exit(0);
                    //lancer une autre application de timer pour verifier que la presence au travail ne dure pas plus de 11Heures
                    //mettre un sleep
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;
            } else {
                configureButton();
            }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }


    //Quand on appuis sur le bouton ca lance la mise a jour de la localisation
    private void configureButton() {
        Btn_ActiverLocalisation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                //mettre plus de temps d'intervalle.
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

            }
        });

    }

}

