package pt.ubi.pdm.vivo.Visit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import pt.ubi.pdm.vivo.R;

public class onVisit extends AppCompatActivity {

    private SurfaceView SV_QRreader;
    private TextView TV_guide;
    private Button Btn_endVisit;
    private Chronometer Chronometer_visiTimer;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private final int RequestCameraPermissionID = 1001;
    private Barcode qrcode;

    private int ready = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_visit);

        SV_QRreader = findViewById(R.id.SV_QRreader);
        TV_guide = findViewById(R.id.TV_guide);
        Btn_endVisit = findViewById(R.id.Btn_endVisit);
        Chronometer_visiTimer =  findViewById(R.id.Ch_visiTimer);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();

        Btn_endVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Btn_endVisit.setVisibility(View.GONE);
                Btn_endVisit.setEnabled(false);
                SV_QRreader.setVisibility(View.VISIBLE);
                SV_QRreader.setEnabled(true);
                ready = 2;
            }
        });

        SV_QRreader.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request Permission
                    ActivityCompat.requestPermissions(onVisit.this,new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(SV_QRreader.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                // Logic used here was
                // ready == 0 means visit not started Yet
                // ready == 1 means visit started but not still not over
                // ready == 2 means visit is over
                // With those flags QR_reader is able to know what is doing

                // QR_code to start is a String "start"
                // QR_code to end is a String "end"

                if(qrcodes.size() != 0){
                    qrcode = qrcodes.valueAt(0);
                    if (qrcode.displayValue.equals("start") && ready == 0) {
                        SV_QRreader.post(new Runnable() {
                            @Override
                            public void run() {
                                SV_QRreader.setEnabled(false);
                                SV_QRreader.setVisibility(View.GONE);

                                Chronometer_visiTimer.setVisibility(View.VISIBLE);

                                Btn_endVisit.setVisibility(View.VISIBLE);
                                Btn_endVisit.setEnabled(true);

                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);

                                ready = 1;

                                TV_guide.setText("Para terminar a visita basta ler o QR code que está à saída");

                                // Timer
                                Chronometer_visiTimer.setBase(SystemClock.elapsedRealtime());
                                Chronometer_visiTimer.start();
                                // set the format for a chronometer
                                Chronometer_visiTimer.setFormat("%s");
                            }
                        });
                    } else if (qrcode.displayValue.equals("end") && ready == 2) {
                        SV_QRreader.post(new Runnable() {
                            @Override
                            public void run() {
                                ready = 3;
                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);

                                Intent i = new Intent(onVisit.this, endVisit.class);
                                i.putExtra("visitTime", Chronometer_visiTimer.getText().toString());
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                }
            }
        });

    }

    // Verify if user already gave permission to use Camera
    // If not, then requests permission to the user when activity launch
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(SV_QRreader.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }break;

        }
    }
}
