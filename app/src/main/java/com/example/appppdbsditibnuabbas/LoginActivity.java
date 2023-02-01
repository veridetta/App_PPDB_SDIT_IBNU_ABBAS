package com.example.appppdbsditibnuabbas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.appppdbsditibnuabbas.Model.Users;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout lyLoading;
    EditText email, password;
    Button btnRegister, btnLogin;
    String  stEmail,  stPassword;
    Users users;
    String kombin="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        klik();
    }

    void init(){
        getSupportActionBar().hide();
        lyLoading = findViewById(R.id.ly_loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        firebaseDatabase = FirebaseDatabase.getInstance("https://app-ppdb-sd-it-ibnu-abbas-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("data");
    }
    void klik(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val();
                if(cek()){
                    login();
                }else{
                    Toast.makeText(LoginActivity.this, "Pastikan terisi semua.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void val(){
        stEmail = email.getText().toString();
        stPassword = password.getText().toString();
        kombin = stEmail+" "+stPassword;
    }
    boolean cek(){
        if( !stEmail.equals("")&&!stPassword.equals("")){
            return true;
        }else {
            return false;
        }
    }
    void login(){
        FirebaseRecyclerOptions<Users> op, priaOptions;
        Query query;
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        query = databaseReference.orderByChild("kombin").equalTo(kombin);
        op = new FirebaseRecyclerOptions.Builder<Users>()
                    .setQuery( query, Users.class)
                    .build();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot friend: dataSnapshot.getChildren()){
                        String firebase_id = (String) friend.getKey();
                        Users users = friend.getValue(Users.class);
                        Log.d("ContactSync","handle number "+firebase_id+" "+"Anak-anak"+" "+friend);
                        Toast.makeText(LoginActivity.this,"Login berhasil.",Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this.getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("login",true);
                        editor.putString("nama",users.getNama());
                        editor.putString("email", users.getEmail());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(LoginActivity.this,"Periksa kembali, email atau password salah",Toast.LENGTH_SHORT).show();
                    Log.d("ContactSync","handle number outer "+dataSnapshot);
                }
                else {
                    //user_does_not_exist
                    Toast.makeText(LoginActivity.this,"Periksa kembali, email atau password salah",Toast.LENGTH_SHORT).show();
                }
                lyLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ContactSync","handle number oncancel "+databaseError.getMessage());
            }
        });
    }
}