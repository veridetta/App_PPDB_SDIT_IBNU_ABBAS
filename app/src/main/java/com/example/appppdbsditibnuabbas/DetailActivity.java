package com.example.appppdbsditibnuabbas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appppdbsditibnuabbas.Model.Borrow;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    TextView judul, kategori, kode, hal, deskripsi;
    EditText nama, durasi;
    ImageView img;
    Button btnCart;
    String stJudul, stKategori, stKode, stHal, stDeskripsi, stCover, stNama, stDurasi;
    Bundle bundle;
    Borrow borrow;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RelativeLayout lyLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        klik();
    }
    void init(){
        getSupportActionBar().hide();
        judul = findViewById(R.id.tv_judul);
        kategori = findViewById(R.id.kategori);
        kode = findViewById(R.id.kode);
        hal = findViewById(R.id.halaman);
        deskripsi = findViewById(R.id.deskripsi);
        nama = findViewById(R.id.nama);
        durasi = findViewById(R.id.durasi);
        img = findViewById(R.id.img_cover);
        lyLoading = findViewById(R.id.ly_loading);
        btnCart = findViewById(R.id.btn_pinjam);
        firebaseDatabase = FirebaseDatabase.getInstance("https://appperpustakaansmpn1rembang-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("borrow");
        bundle = getIntent().getExtras();
        stJudul = bundle.getString("judul");
        stKategori = bundle.getString("kategori");
        stKode = bundle.getString("kode");
        stCover = bundle.getString("cover");
        stDeskripsi = bundle.getString("deskripsi");
        stHal = bundle.getString("hal");

        judul.setText(stJudul);
        kategori.setText("Kategori : "+stKategori);
        kode.setText("Kode buku : "+stKode);
        deskripsi.setText(stDeskripsi);
        hal.setText("Halaman : "+stHal+" Halaman");
        Glide.with(DetailActivity.this).load(stCover).centerCrop().into(img);
    }
    void klik(){
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val();
                if(cek()){
                    signUp();
                }else{
                    Toast.makeText(DetailActivity.this, "Pastikan terisi semua.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void val(){
        stNama = nama.getText().toString();
        stDurasi = durasi.getText().toString();
    }
    boolean cek(){
        if(!stNama.equals("") && !stDurasi.equals("")){
            return true;
        }else {
            return false;
        }
    }
    private void signUp() {
        borrow = new Borrow();
        borrow.setJudul(stJudul);
        borrow.setKategori(stKategori);
        borrow.setKode(stKode);
        borrow.setHal(stHal);
        borrow.setDurasi(stDurasi);
        borrow.setCover(stCover);
        borrow.setPeminjam(stNama);

        HashMap map = new HashMap();
        map.put("judul", stJudul);
        map.put("kategori", stKategori);
        map.put("kode", stKode);
        map.put("hal", stHal);
        map.put("durasi", stDurasi);
        map.put("cover", stCover);
        map.put("peminjam", stNama);

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
                databaseReference.child(newItemKey).setValue(borrow);
                lyLoading.setVisibility(View.GONE);
                // after adding this data we are showing toast message.
                Toast.makeText(DetailActivity.this, "Berhasil menambahkan peminjam", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                lyLoading.setVisibility(View.GONE);
                // we are displaying a failure toast message.
                Toast.makeText(DetailActivity.this, "Gagal ditambahkan " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}