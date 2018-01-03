package pt.ubi.pdm.vivo.Visit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import pt.ubi.pdm.vivo.DataBase.DatabaseHelper;
import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.session;

import static pt.ubi.pdm.vivo.session.API_host;

public class showVisitsPerPeriod extends AppCompatActivity {

    private Button Btn_goBack;
    private TextView TV_dayVisits, TV_monthVisits, TV_yearVisits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_visits_per_period);

        session.visits.clear();

        new getVisitsTask().execute();

    }


    public class getVisitsTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String login_page_url = API_host + "/visits";
            String token = session.profile.getToken();
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this POST request
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
                } else
                    // No error, lets store the information returned
                    stream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                JSONArray jsonArr = new JSONArray(builder.toString());

                if (jsonArr.length() == 0)
                    return null;

                for (int i = 0; i < jsonArr.length(); i++) {
                    session.visits.add(
                            new Visit(
                                    jsonArr.getJSONObject(i).getString("_id"),
                                    jsonArr.getJSONObject(i).getString("user"),
                                    new SimpleDateFormat("yyyy-MM-dd'T'hh:mm")
                                            .parse(jsonArr.getJSONObject(i).getString("visitedAt")),
                                    jsonArr.getJSONObject(i).getString("visitDuration")
                            )
                    );

                    if (jsonArr.getJSONObject(i).has("visitEvaluation")) {
                        JSONArray evals = jsonArr.getJSONObject(i).getJSONArray("visitEvaluation");
                        for (int j = 0; j < evals.length(); j++) {
                            session.visits.get(i).setEvaluations(
                                    evals.getJSONObject(j).getString("question"),
                                    Integer.parseInt(evals.getJSONObject(j).getString("answer"))
                            );
                        }
                    }
                }

                new DatabaseHelper(showVisitsPerPeriod.this).storeVisitsDB(session.visits);

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

        protected void onPostExecute(String result) {

            Calendar now = Calendar.getInstance();
            Calendar visitDate = Calendar.getInstance();

            int today, month, year;
            today = month = year = 0;

            for ( Visit v : session.visits) {
                visitDate.setTime(v.getDate());
                if ( visitDate.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) ) { today++; month++; year++; continue; }
                if ( visitDate.get(Calendar.MONTH) == now.get(Calendar.MONTH) ) { month++; year++; continue; }
                if ( visitDate.get(Calendar.YEAR) == now.get(Calendar.YEAR) ) year++;
            }

            TV_dayVisits = findViewById(R.id.TV_dayVisits);
            TV_monthVisits = findViewById(R.id.TV_monthVisits);
            TV_yearVisits = findViewById(R.id.TV_yearVisits);
            Btn_goBack = findViewById(R.id.Btn_goBack);

            TV_dayVisits.setText(Integer.toString(today));
            TV_monthVisits.setText(Integer.toString(month));
            TV_yearVisits.setText(Integer.toString(year));

            Btn_goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            if (result == null)
                return;
            Toast.makeText(showVisitsPerPeriod.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
