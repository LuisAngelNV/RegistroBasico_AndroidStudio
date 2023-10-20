package com.luisangelnv.socialgame.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.luisangelnv.socialgame.R;
import com.luisangelnv.socialgame.providers.ImageProvider;
import com.luisangelnv.socialgame.utils.FileUtil;

import java.io.File;

public class Post extends AppCompatActivity {

    ImageProvider mImageProvider;
    ImageView mImageViewPostOne;
    private final int Gallery_Request_Code = 1;
    File mImageFile;
    Button mButtonPost;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageProvider = new ImageProvider();
        mImageViewPostOne = findViewById(R.id.ImageViewPost1);
        mButtonPost = findViewById(R.id.btnPost);
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        mImageViewPostOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
    }

    private void saveImage() {
        mImageProvider.save(Post.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Post.this, "Carga exitosa", Toast.LENGTH_LONG).show();
                    Log.d( "Campo", "Carga de imagen con exito al sistema");
                }else{
                    Toast.makeText(Post.this, "Hubo un error de carga", Toast.LENGTH_LONG).show();
                    Log.d( "Campo", "Carga fallida, checar");
                }
            }
        });
    }

    private void OpenGallery() {
        Intent GalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        galleryLauncher.launch(GalleryIntent);
        //startActivityForResult(GalleryIntent, Gallery_Request_Code);

    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Request_Code && resultCode == RESULT_OK) {

        }
    }
    */
    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        try {
                            mImageFile = FileUtil.from(Post.this, result.getData().getData());
                            mImageViewPostOne.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                        } catch (Exception e) {
                            Toast.makeText(Post.this, "Se ha tenido un error con: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );

}