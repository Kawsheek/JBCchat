package com.dev.kaushik.jbcchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.utilities.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.Random;

public class Admin_Panel extends AppCompatActivity {
    private static final String TAG = "tag";
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText addCourses ,addFacultyNames ,addFacultyQuals;
    TextView masterKey;
    Spinner dept;
    ChildEventListener mListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem signOut = menu.findItem(R.id.sign_out_menu);
        signOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mAuthStateListener != null) {
                    mAuth.signOut();
                }
                else {
                    Toast.makeText(Admin_Panel.this, "Already logged out!", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(Admin_Panel.this, Login.class));
                return false;
            }
        });
        MenuItem courses = menu.findItem(R.id.courses);
        courses.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Admin_Panel.this, Courses.class));
                finish();
                return false;
            }
        });
        MenuItem faculty = menu.findItem(R.id.faculty);
        faculty.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Admin_Panel.this, FacultyList.class));
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__panel);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference().child("Lists");
        mRef2= mdatabase.getReference();
        addCourses = (EditText)findViewById(R.id.add_courses);
        addFacultyNames = (EditText)findViewById(R.id.add_faculty_names);
        addFacultyQuals = (EditText)findViewById(R.id.add_faculty_quals);
        masterKey = (TextView)findViewById(R.id.master_key);
        Button buttonAddCourses = (Button) findViewById(R.id.btn_add_courses);
        Button buttonAddFacultyNames = (Button)findViewById(R.id.btn_add_faculty_names);
        Button clear = (Button)findViewById(R.id.clear);
        final Button generate = (Button)findViewById(R.id.generate);
        dept = (Spinner)findViewById(R.id.dept);

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

        buttonAddCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CourseName = addCourses.getText().toString();
                String DeptName = dept.getSelectedItem().toString();
                mRef.child("CourseList").child("CourseName").setValue(DeptName+ " : " +CourseName);
                addCourses.setText("");
                Toast.makeText(Admin_Panel.this, "Done!", Toast.LENGTH_LONG).show();

            }
        });
        buttonAddFacultyNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FacultyName = addFacultyNames.getText().toString();
                String DeptName = dept.getSelectedItem().toString();
                String FacultyQuals = addFacultyQuals.getText().toString();
                mRef.child("FacultyList").child("FacultyName").setValue(DeptName+ ":-" +FacultyName +" : " + FacultyQuals);
                addFacultyNames.setText("");
                addFacultyQuals.setText("");
                Toast.makeText(Admin_Panel.this, "Done!", Toast.LENGTH_LONG).show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef2.child("master_key").removeValue();
                generate.setEnabled(true);
                masterKey.setText("");
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef2.child("master_key").setValue(getSaltString());
                generate.setEnabled(false);
            }
        });
        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String mkey = dataSnapshot.getValue().toString();
                masterKey.setText(mkey);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mRef2.addChildEventListener(mListener);
    }
    public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
