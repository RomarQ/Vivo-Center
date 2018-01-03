package pt.ubi.pdm.vivo;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import pt.ubi.pdm.vivo.Activities.sliderRoutes;
import pt.ubi.pdm.vivo.DataBase.DatabaseHelper;
import pt.ubi.pdm.vivo.EducativeOffers.educativeOffers;
import pt.ubi.pdm.vivo.SimpleAtivities.Center;
import pt.ubi.pdm.vivo.Users.Users;
import pt.ubi.pdm.vivo.Visit.*;
import pt.ubi.pdm.vivo.Service.Receiver;

public class Home extends AppCompatActivity {

    private TextView TV_username;

    private Button Btn_settings;
    private Button Btn_exit;

    private CardView CV_Visit, CV_Centro, CV_Activities, CV_EducativeOffers;
    private View V_divider;

    Dialog admin_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TV_username = findViewById(R.id.TV_username);
        Btn_exit = findViewById(R.id.Btn_exit);
        CV_Visit = findViewById(R.id.CV_Visit);
        CV_Centro = findViewById(R.id.CV_centro);
        CV_Activities = findViewById(R.id.CV_ativities);
        CV_EducativeOffers = findViewById(R.id.CV_Education);

        TV_username.setText(session.profile.getUsername());

        // Verifies if user is ADMIN to enable his features
        if (session.profile.getIsAdmin()) {
            Btn_settings = findViewById(R.id.Btn_settings);
            V_divider = findViewById(R.id.V_divider);

            // Every admin item that was GONE is not set as VISIBLE
            Btn_settings.setEnabled(true);
            Btn_settings.setVisibility(View.VISIBLE);

            V_divider.setVisibility(View.VISIBLE);

            // Prepare dialog that user will have access when presses the settings button
            admin_dialog = new Dialog(this);
            Btn_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp();
                }
            });
        }

        // Logout and clear all Stored Information about LoggedIn User
        // Notifications ID's will not be deleted since is still the same phone
        Btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Destroys alarm that was set to receive notification
                cancelAlarm();
                // Clears LoggedIn user data
                new DatabaseHelper(Home.this).removeDataFromDB();
                // Closes last Activity
                finish();
            }
        });

        // Intent that sends user to Visit
        CV_Visit.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View oView) {
                startActivity(new Intent(Home.this, Visits.class));
            }
        });

        // Intent that sends user to Centro page
        CV_Centro.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View oView) {
                startActivity(new Intent(Home.this, Center.class));
            }
        });


        // Intent that sends user to activities
        CV_Activities.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View oView) {
                startActivity(new Intent(Home.this, sliderRoutes.class));
            }
        });

        // Intent that sends user to Educative offers
        CV_EducativeOffers.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View oView) {
                startActivity(new Intent(Home.this, educativeOffers.class));
            }
        });

    }

    // ADMIN ONLY [ ADMIN FEATURES ]
    private void showPopUp() {

        admin_dialog.setContentView(R.layout.activity_admin_pop_up);

        TextView Btn_close = admin_dialog.findViewById(R.id.Btn_close);
        Button Btn_newNotification = admin_dialog.findViewById(R.id.Btn_newNotification);
        Button Btn_showVisits = admin_dialog.findViewById(R.id.Btn_showVisits);
        Button Btn_users = admin_dialog.findViewById(R.id.Btn_users);
        Button Btn_showVisitsPerPeriod = admin_dialog.findViewById(R.id.Btn_showVisitsPerPeriod);

        Btn_showVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, VisitsLog.class));
            }
        });

        Btn_newNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, newNotification.class));
            }
        });

        Btn_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(Home.this, Users.class));
            }
        });

        Btn_showVisitsPerPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(Home.this, showVisitsPerPeriod.class));
            }
        });

        Btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin_dialog.dismiss();
            }
        });
        admin_dialog.show();
    }

    // As mentioned before, here we destroy the alarm that was set on LoadUser.class
    public void cancelAlarm() {
        Intent i = new Intent(getApplicationContext(), Receiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, Receiver.REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
