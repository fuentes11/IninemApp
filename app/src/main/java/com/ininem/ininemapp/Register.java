package com.ininem.ininemapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    CheckBox USER,ADMIN;
    boolean valid = true;
    FirebaseFirestoreSettings settings;
    FirebaseAuthSettings settings1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);
        USER = findViewById(R.id.isStudent);

        settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        fStore.setFirestoreSettings(settings);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(fullName);
                checkField(email);
                checkField(password);

                if (!(USER.isChecked())){
                    Toast.makeText(Register.this, "Seleccione un tipo de cuenta", Toast.LENGTH_SHORT).show();

                }
                if(valid){
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user =fAuth.getCurrentUser();

                                    Toast.makeText(Register.this, "cuenta creada", Toast.LENGTH_SHORT).show();
                                    DocumentReference df= fStore.collection("users").document(user.getUid());
                                    Map<String,Object> userInfo=new HashMap<>();
                                    userInfo.put("Nombre_completo",fullName.getText().toString());
                                    userInfo.put("Correo_electronico",email.getText().toString());
                                    if (USER.isChecked()){
                                        userInfo.put("isUSer","1");

                                    }
                                    df.set(userInfo);

                                    if (USER.isChecked()){
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    }
                                }
                            }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}