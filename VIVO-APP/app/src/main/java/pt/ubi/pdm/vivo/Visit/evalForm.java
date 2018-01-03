package pt.ubi.pdm.vivo.Visit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.session;

import static pt.ubi.pdm.vivo.session.API_host;

public class evalForm extends AppCompatActivity {

    private RadioGroup RG_1, RG_2, RG_3, RG_4;
    private Button Btn_sendEval;

    // question can be 1->bad, 2->reasonable, 3->good, 4->very good
    int answer_1, answer_2, answer_3, answer_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval_form);

        answer_1 = answer_2 = answer_3 = answer_4 = 0;

        RG_1 = findViewById(R.id.RG_1);
        RG_2 = findViewById(R.id.RG_2);
        RG_3 = findViewById(R.id.RG_3);
        RG_4 = findViewById(R.id.RG_4);
        Btn_sendEval = findViewById(R.id.Btn_sendEval);

        Btn_sendEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer_1 == 0 || answer_2 == 0 || answer_3 == 0 || answer_4 == 0) {
                    Toast.makeText(evalForm.this, "Ainda não respondeu a todas as questões", Toast.LENGTH_LONG).show();
                    return;
                }

                new sendEvaluationTask().execute(getIntent().getStringExtra("visitId"));
                finish();
            }
        });

    }

    private class sendEvaluationTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            String page_url = API_host + "/visiteval";
            String visitId = params[0];
            String question_1 =  getResources().getString(R.string.question1);
            String question_2 =  getResources().getString(R.string.question2);
            String question_3 =  getResources().getString(R.string.question3);
            String question_4 =  getResources().getString(R.string.question4);
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
                // Since this POST sends a request body setDoOutput needs to be true
                urlConnection.setDoOutput(true);


                // Prepare params to be set on body of POST request
                String data = URLEncoder.encode("visitId","UTF-8")
                        + "=" + URLEncoder.encode(visitId,"UTF-8");
                data += "&" + URLEncoder.encode("questions", "UTF-8") + "=";
                data +=       URLEncoder.encode(question_1,"UTF-8");
                data += "," + URLEncoder.encode(question_2,"UTF-8");
                data += "," + URLEncoder.encode(question_3,"UTF-8");
                data += "," + URLEncoder.encode(question_4,"UTF-8");
                data += "&" + URLEncoder.encode("answers","UTF-8") + "=";
                data +=       URLEncoder.encode(Integer.toString(answer_1),"UTF-8");
                data += "," + URLEncoder.encode(Integer.toString(answer_2),"UTF-8");
                data += "," + URLEncoder.encode(Integer.toString(answer_3),"UTF-8");
                data += "," + URLEncoder.encode(Integer.toString(answer_4),"UTF-8");

                urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                urlConnection.getResponseCode();

            } catch (IOException e) {
                e.printStackTrace();
                return "Problema de conexão, por favor verifique a sua ligação à internet";
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }
    }

    public void onRadioButtonClicked_RG1(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        RG_1.clearCheck();
        RG_1.check(view.getId());


        switch(view.getId()) {
            case R.id.RB_opt1_1:
                if (checked)
                    answer_1 = 1;
                break;

            case R.id.RB_opt2_1:
                if (checked)
                    answer_1 = 2;
                break;
            case R.id.RB_opt3_1:
                if (checked)
                    answer_1 = 3;
                break;
            case R.id.RB_opt4_1:
                if (checked)
                    answer_1 = 4;
        }
    }

    public void onRadioButtonClicked_RG2(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        RG_2.clearCheck();
        RG_2.check(view.getId());


        switch(view.getId()) {
            case R.id.RB_opt1_2:
                if (checked)
                    answer_2 = 1;
                break;

            case R.id.RB_opt2_2:
                if (checked)
                    answer_2 = 2;
                break;
            case R.id.RB_opt3_2:
                if (checked)
                    answer_2 = 3;
                break;
            case R.id.RB_opt4_2:
                if (checked)
                    answer_2 = 4;
        }
    }

    public void onRadioButtonClicked_RG3(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        RG_3.clearCheck();
        RG_3.check(view.getId());


        switch(view.getId()) {
            case R.id.RB_opt1_3:
                if (checked)
                    answer_3 = 1;
                break;

            case R.id.RB_opt2_3:
                if (checked)
                    answer_3 = 2;
                break;
            case R.id.RB_opt3_3:
                if (checked)
                    answer_3 = 3;
                break;
            case R.id.RB_opt4_3:
                if (checked)
                    answer_3 = 4;
        }
    }

    public void onRadioButtonClicked_RG4(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        RG_4.clearCheck();
        RG_4.check(view.getId());


        switch(view.getId()) {
            case R.id.RB_opt1_4:
                if (checked)
                    answer_4 = 1;
                break;

            case R.id.RB_opt2_4:
                if (checked)
                    answer_4 = 2;
                break;
            case R.id.RB_opt3_4:
                if (checked)
                    answer_4 = 3;
                break;
            case R.id.RB_opt4_4:
                if (checked)
                    answer_4 = 4;
        }
    }
}
