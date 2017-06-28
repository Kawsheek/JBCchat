package com.dev.kaushik.jbcchat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Panel extends AppCompatActivity {
    private static final String TAG = "tag";
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__panel);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference().child("Lists");
        Button sign_out = (Button)findViewById(R.id.sign_out);
        final EditText addCourses = (EditText)findViewById(R.id.add_courses);
        final EditText addFacultyNames = (EditText)findViewById(R.id.add_faculty_names);
        final EditText addFacultyQuals = (EditText)findViewById(R.id.add_faculty_quals);
        Button buttonAddCourses = (Button) findViewById(R.id.btn_add_courses);
        Button buttonAddFacultyNames = (Button)findViewById(R.id.btn_add_faculty_names);
        final Spinner dept = (Spinner)findViewById(R.id.dept);

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
                String DeptName = dept.getSelectedItem().toString();
                mRef.child("CourseList").child("CourseName").setValue(CourseName);
                addCourses.setText("");

            }
        });
        buttonAddFacultyNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FacultyName = addFacultyNames.getText().toString();
                String DeptName = dept.getSelectedItem().toString();
                String FacultyQuals = addFacultyQuals.getText().toString();
                mRef.child("FacultyList").child("FacultyName").setValue(FacultyName +": " + FacultyQuals);
                addFacultyNames.setText("");
            }
        });
    }
}
