package com.example.doctrocareapp.fireStoreApi;

import com.example.doctrocareapp.model.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientHelper {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference PatientRef = db.collection("Patient");

    public static void addPatient(String name, String address, String tel, String dateOfBirth){
        Patient patient = new Patient(name, address,tel,FirebaseAuth.getInstance().getCurrentUser().getEmail(), dateOfBirth);
        System.out.println("Create object patient");
        PatientRef.document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).set(patient);
    }
}
