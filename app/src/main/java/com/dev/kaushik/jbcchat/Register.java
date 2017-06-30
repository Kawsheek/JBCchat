package com.dev.kaushik.jbcchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kaushik Das on 5/5/2017.
 */

public class Register extends AppCompatActivity {
    EditText username, password, cpassword,mKeyEntered;
    Button registerButton, login;
    String user, pass, cpass, dept, master_key;
    Spinner department;
    CheckBox tCheckbox;
    TextView chooseDept;
    FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef;
    ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        cpassword = (EditText)findViewById(R.id.cpassword);
        mKeyEntered = (EditText)findViewById(R.id.mKey_entered);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (Button)findViewById(R.id.login);
        department = (Spinner)findViewById(R.id.dept);
        tCheckbox = (CheckBox)findViewById(R.id.tCheckbox);
        chooseDept = (TextView)findViewById(R.id.choose_dept);
        mRef = mdatabase.getReference().child("master_key");

        master_key = mKeyEntered.getText().toString();
        mListener = new ValueEventListener() {
            @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserDetails.mKeyRef = dataSnapshot.getValue().toString();
                }
            @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
        mRef.addValueEventListener(mListener);

        Firebase.setAndroidContext(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString().toLowerCase();
                pass = password.getText().toString();
                cpass = cpassword.getText().toString();
                dept = department.getSelectedItem().toString();
                master_key = mKeyEntered.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("only alphabet or number allowed");
                }
                else if(user.length()<5){
                    username.setError("at least 5 characters long");
                }
                else if(pass.length()<8){
                    password.setError("at least 8 characters long");
                }
                else if (!cpass.equals(pass)) {
                    cpassword.setError("mismatch passwords");
                }
                else if (dept.equals("select")) {
                    registerButton.setEnabled(false);
                    chooseDept.setError("Choose your department");
                }
                else if (master_key.equals("")) {
                    mKeyEntered.setError("can't be blank");
                }
                if (!UserDetails.mKeyRef.equals(master_key)) {
                    mKeyEntered.setError("Invalid key entered, confirm with college ADMIN");
                }

                else if (!tCheckbox.isChecked()) {
                    tCheckbox.setError("Confirm your identity");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://jbcchat-ed847.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://jbcchat-ed847.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                reference.child(user).child("department").setValue(dept);
                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("department").setValue(dept);
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                    }

                                    else {
                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });
    }
}