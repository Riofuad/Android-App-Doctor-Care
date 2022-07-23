package com.example.doctrocareapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doctrocareapp.ChatActivity;
import com.example.doctrocareapp.R;
import com.example.doctrocareapp.model.AppointmentInformation;
import com.example.doctrocareapp.model.Doctor;
import com.example.doctrocareapp.model.Patient;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorAppointmentAdapter extends FirestoreRecyclerAdapter<AppointmentInformation, DoctorAppointmentAdapter.MyDoctorAppointmentHolder> {
    StorageReference pathReference ;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public DoctorAppointmentAdapter(@NonNull FirestoreRecyclerOptions<AppointmentInformation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDoctorAppointmentHolder myDoctorAppointmentHolder, int position, @NonNull final AppointmentInformation appointmentInformation) {
        myDoctorAppointmentHolder.dateAppointment.setText(appointmentInformation.getTime());
        myDoctorAppointmentHolder.patientName.setText(appointmentInformation.getPatientName());
        myDoctorAppointmentHolder.appointmentType.setText(appointmentInformation.getAppointmentType());
        myDoctorAppointmentHolder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentInformation.setType("Accepted");
                FirebaseFirestore.getInstance().collection("Patient").document(appointmentInformation.getPatientId()).collection("calendar")
                        .document(appointmentInformation.getTime().replace("/","_")).set(appointmentInformation);
                FirebaseFirestore.getInstance().document(appointmentInformation.getPath()).update("type","Accepted");
                FirebaseFirestore.getInstance().collection("Doctor").document(appointmentInformation.getDoctorId()).collection("calendar")
                        .document(appointmentInformation.getTime().replace("/","_")).set(appointmentInformation);

//////////// here add patient friend to doctor

                FirebaseFirestore.getInstance().document("Patient/"+ appointmentInformation.getPatientId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                FirebaseFirestore.getInstance().collection("Doctor").document(appointmentInformation.getDoctorId()+"")
                                        .collection("MyPatients").document(appointmentInformation.getPatientId()).set(documentSnapshot.toObject(Patient.class));
                            }
                        });
                FirebaseFirestore.getInstance().document("Doctor/"+ appointmentInformation.getDoctorId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                FirebaseFirestore.getInstance().collection("Patient").document(appointmentInformation.getPatientId()+"")
                                        .collection("MyDoctors").document(appointmentInformation.getPatientId()).set(documentSnapshot.toObject(Doctor.class));
                            }
                        });


                getSnapshots().getSnapshot(position).getReference().delete();
            }
        });
        myDoctorAppointmentHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentInformation.setType("Refused");
                FirebaseFirestore.getInstance().collection("Patient").document(appointmentInformation.getPatientId()).collection("calendar")
                        .document(appointmentInformation.getTime().replace("/","_")).set(appointmentInformation);
                FirebaseFirestore.getInstance().document(appointmentInformation.getPath()).delete();
                getSnapshots().getSnapshot(position).getReference().delete();
            }
        });

        String imageId = appointmentInformation.getPatientId()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(myDoctorAppointmentHolder.patient_image.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myDoctorAppointmentHolder.patient_image);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void openPage(Context wf, Doctor d){
        Intent i = new Intent(wf, ChatActivity.class);
        i.putExtra("key1",d.getEmail()+"_"+ FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
        i.putExtra("key2",FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()+"_"+d.getEmail());
        wf.startActivity(i);
    }

    @NonNull
    @Override
    public MyDoctorAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointment_item, parent, false);
        return new MyDoctorAppointmentHolder(v);
    }

    class MyDoctorAppointmentHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorAppointmentItems
        TextView dateAppointment;
        TextView patientName;
        Button approveBtn;
        Button cancelBtn;
        TextView appointmentType;
        ImageView patient_image;
        public MyDoctorAppointmentHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointment = itemView.findViewById(R.id.appointment_date);
            patientName = itemView.findViewById(R.id.patient_name);
            approveBtn = itemView.findViewById(R.id.btn_accept);
            cancelBtn = itemView.findViewById(R.id.btn_decline);
            appointmentType = itemView.findViewById(R.id.appointment_type);
            patient_image = itemView.findViewById(R.id.doctor_image);
        }
    }




}
