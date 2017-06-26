package com.dev.kaushik.jbcchat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Panel extends AppCompatActivity {
    private static final String TAG = "tag";
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase mdatabase;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__panel);


        Button sign_out = (Button)findViewById(R.id.sign_out);
        final EditText addCourses = (EditText)findViewById(R.id.add_courses);
        EditText addFacultyNames = (EditText)findViewById(R.id.add_faculty_names);
        Button buttonAddCourses = (Button) findViewById(R.id.btn_add_courses);
        Button buttonAddFacultyNames = (Button)findViewById(R.id.btn_add_faculty_names);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null ) {
                    Log.d(TAG, "logged in" + user.getUid());
                }
                else {
                    Log.d(TAG, "logged_out");
                }
            }
        };

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAuthStateListener != null) {
                    mAuth.signOut();
                    finish();
                }
                else {
                    Toast.makeText(Admin_Panel.this, "Already logged out!", Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonAddCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CourseName = addCourses.getText().toString();
            }
        });
    }
}
