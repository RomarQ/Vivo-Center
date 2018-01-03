package pt.ubi.pdm.vivo.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pt.ubi.pdm.vivo.R;


public class sliderRoutes extends AppCompatActivity {
    private ViewPager viewPager;
    private sliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_routes);

        viewPager = (ViewPager) findViewById(R.id.VP_slider);
        sliderAdapter = new sliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
    }
}
