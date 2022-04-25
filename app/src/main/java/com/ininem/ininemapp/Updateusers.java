package com.ininem.ininemapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Updateusers extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextBrand;
    private EditText editTextDesc;
    private EditText editTextPrice;
    private EditText editTextQty;

    private FirebaseFirestore db;

    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateusers);

        product = (Product) getIntent().getSerializableExtra("product");
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.edittext_name);
        editTextBrand = findViewById(R.id.edittext_brand);
        editTextDesc = findViewById(R.id.edittext_desc);

        editTextName.setText(product.getNombre_completo());
        editTextBrand.setText(product.getCorreo_electronico());
        editTextDesc.setText(product.getIsUser());



        findViewById(R.id.button_update).setOnClickListener(this);
        findViewById(R.id.button_delete).setOnClickListener(this);
        findViewById(R.id.regresar).setOnClickListener(this);
    }

    private boolean hasValidationErrors(String Nombre_completo, String Correo_electronico, String isUser) {
        if (Nombre_completo.isEmpty()) {
            editTextName.setError("ingrese un nombre");
            editTextName.requestFocus();
            return true;
        }

        if (Correo_electronico.isEmpty()) {
            editTextBrand.setError("ingrese un correo");
            editTextBrand.requestFocus();
            return true;
        }

        if (isUser.isEmpty()) {
            editTextDesc.setError("ingrese un valor");
            editTextDesc.requestFocus();
            return true;
        }


        return false;
    }


    private void updateProduct() {
        String Nombre_completo = editTextName.getText().toString().trim();
        String Correo_electronico = editTextBrand.getText().toString().trim();
        String isUser = editTextDesc.getText().toString().trim();


        if (!hasValidationErrors(Nombre_completo, Correo_electronico, isUser)) {

            Product p = new Product(
                    Nombre_completo,Correo_electronico,isUser
            );


            db.collection("users").document(product.getId())
                    .update(
                            "Correo_electronico", p.getCorreo_electronico(),
                            "isUser", p.getIsUser(),
                            "Nombre_completo", p.getNombre_completo()

                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Updateusers.this, "Product Updated", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void deleteProduct() {
        db.collection("users").document(product.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Updateusers.this, "Product deleted", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(Updateusers.this, Admin.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_update:
                updateProduct();
                break;
            case R.id.button_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure about this?");
                builder.setMessage("Deletion is permanent...");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();

                break;
            case R.id.regresar:
                startActivity(new Intent(getApplicationContext(),Admin.class));
                break;
        }
    }
}
