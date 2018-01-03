package pt.ubi.pdm.vivo;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import pt.ubi.pdm.vivo.DataBase.DatabaseHelper;
import pt.ubi.pdm.vivo.Service.*;

import static java.lang.Thread.sleep;
import static pt.ubi.pdm.vivo.session.API_host;

public class LoadUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_user);
        findViewById(R.id.PB_load).setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();

        session.profile.setUser_id(extras.get("id").toString());
        session.profile.setToken(extras.get("token").toString());

        new LoadUser.AuthTask().execute(session.profile.getToken());
    }


    // Setup a recurring alarm every 8 hours
    public void scheduleAlarm() {

        // Construct an intent that will execute the AlarmReceiver
        Intent i = new Intent(getApplicationContext(), Receiver.class);

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, Receiver.REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        // setRepeating() to specify a precise 8 hour interval
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+15000,3600000 * 8 , pIntent);
    }

    private class AuthTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String login_page_url = API_host + "/user";
            String token = params[0];
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this GET request
                url = new URL(login_page_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                // Here we set that this is going to be a GET request
                urlConnection.setRequestMethod("GET");

                urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                urlConnection.connect();

                // Verifies if response was an error
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    stream = urlConnection.getErrorStream();
                }
                else
                    // No error, lets store the information returned
                    stream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                JSONObject jsonObj = new JSONObject(builder.toString());

                // Stores information about loggedIn User
                session.profile.setUsername(jsonObj.getString("username"));
                session.profile.setEmail(jsonObj.getJSONArray("emails").getJSONObject(0).getString("address"));
                session.profile.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sss").parse(jsonObj.getString("createdAt")));

                // Verifies if user is a ADMIN and stores that information
                if(jsonObj.has("roles"))
                    if (jsonObj.getJSONArray("roles").getString(0).equals("admin"))
                        session.profile.setIsAdmin(true);
                    else
                        session.profile.setIsAdmin(false);
                else
                    session.profile.setIsAdmin(false);

                // Sends Profile Object with user information to be stored on database
                // Doing this, App will be able to recognize this user as authenticated
                // while he doesn't logout
                new DatabaseHelper(LoadUser.this).storeProfileDB(session.profile);

                // Sets Up an alarm service to run in background, that will call a broadcastReceiver
                // every 8 hours from this point forward to detect new Notifications from vivo Server
                scheduleAlarm();

                // Just to be a bit fancy, lets extend the duration of loader for 1 more second
                // before sending user to Home page
                sleep(1000);
                startActivity(new Intent(LoadUser.this, Home.class));
                finish();

            } catch (IOException e) {
                e.printStackTrace();
                // If something bad happens during what we did before, send user back to login
                startActivity(new Intent(LoadUser.this, Login.class));
                finish();
                return "Problema de conexão, por favor verifique a sua ligação à internet";
            } catch (JSONException | ParseException | InterruptedException e) {
                e.printStackTrace();
                // If something bad happens during what we did before, send user back to login
                startActivity(new Intent(LoadUser.this, Login.class));
                finish();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        protected void onPostExecute(String response) {
            if (response == null)
                return;

            Toast.makeText(LoadUser.this, response, Toast.LENGTH_LONG).show();
        }
    }
}
