package com.example.jplan.Diary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jplan.Main.MainActivity;
import com.example.jplan.Model.Diary;
import com.example.jplan.Plan.PlanAddActivity;
import com.example.jplan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiaryAddActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1;
    private ActivityResultLauncher<Intent> resultLauncher;


    ImageView diary_pic_btn;
    EditText icon_edt, diary_content_tv;
    Button add_btn;

    String icon_str, content_str;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    Date toTimeStamp = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String timestamp_date = dateFormat.format(toTimeStamp);

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_add);

        diary_content_tv = findViewById(R.id.diary_content_tv);
        diary_pic_btn = findViewById(R.id.diary_pic_btn);
        icon_edt = findViewById(R.id.icon_edt);
        add_btn = findViewById(R.id.add_btn);

        diary_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult.launch(intent);
            }
        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                icon_str = icon_edt.getText().toString();
                content_str = diary_content_tv.getText().toString();

                if (CheckEmpty(icon_str, content_str)) {
                    StorageReference storageReference = mStorage.getReference()
                            .child("diaryImage").child(firebaseAuth.getCurrentUser().getUid()).child(timestamp_date);
                    System.out.println("DiaryAddActivity imageUri 0 " + imageUri);

                    UploadTask uploadTask = storageReference.putFile(imageUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Toast.makeText(DiaryAddActivity.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DiaryAddActivity.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                                Uri downloadUri = task.getResult();

                                Diary diary = new Diary();
                                diary.setEmailId(firebaseAuth.getCurrentUser().getEmail());
                                diary.setIcon(icon_edt.getText().toString());
                                diary.setDiaryContent(diary_content_tv.getText().toString());
                                diary.setImgUrl(downloadUri.toString());
                                firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("Diary").document(timestamp_date).set(diary);

                                Intent intent = new Intent(DiaryAddActivity.this, DiaryFragment.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(DiaryAddActivity.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });


    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        imageUri = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            diary_pic_btn.setImageBitmap(bitmap);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    private boolean CheckEmpty(String... params) {
        for (String param : params) {
            if (param == null || param.isEmpty()) {
                return false;
            }
        }
        return true;
    }

}