package com.example.doctrocareapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doctrocareapp.model.UploadImage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileDoctorActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditProfileDoctorActivity";
    private ImageView doctorImage;
    private ImageButton selectImageDoctor;
    private Button updateProfileDoctor;
    private TextInputEditText doctorName, doctorPhone, doctorAddress;
//    private TextInputEditText doctorEmail;
    final String currentDoctorUID = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    final String doctorID = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    private Uri uriImageDoctor;

    private StorageReference pStorageRef;
    private DatabaseReference pDatabaseRef;
    private FirebaseFirestore doctorRef;
    private StorageReference pathReference;
    private static StorageReference doctorProfileImagesRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private DatabaseReference currentUserImg;
    private DatabaseReference rootRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_doctor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        doctorProfileImagesRef = FirebaseStorage.getInstance().getReference("DoctorProfile");
        rootRef = FirebaseDatabase.getInstance().getReference();

        doctorRef = FirebaseFirestore.getInstance();
        doctorImage = findViewById(R.id.doctor_image);
        selectImageDoctor = findViewById(R.id.select_doctor_image);
        updateProfileDoctor = findViewById(R.id.updateDoctorProfile);
        doctorName = findViewById(R.id.editDoctorName);
        doctorPhone = findViewById(R.id.editDoctorPhone);
        doctorAddress = findViewById(R.id.editDoctorAddress);
        //doctorEmail = findViewById(R.id.emailText);


        pStorageRef = FirebaseStorage.getInstance().getReference("DoctorProfile");
        pDatabaseRef = FirebaseDatabase.getInstance().getReference("DoctorProfile");

        //get the default doctor's information from ProfileDoctorActivity
        Intent intentDoctorProfile = getIntent(); //get the current intent
        String current_name = intentDoctorProfile.getStringExtra("CURRENT_DOCTOR_NAME");
        String current_phone = intentDoctorProfile.getStringExtra("CURRENT_DOCTOR_PHONE");
        String current_address = intentDoctorProfile.getStringExtra("CURRENT_DOCTOR_ADDRESS");

        //Set the default information in he text fields
        doctorName.setText(current_name);
        doctorPhone.setText(current_phone);
        doctorAddress.setText(current_address);
        /*
        currentUserImg = FirebaseDatabase.getInstance().getReference("DoctorProfile").child("1590965871687");
        Glide.with(this)
                .load(currentUserImg)
                .into(profileImage);
                   */
        String userPhotoPath = currentDoctorUID + ".jpg";
        pathReference = storageRef.child("DoctorProfile/" + userPhotoPath); //Doctor photo in database
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(EditProfileDoctorActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.doctor)
                        .fit()
                        .centerCrop()
                        .into(doctorImage);//Store here the imageView

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(EditProfileDoctorActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        selectImageDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        updateProfileDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateAddress = doctorAddress.getText().toString();
                String updateName = doctorName.getText().toString();
                //String updateEmail = doctorEmail.getText().toString();
                String updatePhone = doctorPhone.getText().toString();
                uploadProfileImage();
                updateDoctorInfo(updateName, updateAddress, updatePhone);
            }
        });
    }

    /* Update the doctor info in the database */
    private void updateDoctorInfo(String name, String address, String phone) {
        DocumentReference documentReference = doctorRef.collection("Doctor").document("" + doctorID + "");
        documentReference.update("address", address);
        //documentReference.update("email", email);
        documentReference.update("name", name);
        documentReference.update("tel", phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileDoctorActivity.this, "Info Updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileDoctorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Androidview", e.getMessage());
                    }
                });
    }

    /* Used to choose a file */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /* used to get the data back */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
//            uriImagePatient = data.getData();
//            Picasso.with(this).load(uriImagePatient).into(patientImage);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                StorageReference filePath = doctorProfileImagesRef.child(currentDoctorUID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(EditProfileDoctorActivity.this, "Profile image uploaded successfully...", Toast.LENGTH_SHORT).show();
//                            final String downloadUrl = filePath.getDownloadUrl().toString();
//                            String userPhotoPath = currentPatientUID + ".jpg";
//                            pathReference = storageRef.child("Profile Images").child(userPhotoPath); //Patient photo in database
//                            final String downloadUrl = pathReference.getDownloadUrl().toString();

//                            String userPhotoPath = currentPatientUID + ".jpg";
//                            pathReference = storageRef.child("PatientProfile/" + userPhotoPath); //Patient photo in database
//                            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
////                                    uriImagePatient = data.getData();
////                                    Picasso.with(EditProfilePatientActivity.this).load(uriImagePatient).into(patientImage);
//
//                                    // profileImage.setImageURI(uri);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    // Handle any errors
//                                    Toast.makeText(EditProfilePatientActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });

//                            DocumentReference imageRef = patientRef.collection("Patient").document("" + currentPatientUID + "");
//                            imageRef.update("imageURL", downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(EditProfilePatientActivity.this, "Image save in database, successfully...", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        String message = task.getException().toString();
//                                        Toast.makeText(EditProfilePatientActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                            rootRef.child("User").child(currentDoctorUID)
                                    .setValue(filePath.getDownloadUrl().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(EditProfileDoctorActivity.this, "Image save in database, successfully...", Toast.LENGTH_SHORT).show();
                                                String userPhotoPath = currentDoctorUID + ".jpg";
                                                pathReference = storageRef.child("DoctorProfile/" + userPhotoPath); //Patient photo in database
                                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Picasso.with(EditProfileDoctorActivity.this)
                                                                .load(uri)
                                                                .placeholder(R.drawable.doctor)
                                                                .fit()
                                                                .centerCrop()
                                                                .into(doctorImage); //Store here the imageView
//                                                        DocumentReference imageRef = patientRef.collection("Patient").document("" + currentPatientUID + "");
//                                                        imageRef.update("imageURL", downloadUrl);
                                                        // profileImage.setImageURI(uri);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Handle any errors
                                                        Toast.makeText(EditProfileDoctorActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            } else {
                                                String message = task.getException().toString();
                                                Toast.makeText(EditProfileDoctorActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(EditProfileDoctorActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    /* Retrieve the extension of the file to upload */
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /* Used to upload the doctor image in the DataBase */
    private void uploadProfileImage() {
        /* check if the image is not null */
        if (uriImageDoctor != null) {
            StorageReference storageReference = pStorageRef.child(currentDoctorUID
                    + "." + getFileExtension(uriImageDoctor));
            storageReference.putFile(uriImageDoctor).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return pStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.e(TAG, "then: " + downloadUri.toString());

                        UploadImage upload = new UploadImage(currentDoctorUID,
                                downloadUri.toString());
                        pDatabaseRef.push().setValue(upload);
                    }

                    /*
                    if (uriImageDoctor != null) {
                        StorageReference fileReference = pStorageRef.child(System.currentTimeMillis()
                                + "." + getFileExtension(uriImageDoctor));
                        fileReference.putFile(uriImageDoctor)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(EditProfileDoctorActivity.this, "Update Successful", Toast.LENGTH_SHORT)
                                                .show();
                                        //Upload the image to the database
                                        UploadImage uploadImage = new UploadImage(currentDoctorUID, taskSnapshot.getDownloadUrl().toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfileDoctorActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }*/
                    else {
                        Toast.makeText(EditProfileDoctorActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                /*
                private void getDownloadUrl(StorageReference fileReference) {
                    fileReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Log.d(TAG, "onSuccess" + uri);
                                }
                            });
                }
                 */


            });
        }
    }
}