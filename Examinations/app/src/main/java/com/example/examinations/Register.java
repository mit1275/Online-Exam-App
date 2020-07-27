package com.example.examinations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examinations.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText mFullname,mpassword,mphone,memail;
    Button mregister;
    TextView mlogin;
    FirebaseDatabase database;
    DatabaseReference databaseusers;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseAuth.getInstance().signOut();
        mFullname=findViewById(R.id.name);
        memail=findViewById(R.id.email);
        mpassword=findViewById(R.id.password);
        mphone=findViewById(R.id.phone);
        mregister=findViewById(R.id.registerbtn);
        mlogin=findViewById(R.id.loginhere);

        fAuth=FirebaseAuth.getInstance();
//        String passid=FirebaseAuth.getInstance().getUid();

        databaseusers=FirebaseDatabase.getInstance().getReference("user");

        if(fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email=memail.getText().toString().trim();
                final String password=mpassword.getText().toString().trim();
                final String name=mFullname.getText().toString().trim();
                final String phone=mphone.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    memail.setError("Enter email");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mpassword.setError("Enter password");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String id=databaseusers.push().getKey();
                            User user=new User(name,email,password,phone);
                            databaseusers.child(id).setValue(user);

                            Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Register.this,"Error "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));

            }
        });
    }
}
