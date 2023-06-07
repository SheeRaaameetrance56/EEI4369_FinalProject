package com.ousl.final_project_eei4369_notetakingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageViewActivity extends AppCompatActivity {

    // components
    ImageView imageView;
    Button camera_btn;
    private static final int requestCamera = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.image_view);
        camera_btn = findViewById(R.id.btn_camera);

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (ContextCompat.checkSelfPermission(ImageViewActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    startActivityForResult(camera, requestCamera);
                }else {
                    // Request camera permission
                    ActivityCompat.requestPermissions(ImageViewActivity.this, new String[]{Manifest.permission.CAMERA}, requestCamera);
                }
            }
        });

    }

    // function inside camera activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if (requestCode == requestCamera){
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);

                saveImageToGallery(imageBitmap);
            }
        }catch (Exception e){

        }
    }

    // saving image
    private void saveImageToGallery(Bitmap imageBitmap) {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // set file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "IMG_"+ timestamp +".jpg";
        File imageFile = new File(storageDirectory, fileName);

        // get image bitmap to save as output stream
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush(); // writes bitmap in output stream
            outputStream.close();

            Intent mediaScanIndent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIndent.setData(Uri.fromFile(imageFile));
            sendBroadcast(mediaScanIndent);

            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}