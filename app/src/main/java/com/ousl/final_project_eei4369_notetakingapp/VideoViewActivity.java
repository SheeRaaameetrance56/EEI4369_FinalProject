package com.ousl.final_project_eei4369_notetakingapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoViewActivity extends AppCompatActivity {

    Button video_btn;
    VideoView videoView;

    private static final int requestCamera = 12;
    private Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        video_btn = findViewById(R.id.videoBtn);
        videoView = findViewById(R.id.videoView);


        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Create a file to store the video
                    File videoFile = createVideoFile();

                    if (videoFile != null) {
                        // Get the file URI
                        videoUri = FileProvider.getUriForFile(VideoViewActivity.this, getPackageName() + ".fileprovider", videoFile);

                        // Set the output file URI
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

                        // Request camera permission
                        if (ContextCompat.checkSelfPermission(VideoViewActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            // Camera permission granted, start the camera intent
                            startActivityForResult(intent, requestCamera);
                        } else {
                            // Request camera permission
                            ActivityCompat.requestPermissions(VideoViewActivity.this, new String[]{Manifest.permission.CAMERA}, requestCamera);
                        }
                    }
                }
            }
        private File createVideoFile() {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String videoFileName = "VID_" + timestamp + ".mp4";

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File videoFile = null;
                try {
                    videoFile = File.createTempFile(videoFileName, ".mp4", storageDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return videoFile;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestCamera && resultCode == RESULT_OK) {
            // Video captured successfully, save the video URI to the desired directory
            if (videoUri != null) {
                videoView.setVideoURI(videoUri);
                videoView.start();
                File destinationFile = new File("YOUR_DESTINATION_DIRECTORY_PATH", "YOUR_VIDEO_NAME.mp4");

                try {
                    copyFile(videoUri, destinationFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyFile(Uri videoUri, File destinationFile) throws IOException {
            InputStream inputStream = getContentResolver().openInputStream(videoUri);
            OutputStream outputStream = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
    }


}