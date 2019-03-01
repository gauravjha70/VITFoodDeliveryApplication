package android.gaurav.com.vitfooddeliveryapplication.Orders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gaurav.com.vitfooddeliveryapplication.R;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class Scanner extends AppCompatActivity {

    BarcodeDetector barcodeDetector;
    SurfaceView camera;
    CameraSource cameraSource;
    SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        camera = findViewById(R.id.camera_view);
        camera.setZOrderMediaOverlay(true);
        surfaceHolder = camera.getHolder();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if(!barcodeDetector.isOperational())
        {
            Toast.makeText(this,"Sorry could not detect", Toast.LENGTH_SHORT).show();
            finish();
        }

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1040)
                .build();

        camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if(ContextCompat.checkSelfPermission(Scanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(camera.getHolder());
                    }
                }
                catch (Exception e){}
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size()>0)
                {
                    Intent intent = new Intent();
                    intent.putExtra("barcode",barcodes.valueAt(0));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

    }
}
