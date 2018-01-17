package com.michalenko.olga.simpleinsta.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.michalenko.olga.simpleinsta.R;
import com.michalenko.olga.simpleinsta.model.Post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private Bitmap imageBitmap = null;
    private ImageView image;
    private FirebaseAuth mAuth;
    private EditText etext;
    private ProgressBar pb;
    private LinearLayout ll;
    FirebaseDatabase database ;
    DatabaseReference myRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mAuth = FirebaseAuth.getInstance();
        image = (ImageView)findViewById(R.id.mImageView ) ;
        etext = (EditText) findViewById(R.id.commentPhoto);
        pb = (ProgressBar) findViewById(R.id.progressPhoto);
        ll = (LinearLayout) findViewById(R.id.fotoContainer);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent,1);
        }
         database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            this.imageBitmap = imageBitmap;
        }
    }

    public void sendPhoto(View view) throws IOException {
       if(imageBitmap != null) {
           pb.setVisibility(View.VISIBLE);
           ll.setVisibility(View.GONE);
           Date date = new Date();
           File f = new File(this.getCacheDir(), mAuth.getCurrentUser().getUid().toString() + date.toString() );
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           imageBitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
           byte[] bitmapdata = bos.toByteArray();
           FileOutputStream fos = new FileOutputStream(f);
           fos.write(bitmapdata);
           fos.flush();
           fos.close();


           Uri uri = Uri.fromFile(f);
           StorageReference riversRef = mStorageRef.child("images/"+ f.getName() +".png");
           final String name = f.getName() +".png";
           riversRef.putFile(uri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Post post = new Post ();
                           Uri downloadUrl = taskSnapshot.getDownloadUrl();
                           if(downloadUrl != null) {
                               post.setImagePath(name);
                               post.setComment(etext.getText().toString());
                               post.setOwner(mAuth.getCurrentUser().getEmail().toString());
                           }
                           myRef.child(mAuth.getCurrentUser().getUid().toString()).push().setValue(post);
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {

                           pb.setVisibility(View.GONE);
                           ll.setVisibility(View.VISIBLE);
                           Toast.makeText(PhotoActivity.this, "Не удалось загрузить пост", Toast.LENGTH_SHORT).show();
                       }
                   });

           StorageReference ref = mStorageRef.child(mAuth.getCurrentUser().getUid().toString()+"/"+ f.getName() +".png");

           ref.putFile(uri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Post post = new Post ();
                           post.setImagePath(name);
                           post.setComment(etext.getText().toString());
                           post.setOwner(mAuth.getCurrentUser().getEmail().toString());
                           myRef.child("postList/").push().setValue(post);
                           pb.setVisibility(View.GONE);
                           ll.setVisibility(View.VISIBLE);
                           PhotoActivity.this.finish();
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {

                           pb.setVisibility(View.GONE);
                           ll.setVisibility(View.VISIBLE);
                           Toast.makeText(PhotoActivity.this, "Не удалось загрузить фото", Toast.LENGTH_SHORT).show();
                       }
                   });

           pb.setVisibility(View.GONE);
           ll.setVisibility(View.VISIBLE);
       } else {
           Toast.makeText(this, "Загрузите фото", Toast.LENGTH_SHORT).show();
       }

    }
}
