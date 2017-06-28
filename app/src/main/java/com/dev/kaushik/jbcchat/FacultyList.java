package com.dev.kaushik.jbcchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FacultyList extends AppCompatActivity {
    ListView FacultyList;
    ArrayList<String> faculty_al = new ArrayList<>();
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
                startActivity(new Intent(FacultyList.this, Login.class));
                return false;
            }
        });
        MenuItem courses = menu.findItem(R.id.courses);
        courses.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(FacultyList.this, Courses.class));
                return false;
            }
        });
        MenuItem faculty = menu.findItem(R.id.faculty);
        faculty.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(FacultyList.this, FacultyList.class));
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);

        FacultyList = (ListView) findViewById(R.id.faculty_list);
        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReferenceFromUrl("https://jbcchat-ed847.firebaseio.com/Lists/FacultyList");

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.getValue().toString();
                faculty_al.add(name);
                FacultyList.setAdapter(new ArrayAdapter<String>(FacultyList.this, android.R.layout.simple_list_item_1, faculty_al));
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
