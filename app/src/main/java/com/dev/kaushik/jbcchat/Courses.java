package com.dev.kaushik.jbcchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Courses extends AppCompatActivity {
    ListView courseList;
    ArrayList<String> courses_al = new ArrayList<>();
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;
    ChildEventListener mListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem signOut = menu.findItem(R.id.sign_out_menu);
        signOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Courses.this, Login.class));
                return false;
            }
        });
        MenuItem faculty = menu.findItem(R.id.faculty);
        faculty.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Courses.this, FacultyList.class));
                return false;
            }
        });

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        courseList = (ListView)findViewById(R.id.courseList);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReferenceFromUrl("https://jbcchat-ed847.firebaseio.com/Lists/FacultyList");

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.getValue().toString();
                courses_al.add(name);
                courseList.setAdapter(new ArrayAdapter<String>(Courses.this, android.R.layout.simple_list_item_1, courses_al));
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
        mRef.addChildEventListener(mListener);

    }
}
