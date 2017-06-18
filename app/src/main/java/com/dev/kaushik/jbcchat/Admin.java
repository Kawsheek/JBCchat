package com.dev.kaushik.jbcchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin extends AppCompatActivity {

    EditText admin_id, admin_pass;
    FirebaseDatabase mdatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        admin_id = (EditText)findViewById(R.id.admin_id);
        admin_pass = (EditText)findViewById(R.id.admin_pass);
        Button submit = (Button)findViewById(R.id.btn_submit);

        String id = admin_id.getText().toString();
        String password = admin_pass.getText().toString();
        myRef= mdatabase.getReference("admin");

    }
}
