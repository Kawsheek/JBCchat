package com.dev.kaushik.jbcchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Kaushik Das on 3/5/2017.
 */

public class AdminLogin extends AppCompatActivity {

    EditText admin_email, admin_pass;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(AdminLogin.this, Admin_Panel.class));
            finish();
         }

         //setting the views
        admin_email = (EditText)findViewById(R.id.admin_email);
        admin_pass = (EditText)findViewById(R.id.admin_pass);
        Button submit = (Button)findViewById(R.id.btn_submit);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = admin_email.getText().toString();
                final String password = admin_pass.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(),"Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //Authenticate User
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(AdminLogin.this, "Authentication failure!", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Intent intent = new Intent(AdminLogin.this, Admin_Panel.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });



            }
        });

    }
}
