package pt.ubi.pdm.vivo.Users;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.Visit.VisitsLog;
import pt.ubi.pdm.vivo.session;

import static pt.ubi.pdm.vivo.session.API_host;

// Only admins have Access to this activity

public class aboutUser extends AppCompatActivity {

    User user;

    private EditText ET_username, ET_email;
    private Button Btn_deleteUser, Btn_updateUser, Btn_visitsUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user);

        user = new User().getUser(getIntent().getStringExtra("userId"));

        ET_username = findViewById(R.id.ET_username);
        ET_email = findViewById(R.id.ET_email);

        Btn_deleteUser = findViewById(R.id.Btn_deleteUser);
        Btn_updateUser = findViewById(R.id.Btn_updateUser);
        Btn_visitsUser = findViewById(R.id.Btn_visitsUser);

        ET_username.setText(user.getUsername());
        ET_email.setText(user.getEmail());

        if (user.getIsAdmin()) {
            Btn_deleteUser.setEnabled(false);
            Btn_deleteUser.setVisibility(View.GONE);
        }

        Btn_deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteUserTask().execute(user.getUserId());
            }
        });

        Btn_updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ET_username.getText().toString();
                String email = ET_email.getText().toString();

                if (username.equals(user.getUsername()) && email.equals(user.getEmail()))
                    Toast.makeText(
                            aboutUser.this,
                            "Precisa de alterar os dados primeiro!",
                            Toast.LENGTH_LONG).show();
                else
                    new updateUserTask().execute(user.getUserId(), username, email);
            }
        });

        Btn_visitsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(aboutUser.this, VisitsLog.class);
                i.putExtra("userId", user.getUserId());
                startActivity(i);
            }
        });

    }

    private class deleteUserTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String login_page_url = API_host + "/user";
            String userId = params[0];
            String token = session.profile.getToken();
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this POST request
                url = new URL(login_page_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                // Here we set that this is going to be a DELETE request
                urlConnection.setRequestMethod("DELETE");
                // Since this DELETE sends a request body setDoOutput needs to be true
                urlConnection.setDoOutput(true);

                // Prepare param userId to be set on body of DELETE request
                String data = URLEncoder.encode("userId", "UTF-8")
                        + "=" + URLEncoder.encode(userId, "UTF-8");

                urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                // Verifies if response was an error
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    stream = urlConnection.getErrorStream();
                    return "Ocorreu algum problema!";
                }
                else
                    // No error, check the information returned
                    stream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                JSONObject jsonObj = new JSONObject(builder.toString());

                if ( jsonObj.getBoolean("done") == true ){
                    finish();
                    return "Utilizador eliminado com sucesso!";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "Problema de conexão, por favor verifique a sua ligação à internet";
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return "Ocorreu algum problema!";
        }

        protected void onPostExecute(String response) {
            if (response == null)
                return;

            Toast.makeText(aboutUser.this, response, Toast.LENGTH_LONG).show();
        }
    }

    private class updateUserTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String login_page_url = API_host + "/user";
            String userId = params[0];
            String username = params[1];
            String email = params[2];
            String token = session.profile.getToken();
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this POST request
                url = new URL(login_page_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                // Here we set that this is going to be a POST request
                urlConnection.setRequestMethod("POST");
                // Since this POST sends a request body setDoOutput needs to be true
                urlConnection.setDoOutput(true);

                // Prepare params userId, username and email to be set on body of POST request
                String data = URLEncoder.encode("userId", "UTF-8")
                        + "=" + URLEncoder.encode(userId, "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(email, "UTF-8");

                urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                // Verifies if response was an error
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    stream = urlConnection.getErrorStream();
                    return "Ocorreu algum problema!";
                }
                else
                    // No error, check the information returned
                    stream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                JSONObject jsonObj = new JSONObject(builder.toString());

                if ( jsonObj.getBoolean("done") == true ){
                    user.setUsername(username);
                    user.setEmail(email);
                    return "Dados alterados com sucesso!";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "Problema de conexão, por favor verifique a sua ligação à internet";
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return "Muito provavelmente já existem utilizadores com esses dados!";
        }

        protected void onPostExecute(String response) {
            if (response == null)
                return;

            Toast.makeText(aboutUser.this, response, Toast.LENGTH_LONG).show();
        }
    }
}
