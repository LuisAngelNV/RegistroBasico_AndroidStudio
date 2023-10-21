package com.luisangelnv.socialgame.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.luisangelnv.socialgame.R;
import com.luisangelnv.socialgame.models.Publicaciones;
import com.luisangelnv.socialgame.providers.AuthProviders;
import com.luisangelnv.socialgame.providers.ImageProvider;
import com.luisangelnv.socialgame.providers.PostProvider;
import com.luisangelnv.socialgame.utils.FileUtil;

import java.io.File;
import java.util.Objects;

public class Post extends AppCompatActivity {

    ImageProvider mImageProvider;
    ImageView mImageViewPostOne, mImageViewPC, mImageViewPS4, mImageViewXbox, mImageViewNintendo ;
    File mImageFile;
    Button mButtonPost;
    TextInputEditText mTextInputTitle, mTextInputDescription;
    TextView mTextViewCategory;
    String mCategory = "", mTitle = "", mDescription = "";
    PostProvider mPostProvider;
    AuthProviders mAuthProvider;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageProvider = new ImageProvider();
        mImageViewPostOne = findViewById(R.id.ImageViewPost1);
        mButtonPost = findViewById(R.id.btnPost);
        mTextInputTitle = findViewById(R.id.TxtInputVideoGame);
        mTextInputDescription = findViewById(R.id.TxtInputDescription);
        mImageViewPC = findViewById(R.id.ImageViewPC);
        mImageViewPS4 = findViewById(R.id.ImageViewPS4);
        mImageViewXbox = findViewById(R.id.ImageViewXbox);
        mImageViewNintendo = findViewById(R.id.ImageViewNintendo);
        mTextViewCategory = findViewById(R.id.TextViewCategory);
        mButtonPost.setOnClickListener(v -> ClicPost());
        mImageViewPostOne.setOnClickListener(view -> OpenGallery());
        mImageViewPC.setOnClickListener(v -> {
            mCategory= "PC";
            mTextViewCategory.setText(mCategory);
        });
        mImageViewPS4.setOnClickListener(v -> {
            mCategory = "PS4";
            mTextViewCategory.setText(mCategory);
        });
        mImageViewXbox.setOnClickListener(v -> {
            mCategory = "Xbox";
            mTextViewCategory.setText(mCategory);
        });
        mImageViewNintendo.setOnClickListener(v -> {
            mCategory = "Nintendo";
            mTextViewCategory.setText(mCategory);
        });
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProviders();
    }



    private void ClicPost() {
        mTitle = Objects.requireNonNull(mTextInputTitle.getText()).toString();
        mDescription = Objects.requireNonNull(mTextInputDescription.getText()).toString();
        if (!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            if (mImageFile != null){
                saveImage();
            }else{
                Toast.makeText(this, "Debes seleccionar una imagen tu galeria patra continuar", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Oh no!!!\r\nHas olvidado llenar alguno de los campos\r\nVerifcalos, por favor. 😁", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveImage() {
        mImageProvider.save(Post.this, mImageFile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    Publicaciones post = new Publicaciones();
                    post.setImage1(url);
                    post.setTitle(mTitle);
                    post.setDescription(mDescription);
                    post.setCategory(mCategory);
                    post.setIdUser(mAuthProvider.getUid());
                    mPostProvider.save(post).addOnCompleteListener(taskSave -> {
                        if (taskSave.isSuccessful()){
                            Toast.makeText(Post.this, "Muy bien, proceso finalizado", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Post.this, "Algo no ha funcionado bien", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                Log.d( "Campo", "Carga de imagen con exito al sistema");
            }else{
                Toast.makeText(Post.this, "Hubo un error de carga", Toast.LENGTH_LONG).show();
                Log.d( "Campo", "Carga fallida, checar");
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
                            assert result.getData() != null;
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