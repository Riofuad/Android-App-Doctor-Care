package com.example.doctrocareapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.example.doctrocareapp.model.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchPatActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference doctorRef = db.collection("Doctor");

    private DoctorAdapterFiltered adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        configureToolbar();
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.searchPatRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = doctorRef.orderBy("name", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                adapter = new DoctorAdapterFiltered(task.getResult().toObjects(Doctor.class));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        Drawable r = getResources().getDrawable(R.drawable.ic_local_hospital_black_24dp);
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" Specialist" );
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(Html.fromHtml("<font color = #000000>" + "Search patient" + "</font>"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                DoctorAdapterFiltered.specialistSearch = false;
               adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_all:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("");
                return true;
            case R.id.option_PenyakitDalam:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Penyakit Dalam");
                return true;
            case R.id.option_Anak:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Anak");
                return true;
            case R.id.option_Saraf:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Saraf");
                return true;
            case R.id.option_KandunganDanGinekologi:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Kandungan dan Ginekologi");
                return true;
            case R.id.option_Bedah:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Bedah");
                return true;
            case R.id.option_KulitDanKelamin:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Kulit dan Kelamin");
                return true;
            case R.id.option_THT:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("THT");
                return true;
            case R.id.option_Mata:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Mata");
                return true;
            case R.id.option_Psikiater:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Psikiater");
                return true;
            case R.id.option_Gigi:
                DoctorAdapterFiltered.specialistSearch = true;
                adapter.getFilter().filter("Gigi");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void configureToolbar(){
        // Get the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Doctors list");
        // Sets the Toolbar
        setSupportActionBar(toolbar);
    }


}
