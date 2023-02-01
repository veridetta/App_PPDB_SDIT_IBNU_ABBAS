package com.example.appppdbsditibnuabbas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appppdbsditibnuabbas.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    RelativeLayout lyLoading;
    EditText nama, email, nis, password;
    Button btnRegister, btnLogin;
    String stNama, stEmail, stNis, stPassword, kombin;
    Users users;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        klik();
    }

    void init(){
        getSupportActionBar().hide();
        lyLoading = findViewById(R.id.ly_loading);
        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        nis = findViewById(R.id.nis);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        firebaseDatabase = FirebaseDatabase.getInstance("https://app-ppdb-sd-it-ibnu-abbas-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("users");
    }
    void klik(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val();
                if(cek(stNama, stEmail, stPassword, stNis)){
                    signUp(stNama, stEmail, stPassword, stNis);
                }else{
                Toast.makeText(SignUpActivity.this, "Pastikan terisi semua.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void val(){
        stNama = nama.getText().toString();
        stEmail = email.getText().toString();
        stNis = nis.getText().toString();
        stPassword = password.getText().toString();
        kombin = stEmail+" "+stPassword;
    }
    boolean cek(String cNama, String cEmail, String cPass, String cNis){
        if(!cNama.equals("") && !cEmail.equals("")&&!cPass.equals("")&&!cNis.equals("")){
            return true;
        }else {
            return false;
        }
    }
    private void signUp(String fNama, String fEmail, String fPass, String fNis) {
        users = new Users();
        users.setNama(fNama);
        users.setEmail(fEmail);
        users.setPassword(fPass);
        users.setNis(fNis);
        users.setKombin(kombin);
        HashMap map = new HashMap();
        map.put("nama", fNama);
        map.put("email", fEmail);
        map.put("password", fPass);
        map.put("nis", fNis);
        map.put("kombin", kombin);
        String newItemKey = databaseReference.push().getKey();
        lyLoading.setVisibility(View.VISIBLE);
        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.child(newItemKey).setValue(users);
                lyLoading.setVisibility(View.GONE);
                // after adding this data we are showing toast message.
                Toast.makeText(SignUpActivity.this, "pendaftaran berhasil, silahkan login untuk melanjutkan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                lyLoading.setVisibility(View.GONE);
                // we are displaying a failure toast message.
                Toast.makeText(SignUpActivity.this, "Pendaftaran gagal " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}