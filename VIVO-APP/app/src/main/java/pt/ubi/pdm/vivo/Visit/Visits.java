package pt.ubi.pdm.vivo.Visit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.howToGetHere;
import pt.ubi.pdm.vivo.SimpleAtivities.Prices;

public class Visits extends AppCompatActivity {

    private CardView CV_startVisit, CV_visitsLog, CV_prices, CV_howToGetHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        CV_startVisit = findViewById(R.id.CV_startVisit);
        CV_visitsLog = findViewById(R.id.CV_visitsLog);
        CV_prices = findViewById(R.id.CV_prices);
        CV_howToGetHere = findViewById(R.id.CV_howToGetHere);

        CV_startVisit.setOnClickListener(new CardView.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(Visits.this, onVisit.class));
            }

        });

        CV_visitsLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Visits.this, VisitsLog.class));
            }
        });

        CV_prices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Visits.this, Prices.class));
            }
        });

        CV_howToGetHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Visits.this, howToGetHere.class));
            }
        });
    }
}
