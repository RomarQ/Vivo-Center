package pt.ubi.pdm.vivo.EducativeOffers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import pt.ubi.pdm.vivo.R;

public class educativeOffers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educative_offers);

        findViewById(R.id.CV_offer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(educativeOffers.this, Offer1.class));
            }
        });

        findViewById(R.id.CV_offer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(educativeOffers.this, Offer2.class));
            }
        });

        findViewById(R.id.CV_offer3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(educativeOffers.this, Offer3.class));
            }
        });

        findViewById(R.id.CV_offer4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(educativeOffers.this, Offer4.class));
            }
        });
    }
}
