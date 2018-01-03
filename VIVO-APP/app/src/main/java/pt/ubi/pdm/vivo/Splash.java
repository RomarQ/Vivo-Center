package pt.ubi.pdm.vivo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import pt.ubi.pdm.vivo.DataBase.DatabaseHelper;

public class Splash extends AppCompatActivity {
    private CircleImageView IV_logo_image;
    private ImageView IV_logo_text;
    private LinearLayout LL_cape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LL_cape = findViewById(R.id.LL_splash);
        IV_logo_image = findViewById(R.id.IV_logo_image);
        IV_logo_text = findViewById(R.id.IV_logo_text);
        Animation splash = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        IV_logo_image.startAnimation(splash);
        IV_logo_text.startAnimation(splash);
        LL_cape.startAnimation(splash);

        Thread timer = new Thread() {
            public void run () {
                Intent i;
                try {
                    sleep(4000);
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                } finally {
                    Bundle b = new DatabaseHelper(Splash.this).get_Id_Token();
                    if (b == null) {
                        i = new Intent(Splash.this, Login.class);
                    } else {
                        i = new Intent(Splash.this, LoadUser.class);
                        i.putExtras(b);
                    }
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }

}
