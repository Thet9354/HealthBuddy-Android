package com.example.healthbuddy;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.healthbuddy.Fragment.Nutrition;
import com.example.healthbuddy.Model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddRecipe_Activity extends AppCompatActivity {

    private EditText editTxt_recipeName, editTxt_caloriesAmt, editTxt_recipeDetail;
    private FrameLayout fl_addRecipe;
    private Button btn_add;

    // Variables to store input value
    private String recipeName, recipeCalories, recipeDetails;
    private Uri ImageUrl;
    private Bitmap bitmap, defaultBitmap;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("recipe");

    private static final int requestCamera1 = 12;

    private String photoUrl;
    private String currentUserId;

    Intent intent;
    private String mName, mPhoneNumber, email, mPassword;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://healthbuddy-42e58-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference  = database.getReference().child("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        intent = getIntent();

        initWidget();

        getIntentData();

        pageDirectories();
    }

    private void getIntentData() {

        mName = intent.getStringExtra("Name");
        mPhoneNumber = intent.getStringExtra("Phone Number");
        email = intent.getStringExtra("Email");
        mPassword = intent.getStringExtra("Password");
    }

    private void pageDirectories() {

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recipeName = editTxt_recipeName.getText().toString();
                recipeCalories = editTxt_caloriesAmt.getText().toString();
                recipeDetails = editTxt_recipeDetail.getText().toString();


                validateName();
                validateCalories();
                validateDetails();
                validateImage();
                validateInput();
            }
        });
    }

    private void validateInput() {
        if (!validateName() | !validateCalories() | !validateDetails()) {
            Toast.makeText(this, "Something is wrong here", Toast.LENGTH_SHORT).show();
        }
        else {
            //ADD DATA STORED TO FIREBASE
            addData();
        }
    }

    private void addData() {

        String userRecipe = "User's Recipe";
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(mPhoneNumber).hasChild(userRecipe))
                {
                    long numOfRecipe = snapshot.child(mPhoneNumber).child(userRecipe).getChildrenCount();
                    System.out.println(numOfRecipe);

                    for (int i = (int) numOfRecipe; i<=50; i++)
                    {
                        String recipeID = "recipe " + (numOfRecipe + 1);

                        databaseReference.child(mPhoneNumber).child(userRecipe).child(recipeID).child("recipeName").setValue(recipeName);
                        databaseReference.child(mPhoneNumber).child(userRecipe).child(recipeID).child("recipeCalories").setValue(recipeCalories);
                        databaseReference.child(mPhoneNumber).child(userRecipe).child(recipeID).child("recipeDetails").setValue(recipeDetails);

//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] data = baos.toByteArray();
//
//                        StorageReference riversRef = imagesRef.child(recipeID+".jpg");
//                        UploadTask uploadTask = riversRef.putBytes(data);
//
//                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // Get a URL to the uploaded content
//                                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(AddRecipe_Activity.this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    }

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));


                }
                else
                {
                    databaseReference.child(mPhoneNumber).child(userRecipe).child("recipe 1").child("recipeName").setValue(recipeName);
                    databaseReference.child(mPhoneNumber).child(userRecipe).child("recipe 1").child("recipeCalories").setValue(recipeCalories);
                    databaseReference.child(mPhoneNumber).child(userRecipe).child("recipe 1").child("recipeDetails").setValue(recipeDetails);

//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] data = baos.toByteArray();
//
//                    StorageReference riversRef = imagesRef.child("Recipe 1.jpg");
//                    UploadTask uploadTask = riversRef.putBytes(data);
//
//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Get a URL to the uploaded content
//                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(AddRecipe_Activity.this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void validateImage() {

    }

    private boolean validateDetails() {
        if (recipeDetails.isEmpty())
        {
            editTxt_recipeDetail.setError("Required");
            return false;
        }
        else
            return true;
    }

    private boolean validateCalories() {
        if (recipeCalories.isEmpty())
        {
            editTxt_caloriesAmt.setError("Required");
            return false;
        }
        else
            return true;
    }

    private boolean validateName() {
        if (recipeName.isEmpty())
        {
            editTxt_recipeName.setError("Required");
            return false;
        }
        else
            return true;
    }

    private void initWidget() {

        // EditText
        editTxt_recipeName = findViewById(R.id.editTxt_recipeName);
        editTxt_caloriesAmt = findViewById(R.id.editTxt_caloriesAmt);
        editTxt_recipeDetail = findViewById(R.id.editTxt_recipeDetail);

        // Button
        btn_add = findViewById(R.id.btn_add);

        // FrameLayout
        fl_addRecipe = findViewById(R.id.fl_addRecipe);

    }
}