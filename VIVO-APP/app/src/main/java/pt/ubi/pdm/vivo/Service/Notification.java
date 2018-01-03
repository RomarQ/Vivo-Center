package pt.ubi.pdm.vivo.Service;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pt.ubi.pdm.vivo.R;

public class Notification extends AppCompatActivity {

    private String notificationContent;
    private TextView TV_notificationContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(0);

        notificationContent = getIntent().getStringExtra("content");
        TV_notificationContent = findViewById(R.id.TV_notificationContent);

        if (notificationContent != null) {
            TV_notificationContent.setText(notificationContent);
        }else {
            finish();
        }

    }
}
