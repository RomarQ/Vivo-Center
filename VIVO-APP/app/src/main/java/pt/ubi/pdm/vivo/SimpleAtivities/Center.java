package pt.ubi.pdm.vivo.SimpleAtivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.Team;

public class Center extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centro);

        findViewById(R.id.cvExposicao).setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View oView) {
                final Intent i = new Intent(Center.this, Exhibition.class);
                startActivity(i);
            }
        });
        findViewById(R.id.cvEquipa).setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View oView) {
                final Intent i = new Intent(Center.this, Team.class);
                startActivity(i);
            }
        });
        findViewById(R.id.cvSobreNos).setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View oView) {
                final Intent i = new Intent(Center.this, AboutUs.class);
                startActivity(i);
            }
        });
    }
}
