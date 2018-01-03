package pt.ubi.pdm.vivo.Users;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.session;

import static pt.ubi.pdm.vivo.session.API_host;

public class Users extends AppCompatActivity {

    RecyclerView RV_users;
    RecyclerView.Adapter RV_adpter_users;
    RecyclerView.LayoutManager RV_layoutManager_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Clear old data since we are going to make a new request
        session.users.clear();

        // Makes a new request to updates session.users
        new getUsersTask().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Same as onCreate, this is need to update the Information in case admin decides to
        // Delete or Edit a user
        // When those Activities finish() onRestart is called

        session.users.clear();
        new getUsersTask().execute();
    }

    private class getUsersTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String login_page_url = API_host + "/users";
            String token = session.profile.getToken();
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
                JSONArray jsonArr = new JSONArray(builder.toString());

                if (jsonArr.length() == 0 )
                    return null
                            ;
                for (int i=0; i < jsonArr.length(); i++)  {
                    ArrayList<Date> LoginLog = new ArrayList<>();

                    if (jsonArr.getJSONObject(i).has("services"))
                        if (jsonArr.getJSONObject(i)
                                .getJSONObject("services").has("resume"))
                            if (jsonArr.getJSONObject(i).getJSONObject("services")
                                    .getJSONObject("resume").has("loginTokens")) {

                                JSONArray lLog = jsonArr.getJSONObject(i).getJSONObject("services")
                                        .getJSONObject("resume").getJSONArray("loginTokens");

                                for (int j = 0; j < lLog.length(); j++)
                                    LoginLog.add(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm")
                                            .parse(lLog.getJSONObject(j).getString("when")));
                            }

                    boolean isAdmin = false;

                    if(jsonArr.getJSONObject(i).has("roles"))
                        if (jsonArr.getJSONObject(i).getJSONArray("roles").getString(0).equals("admin"))
                            isAdmin = true;

                    session.users.add(
                        new User(
                            jsonArr.getJSONObject(i).getString("_id"),
                            jsonArr.getJSONObject(i).getString("username"),
                            jsonArr.getJSONObject(i).getJSONArray("emails").getJSONObject(0).getString("address"),
                            new SimpleDateFormat("yyyy-MM-dd'T'hh:mm")
                                    .parse(jsonArr.getJSONObject(i).getString("createdAt")),
                            LoginLog,
                            isAdmin
                        )
                    );
                }


            } catch (IOException e) {
                e.printStackTrace();
                return "Problema de conexão, por favor verifique a sua ligação à internet";
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        protected void onPostExecute(String response) {

            if (response != null)
                Toast.makeText(Users.this, response, Toast.LENGTH_LONG).show();

            // Propagates the information gotten before to usersAdapter

            RV_users = findViewById(R.id.RV_users);
            RV_users.setHasFixedSize(true);
            RV_layoutManager_users = new LinearLayoutManager(Users.this);
            RV_adpter_users = new usersAdapter();

            RV_users.setLayoutManager(RV_layoutManager_users);
            RV_users.setAdapter(RV_adpter_users);


            if (response == null)
                return;
            Toast.makeText(Users.this, response, Toast.LENGTH_LONG).show();
        }
    }
}
