package pt.ubi.pdm.vivo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import static pt.ubi.pdm.vivo.session.API_host;

public class Login extends AppCompatActivity {

    private EditText ET_login;
    private EditText ET_password;
    private CheckBox CB_remember;
    private Button Btn_login;
    private Button Btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ET_login = findViewById(R.id.ET_login);
        ET_password = findViewById(R.id.ET_password);
        Btn_login = findViewById(R.id.Btn_login);
        Btn_signup = findViewById(R.id.Btn_signup);


        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ET_login.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Nome/Email em falta!", Toast.LENGTH_LONG).show();
                }
                else if (ET_password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Password em falta!", Toast.LENGTH_LONG).show();
                }
                else {
                    new LoginTask().execute(ET_login.getText().toString(), ET_password.getText().toString());
                }
            }
        });

        Btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));
                finish();
            }
        });
    }

    private class LoginTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String login_page_url = API_host + "/users/login";
            String login = params[0];
            String password = params[1];
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this POST request
                url = new URL(login_page_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                // Here we set that this is going to be a POST request
                urlConnection.setRequestMethod("POST");
                // Since this POST send a request body setDoOutput needs to be true
                urlConnection.setDoOutput(true);

                // Prepare params username and password to be set on body of POST request
                String data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(login, "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");

                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                // Verifies if response was an error
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    return "Dados incorretos!";
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


                Intent i = new Intent(Login.this, LoadUser.class);
                    i.putExtra("id", jsonObj.get("id").toString());
                    i.putExtra("token", jsonObj.get("token").toString());
                startActivity(i);
                finish();

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

            return null;
        }

        protected void onPostExecute(String response) {
            if (response == null)
                return;

            Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
        }
    }
}
