package pt.ubi.pdm.vivo.Visit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import pt.ubi.pdm.vivo.session;

import static pt.ubi.pdm.vivo.session.API_host;

public class endVisit extends AppCompatActivity {

    private Button Btn_goBack, Btn_evaluate;
    private TextView TV_visiTime;

    private String visitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_visit);

        final String visitTime = getIntent().getStringExtra("visitTime");

        new addVisitTask().execute(visitTime);

        TV_visiTime = findViewById(R.id.TV_visiTime);
        Btn_goBack = findViewById(R.id.Btn_goBack);
        Btn_evaluate = findViewById(R.id.Btn_evaluate);

        TV_visiTime.setText(visitTime);

        Btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Btn_evaluate.setEnabled(false);
                Btn_evaluate.setVisibility(View.GONE);

                if (visitId == null)
                    return;

                Intent i = new Intent(endVisit.this, evalForm.class);
                i.putExtra("visitId", visitId);
                startActivity(i);
            }
        });

        onRestart();
    }

    private class addVisitTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            String page_url = API_host + "/newvisit";
            String duration = params[0];
            String token = session.profile.getToken();
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this POST request
                url = new URL(page_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                // Here we set that this is going to be a POST request
                urlConnection.setRequestMethod("POST");
                // Since this POST send a request body setDoOutput needs to be true
                urlConnection.setDoOutput(true);

                urlConnection.addRequestProperty("Authorization", "Bearer " + token);

                // Prepare params to be set on body of POST request
                String data = URLEncoder.encode("duration", "UTF-8")
                        + "=" + URLEncoder.encode(duration, "UTF-8");

                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

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
                Log.d("rod", builder.toString());
                JSONObject jsonObj = new JSONObject(builder.toString());

                if (jsonObj.has("visitId"))
                    visitId = jsonObj.getString("visitId");

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
    }
}
