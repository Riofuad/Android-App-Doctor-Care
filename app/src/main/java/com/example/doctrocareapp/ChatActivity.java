package com.example.doctrocareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctrocareapp.adapter.MessageAdapter;
import com.example.doctrocareapp.model.Message;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Bundle extras;
    private CollectionReference MessageRef1 ;
    private CollectionReference MessageRef2 ;
    private MessageAdapter adapter;
    private TextInputEditText send;
    private Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        extras = getIntent().getExtras();
        MessageRef1 = FirebaseFirestore.getInstance().collection("chat").document(extras.getString("key1")).collection("message");
        MessageRef2 = FirebaseFirestore.getInstance().collection("chat").document(extras.getString("key2")).collection("message");
        send = (TextInputEditText)findViewById(R.id.activity_mentor_chat_message_edit_text);
        btnSend = (Button)findViewById(R.id.activity_mentor_chat_send_button);
        setUpRecyclerView();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message(send.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
                MessageRef1.document().set(msg);
                MessageRef2.document().set(msg);
                send.setText("");
//                if (!send.getText().toString().isEmpty()){
//                    Message msg = new Message(send.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
//                    MessageRef1.document().set(msg);
//                    MessageRef2.document().set(msg);
//                    send.setText("");
//
//                } else {
//                    Toast.makeText(ChatActivity.this, "Insert text to send!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = MessageRef1.orderBy("dateCreated");
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        adapter = new MessageAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.activity_mentor_chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
