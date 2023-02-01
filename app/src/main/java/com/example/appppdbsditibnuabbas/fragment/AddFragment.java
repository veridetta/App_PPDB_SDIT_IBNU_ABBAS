package com.example.appppdbsditibnuabbas.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appppdbsditibnuabbas.Model.Book;
import com.example.appppdbsditibnuabbas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    RelativeLayout lyLoading;
    EditText judul,  kode, hal,deskripsi, cover;
    Spinner kategori;
    Button btnAdd;
    String stJudul, stKategori, stKode, stHal, stDeskripsi, stCover;
    Book book;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add,container, false);
        init();
        klik();
        return view;
    }
    void init(){
        lyLoading = view.findViewById(R.id.ly_loading);
        judul = view.findViewById(R.id.judul);
        kategori = view.findViewById(R.id.sp_kat);
        deskripsi = view.findViewById(R.id.des);
        hal = view.findViewById(R.id.halaman);
        kode = view.findViewById(R.id.kode);
        cover = view.findViewById(R.id.cover);
        btnAdd = view.findViewById(R.id.btn_tambah);
        firebaseDatabase = FirebaseDatabase.getInstance("https://appperpustakaansmpn1rembang-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("book");
    }
    void klik(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val();
                if(cek()){
                    signUp();
                }else{
                    Toast.makeText(getActivity(), "Pastikan terisi semua.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void val(){
        stJudul = judul.getText().toString();
        stKategori = kategori.getSelectedItem().toString();
        stKode = kode.getText().toString();
        stDeskripsi = deskripsi.getText().toString();
        stCover = cover.getText().toString();
        stHal = hal.getText().toString();
    }
    boolean cek(){
        if(!stJudul.equals("") && !stKode.equals("")&&!stKategori.equals("")&&!stCover.equals("")&&!stDeskripsi.equals("")&&!stHal.equals("")){
            return true;
        }else {
            return false;
        }
    }
    private void signUp() {
        book = new Book();
        book.setJudul(stJudul);
        book.setKategori(stKategori);
        book.setKode(stKode);
        book.setCover(stCover);
        book.setDeskripsi(stDeskripsi);
        book.setHal(stHal);
        HashMap map = new HashMap();
        map.put("judul", stJudul);
        map.put("kategori", stKategori);
        map.put("kode", stKode);
        map.put("cover", stCover);
        map.put("deskripsi", stDeskripsi);
        map.put("hal", stHal);
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
                databaseReference.child(newItemKey).setValue(book);
                lyLoading.setVisibility(View.GONE);
                // after adding this data we are showing toast message.
                Toast.makeText(getActivity(), "Buku berhasil ditambah.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                lyLoading.setVisibility(View.GONE);
                // we are displaying a failure toast message.
                Toast.makeText(getActivity(), "Gagal menambahkan buku " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}