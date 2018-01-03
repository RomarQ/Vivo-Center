package pt.ubi.pdm.vivo;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static pt.ubi.pdm.vivo.session.API_host;

public class newNotification extends AppCompatActivity {

    private Button Btn_newNotification;
    private EditText ET_notificationContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);

        Btn_newNotification = findViewById(R.id.Btn_newNotification);
        ET_notificationContent = findViewById(R.id.ET_notificationContent);

        Btn_newNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new newNotificationTask().execute(ET_notificationContent.getText().toString());
            }
        });

    }

    private class newNotificationTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String signup_page_url = API_host + "/newnotification";
            String content = params[0];
            String token = session.profile.getToken();
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;


            try {
                // Here we set for which url we gonna send this POST request
                url = new URL(signup_page_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                // Here we set that this is going to be a POST request
                urlConnection.setRequestMethod("POST");
                // Since this POST send a request body setDoOutput needs to be true
                urlConnection.setDoOutput(true);

                // Prepare param content to be set on body of POST request
                String data = URLEncoder.encode("content", "UTF-8")
                        + "=" + URLEncoder.encode(content, "UTF-8");


                urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                // Verifies if response was an error
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    return "Ocorreu algum problema!";
                }

                finish();
                return "Notificação enviada!";

            } catch (IOException e) {
                e.printStackTrace();
                return "Problema de conexão, por favor verifique a sua ligação à internet";
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        protected void onPostExecute(String response) {
            if (response == null)
                return;

            Toast.makeText(newNotification.this, response, Toast.LENGTH_LONG).show();
        }
    }
}
