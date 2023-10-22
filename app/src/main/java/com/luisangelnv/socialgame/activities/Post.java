package com.luisangelnv.socialgame.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.luisangelnv.socialgame.R;
import com.luisangelnv.socialgame.models.Publicaciones;
import com.luisangelnv.socialgame.providers.AuthProviders;
import com.luisangelnv.socialgame.providers.ImageProvider;
import com.luisangelnv.socialgame.providers.PostProvider;
import com.luisangelnv.socialgame.utils.FileUtil;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Post extends AppCompatActivity {

    ImageProvider mImageProvider;
    ImageView mImageViewPostOne, mImageViewPosDos, mImageViewPC, mImageViewPS4, mImageViewXbox, mImageViewNintendo ;
    File mImageFile, mImageFile2;
    Button mButtonPost;
    TextInputEditText mTextInputTitle, mTextInputDescription;
    TextView mTextViewCategory;
    String mCategory = "", mTitle = "", mDescription = "";
    PostProvider mPostProvider;
    AuthProviders mAuthProvider;
    private final int GALLERY_REQUEST_CODE =1 ,  GALLERY_REQUEST_CODE_2 = 2, PHOTO_REQUEST_CODE = 3, PHOTO_REQUEST_CODE_2 = 4;
    AlertDialog mDialog;
    AlertDialog.Builder mBuilderSelection;
    CircleImageView mCircleImageBack;
    CharSequence option[];
    String mAbsoltePhotoPath, mPhotoPath, mAbsoltePhotoPath2, mPhotoPath2;
    File mPhotoFile, mPhotoFile2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageProvider = new ImageProvider();
        mImageViewPostOne = findViewById(R.id.ImageViewPost1);
        mImageViewPosDos = findViewById(R.id.ImageViewPost2);
        mButtonPost = findViewById(R.id.btnPost);
        mTextInputTitle = findViewById(R.id.TxtInputVideoGame);
        mTextInputDescription = findViewById(R.id.TxtInputDescription);
        mImageViewPC = findViewById(R.id.ImageViewPC);
        mImageViewPS4 = findViewById(R.id.ImageViewPS4);
        mImageViewXbox = findViewById(R.id.ImageViewXbox);
        mImageViewNintendo = findViewById(R.id.ImageViewNintendo);
        mTextViewCategory = findViewById(R.id.TextViewCategory);
        mCircleImageBack = findViewById(R.id.circleImageBack);
        mCircleImageBack.setOnClickListener(v -> finish());
        mButtonPost.setOnClickListener(v -> ClicPost());
        mImageViewPostOne.setOnClickListener(view -> selectOptionImage(1));
        mImageViewPosDos.setOnClickListener(view -> selectOptionImage(2));
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
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento, por favor...")
                .setCancelable(false).build();

        mBuilderSelection = new AlertDialog.Builder(this);
        mBuilderSelection.setTitle("Seleccionar una opcion");
        option = new CharSequence[]{"Imagen de galeria", "Tomar foto"};

    }

    private void selectOptionImage(int numberImage) {
        mBuilderSelection.setItems(option, (dialog, which) -> {
            if( which == 0){
                if (numberImage == 1){
                    OpenGallery(GALLERY_REQUEST_CODE);
                } else if (numberImage == 2) {
                    OpenGallery(GALLERY_REQUEST_CODE_2);
                }
            } else if (which == 1) {
                if (1 == numberImage){ takePhoto(PHOTO_REQUEST_CODE);
                } else if (numberImage == 2) takePhoto(PHOTO_REQUEST_CODE_2);
            }
        });
        mBuilderSelection.show();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void takePhoto(int requestCode) {
        Intent TakePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(TakePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            }catch (Exception e){
                Toast.makeText(this, "Se ha tenido un error en el archivo" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (photoFile != null){
                Uri photouri = FileProvider.getUriForFile(Post.this, "com.luisangelnv.socialgame", photoFile);
                TakePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
                startActivityForResult(TakePictureIntent, PHOTO_REQUEST_CODE);
            }
        }
    }

    private File createPhotoFile(int requesCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File PhotoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        if (requesCode == PHOTO_REQUEST_CODE) {
            mPhotoPath = "file: " + PhotoFile.getAbsolutePath();
            mAbsoltePhotoPath = PhotoFile.getAbsolutePath();
        } else if (requesCode == PHOTO_REQUEST_CODE_2) {
            mPhotoPath2 = "file: " + PhotoFile.getAbsolutePath();
            mAbsoltePhotoPath2 = PhotoFile.getAbsolutePath();
        }
        return  PhotoFile;
    }


    private void ClicPost() {
        mTitle = Objects.requireNonNull(mTextInputTitle.getText()).toString();
        mDescription = Objects.requireNonNull(mTextInputDescription.getText()).toString();
        if (!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            //Selección de galeria
            if (mImageFile != null && mImageFile2 != null){
                saveImage(mImageFile, mImageFile2);
                //Selección de cámara ambas
            } else if(mPhotoFile != null && mPhotoFile2 != null){
                saveImage(mPhotoFile, mPhotoFile2);
            } else if(mImageFile != null && mPhotoFile2 != null){
                saveImage(mImageFile, mPhotoFile2);
            }else if(mPhotoFile != null && mImageFile2 != null){
                saveImage(mPhotoFile, mImageFile2);
            }
            else{
                Toast.makeText(this, "Debes seleccionar una imagen tu galeria patra continuar", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Oh no!!!\r\nHas olvidado llenar alguno de los campos\r\nVerifcalos, por favor. 😁", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveImage(File ImageFile, File ImageFile2) {
        mDialog.show();
        mImageProvider.save(Post.this, ImageFile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    mImageProvider.save(Post.this, ImageFile2).addOnCompleteListener(taskImage2 -> {
                        if(taskImage2.isSuccessful()){
                            mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(uri2 -> {
                                String url2 = uri2.toString();
                                Publicaciones post = new Publicaciones();
                                post.setImage1(url);
                                post.setImage2(url2);
                                post.setTitle(mTitle);
                                post.setDescription(mDescription);
                                post.setCategory(mCategory);
                                post.setIdUser(mAuthProvider.getUid());
                                mPostProvider.save(post).addOnCompleteListener(taskSave -> {
                                    mDialog.dismiss();
                                    if (taskSave.isSuccessful()){
                                        ClearForm();
                                        Toast.makeText(Post.this, "Muy bien, proceso finalizado", Toast.LENGTH_SHORT).show();
                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(Post.this, "Algo no ha funcionado bien", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                        }else {
                            mDialog.dismiss();
                            Toast.makeText(this, "Oh no...\n\rSe ha producido un error al momento de guaradr tu imagem.", Toast.LENGTH_LONG).show();
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

    private void ClearForm() {
        mTextInputTitle.setText("");
        mTextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImageViewPostOne.setImageResource(R.drawable.upload_image);
        mImageViewPosDos.setImageResource(R.drawable.upload_image);
        mTitle = "";
        mDescription = "";
        mCategory = "";
        mImageFile = null;
        mImageFile2 = null;
    }

    /** @noinspection deprecation*/
    private void OpenGallery(int requestCode) {
        Intent GalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        //galleryLauncher.launch(GalleryIntent);
        startActivityForResult(GalleryIntent, requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent  data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        SELECCIÓN DE GALERIA
         */
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mPhotoFile = null;
                assert data != null;
                mImageFile = FileUtil.from(Post.this, data.getData());
                mImageViewPostOne.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Toast.makeText(Post.this, "Se ha tenido un error con: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            try {
                mPhotoFile2 = null;
                assert data != null;
                mImageFile2 = FileUtil.from(Post.this, data.getData());
                mImageViewPosDos.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            } catch (Exception e) {
                Toast.makeText(Post.this, "Se ha tenido un error con: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
            mImageFile = null;
            mPhotoFile = new File(mAbsoltePhotoPath);
            Picasso.with(Post.this).load(mPhotoPath).into((mImageViewPostOne));
        }
        if (requestCode == PHOTO_REQUEST_CODE_2 && resultCode == RESULT_OK){
            mImageFile2 = null;
            mPhotoFile2 = new File(mAbsoltePhotoPath2);
            Picasso.with(Post.this).load(mPhotoPath2).into((mImageViewPosDos));
        }
    }

    /*ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
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
    );*/
}