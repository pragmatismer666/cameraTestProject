package krunal.com.example.cameraapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";

    private AppExecutor mAppExcutor;

    private ImageView mImageView;
    private TextView mComment;

    private Button mStartCamera,mReview, mSaveButton;

    private String mTempPhotoPath;

    private Bitmap mResultsBitmap;

    private FloatingActionButton mClear,mSave,mShare;

    private String mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppExcutor = new AppExecutor();

        mImageView = findViewById(R.id.imageView);
        mComment = findViewById(R.id.textView);
        mStartCamera = findViewById(R.id.startCamera);
        mSaveButton = findViewById(R.id.save);
        mReview = findViewById(R.id.review);

        mImageView.setVisibility(View.GONE);
        mComment.setVisibility(View.GONE);

        mStartCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // If you do not have permission, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        REQUEST_STORAGE_PERMISSION);
            } else {
                launchCamera();
            }
        });

        mReview.setOnClickListener(v -> {
            Intent reviewintent = new Intent(getApplicationContext(), ReviewActivity.class);
            startActivity(reviewintent);
        });

        mSaveButton.setOnClickListener((View v) -> {
            mContent = mComment.getText().toString();
            mAppExcutor.diskIO().execute(() -> {
                // Delete the temporary image file
                BitmapUtils.deleteImageFile(this, mTempPhotoPath);
                // Save the image
                String filePath = BitmapUtils.saveImage(this, mResultsBitmap);
                DBHelper myDb = new DBHelper(MainActivity.this);
                myDb.insertContact(filePath, mContent);
            });
            Toast.makeText(this,"Image Save",Toast.LENGTH_LONG).show();
            processAndSetImageReset();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {
            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    private void launchCamera() {
        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();
                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);
                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private void processAndSetImage() {
        // Toggle Visibility of the views
        mStartCamera.setVisibility(View.GONE);
        mSaveButton.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);
        mComment.setVisibility(View.VISIBLE);
        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);
        // Set the new bitmap to the ImageView
        mImageView.setImageBitmap(mResultsBitmap);
    }

    private void processAndSetImageReset() {
        // Toggle Visibility of the views
        mStartCamera.setVisibility(View.VISIBLE);
        mSaveButton.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        mComment.setVisibility(View.GONE);
    }


}
