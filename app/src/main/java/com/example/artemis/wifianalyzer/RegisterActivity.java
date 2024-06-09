package com.example.artemis.wifianalyzer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword,enroll,year,branch;
    private ProgressDialog pDialog;
    RegisterActivity r;
    String IMEI ="";
   // private FirebaseDatabase auth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        r = this;
       // auth = FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com");

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(new GeneratorActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 110);
        }
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    DatabaseReference reff = FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("name");
                    reff.setValue(name);

                     reff = FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("email");
                    reff.setValue("91"+email);

                     reff = FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("password");
                    reff.setValue(password);

                    reff = FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("phoneNo");
                    reff.setValue("register");
                    reff = FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("i");
                    reff.setValue(1);
                    startActivity(new Intent(r,MainActivity.class));

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });



    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
