package pt.ubi.pdm.vivo.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.ubi.pdm.vivo.DataBase.DatabaseHelper;
import pt.ubi.pdm.vivo.session;

import static pt.ubi.pdm.vivo.session.API_host;

public class Receiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 13232;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        // wakes wifi
        session.holdWifiLock(context);

        new getNotification().execute();

        // wifi can go to sleep now
        session.releaseWifiLock();
    }

    public class getNotification extends AsyncTask<Bundle, Bundle, Bundle> {

        @Override
        protected Bundle doInBackground(Bundle... params) {

            String login_page_url = API_host + "/notifications";
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this POST request
                url = new URL(login_page_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                // Here we set that this is going to be a GET request
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();

                // Verifies if response was an error
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    stream = urlConnection.getErrorStream();
                } else
                    // No error, lets store the information returned
                    stream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                JSONObject jsonObj = new JSONObject(builder.toString());

                if (jsonObj.length() != 0) {
                    Bundle b = new Bundle();
                    b.putString("_id",jsonObj.getString("_id"));
                    b.putString("content", jsonObj.getString("content"));
                    return b;
                }

            } catch (IOException e) {
                e.printStackTrace();
                new getNotification().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bundle result) {

            // Verifies if request returned something
            if (result != null) {

                DatabaseHelper dbHelper = new DatabaseHelper(context);
                // Verifies if the notification is new
                if (!dbHelper.NotificationExists(result.getString("_id"))) {
                    // Notification is new, so, lets store it
                    dbHelper.storeNotificationDB(result.getString("_id"));
                    // And notify user
                    Intent i = new Intent(context, vivoService.class);
                    i.putExtra("content", result.getString("content"));
                    context.startService(i);
                }
            }
        }
    }
}
