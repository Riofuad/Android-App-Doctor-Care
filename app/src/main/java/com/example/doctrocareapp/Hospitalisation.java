package com.example.doctrocareapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctrocareapp.adapter.HospitalisationAdapter;
import com.example.doctrocareapp.model.Form;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Hospitalisation extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference FormRef;
    private HospitalisationAdapter adapter;
    View result;


    public Hospitalisation() {
        // Required empty public constructor
    }


    public static Hospitalisation newInstance() {
        Hospitalisation fragment = new Hospitalisation();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        result = inflater.inflate(R.layout.fragment_hospitalisation, container, false);
        setUpRecyclerView();
        return result;
    }
    private void setUpRecyclerView() {
        String email_id = getActivity().getIntent().getExtras().getString("patient_email");
        FormRef = db.collection("Patient").document(email_id).collection("MyMedicalFolder");
        Query query = FormRef.whereEqualTo("type", "Hospitalisation");

        FirestoreRecyclerOptions<Form> options = new FirestoreRecyclerOptions.Builder<Form>()
                .setQuery(query, Form.class)
                .build();

        adapter = new HospitalisationAdapter(options);

        RecyclerView recyclerView = result.findViewById(R.id.hospitalisationRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
