package pt.ubi.pdm.vivo.Visit;

import android.content.Context;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import pt.ubi.pdm.vivo.DataBase.DatabaseHelper;
import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.session;

import static pt.ubi.pdm.vivo.session.API_host;

public class VisitsLog extends AppCompatActivity {

    RecyclerView RV_visitsLog;
    RecyclerView.Adapter RV_adpter_visitsLog;
    RecyclerView.LayoutManager RV_layoutManager_visitsLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_log);

        session.visits.clear();

        if (getIntent().hasExtra("userId"))
            new getVisitsTask().execute(getIntent().getStringExtra("userId"));
        else
            new getVisitsTask().execute();

    }

    public class getVisitsTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            String login_page_url = API_host + "/visits";
            String token = session.profile.getToken();
            String userId = null;
            if (params.length > 0)
                userId = params[0];
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

            try {
                // Here we set for which url we gonna send this POST request
                url = new URL(login_page_url);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (userId != null) {
                    // Here we set that this is going to be a POST request
                    urlConnection.setRequestMethod("POST");
                    // Since this POST send a request body setDoOutput needs to be true
                    urlConnection.setDoOutput(true);

                    // Prepare params username, email and password to be set on body of POST request
                    String data = URLEncoder.encode("userId", "UTF-8")
                            + "=" + URLEncoder.encode(userId, "UTF-8");

                    urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                    urlConnection.connect();

                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(data);
                    wr.flush();
                }else {
                    // Here we set that this is going to be a GET request
                    urlConnection.setRequestMethod("GET");

                    urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                    urlConnection.connect();
                }
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

                new DatabaseHelper(VisitsLog.this).storeVisitsDB(session.visits);

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

            RV_visitsLog = findViewById(R.id.RV_visits);
            RV_visitsLog.setHasFixedSize(true);
            RV_layoutManager_visitsLog = new LinearLayoutManager(VisitsLog.this);
            RV_adpter_visitsLog = new VisitsLogAdapter();

            RV_visitsLog.setLayoutManager(RV_layoutManager_visitsLog);
            RV_visitsLog.setAdapter(RV_adpter_visitsLog);

            if (result == null)
                return;
            Toast.makeText(VisitsLog.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
