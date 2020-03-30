package com.tzutalin.dlibtest;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class cameraEvent extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button btn_record;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;

    private int mCameraFacing;
    private Button btnChangeFacing;
    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.camera);

        TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("녹하를 위해 권한을 허용하시오.")
                .setDeniedMessage("거부 됨")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .check();

        btn_record = (Button)findViewById(R.id.camstart);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(recording) {
                    Toast.makeText(cameraEvent.this, "여기들어옴", Toast.LENGTH_SHORT).show();
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    camera.lock();
                    recording = false;
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(cameraEvent.this, "녹화가 시작?", Toast.LENGTH_SHORT).show();
                            try {

                                mediaRecorder.start();
                                recording= true;


                            } catch (Exception e) {
                                mediaRecorder.release();

                            }


                        }
                    });
                }
            }
        });




    }

    PermissionListener permission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(cameraEvent.this, "권한 허가", Toast.LENGTH_SHORT).show();

            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView)findViewById(R.id.surface);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(cameraEvent.this);
            surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(cameraEvent.this, "권한 거부", Toast.LENGTH_SHORT).show();

            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView)findViewById(R.id.surface);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(cameraEvent.this);
            surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    };



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        refreshCamrea(camera);
    }

    private void refreshCamrea(Camera camera) {
        if(surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setCamera(camera);
    }

    private void setCamera(Camera cam) {
        camera = cam;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamrea(camera);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
