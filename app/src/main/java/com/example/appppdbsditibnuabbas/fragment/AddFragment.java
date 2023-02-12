package com.example.appppdbsditibnuabbas.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appppdbsditibnuabbas.HomeActivity;
import com.example.appppdbsditibnuabbas.Model.Book;
import com.example.appppdbsditibnuabbas.Model.Pendaftaran;
import com.example.appppdbsditibnuabbas.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
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
    String path = "";
    DatabaseReference mbase;
    Boolean sudahDaftar=false;
    private String mParam1;
    private String mParam2;
    EditText etNama, etNis,etTtl,etAlamat, etHp, etNamaAyah, etPekerjaanAyah, etNamaIbu, etPekerjaanIbu;
    ImageView imgKip, imgPas;
    Button btnKirim, btnPas, btnKip;
    Boolean imgKipStat, imgPasStat,imgPasGet,imgKipGet;
    Spinner spJk;

    String stNama, StNis, stTtl, stJk, stAlamat, stHp, stNamaAyah, stPekerjaanAyah, stNamaIbu, stPekerjaanIbu;
    private ImagePicker imagePicker = new ImagePicker();
    Uri uriPas, uriKip;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://app-ppdb-sd-it-ibnu-abbas.appspot.com");
    StorageReference storageRef = storage.getReference();
    StorageReference kipPath, pasPath;
    String urlKip, urlPas;
    //
    View view;
    RelativeLayout lyLoading;
    Pendaftaran pendaftaran;
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
        init_fb();
        klik();
        return view;
    }
    void init(){
        lyLoading = view.findViewById(R.id.ly_loading);
        etNama = view.findViewById(R.id.nama);
        etNis = view.findViewById(R.id.nis);
        spJk = view.findViewById(R.id.sp_jk);
        etAlamat = view.findViewById(R.id.alamat);
        etHp  = view.findViewById(R.id.hp);
        etTtl = view.findViewById(R.id.ttl);
        etNamaAyah = view.findViewById(R.id.nama_ayah);
        etPekerjaanAyah = view.findViewById(R.id.pekerjaan_ayah);
        etNamaIbu  = view.findViewById(R.id.nama_ibu);
        etPekerjaanIbu = view.findViewById(R.id.pekerjaan_ibu);
        btnKirim = view.findViewById(R.id.btn_tambah);
        btnPas = view.findViewById(R.id.btn_pas_foto);
        btnKip = view.findViewById(R.id.btn_kip);
        imgPas = view.findViewById(R.id.img_pas_foto);
        imgKip = view.findViewById(R.id.img_kip);
        firebaseDatabase = FirebaseDatabase.getInstance("https://app-ppdb-sd-it-ibnu-abbas-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("data");
    }
    void klik(){
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val();
                if(sudahDaftar){
                    Toast.makeText(getActivity(), "Kamu sudah mendaftar sebelumnya.", Toast.LENGTH_SHORT).show();
                }else{
                    if(cek()){
                        signUp();
                    }else{
                        Toast.makeText(getActivity(), "Pastikan terisi semua.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btnPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ImagePicker.with(getActivity())
                        .cropSquare()
                        .maxResultSize(512, 512,true)
                        .galleryOnly()
                        .createIntent();*/
                imgPasGet=false;
                pickMedia();
                Toast.makeText(getActivity(),"KLIK",Toast.LENGTH_SHORT).show();
            }
        });
        btnKip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ImagePicker.with(getActivity())
                        .cropSquare()
                        .maxResultSize(512, 512,true)
                        .galleryOnly()
                        .createIntent();*/
                imgKipGet=true;
                pickMedia();
                Toast.makeText(getActivity(),"KLIK",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void val(){
        stNama = etNama.getText().toString();
        stTtl = etTtl.getText().toString();
        StNis = etNis.getText().toString();
        stJk = spJk.getSelectedItem().toString();
        stHp = etHp.getText().toString();
        stAlamat = etAlamat.getText().toString();
        stNamaAyah = etNamaAyah.getText().toString();
        stPekerjaanAyah = etPekerjaanAyah.getText().toString();
        stNamaIbu = etNamaIbu.getText().toString();
        stPekerjaanIbu = etPekerjaanIbu.getText().toString();
    }
    boolean cek(){
        if(imgPasStat){
            if(!stNama.equals("") && !stTtl.equals("")&&!stAlamat.equals("")&&!stJk.equals("")&&
                    !stNamaAyah.equals("")&&!stHp.equals("")&&!stPekerjaanAyah.equals("")&&
                    !stNamaIbu.equals("")&&!stPekerjaanIbu.equals("")){
                return true;
            }else {
                return false;
            }
        }else{
            Toast.makeText(getActivity(), "Pas foto tidak boleh kosong.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void signUp() {
        pendaftaran = new Pendaftaran();
        pendaftaran.setNama(stNama);
        pendaftaran.setNIS(StNis);
        pendaftaran.setTtl(stTtl);
        pendaftaran.setAlamat(stAlamat);
        pendaftaran.setHp(stHp);
        pendaftaran.setNama_ayah(stNamaAyah);
        pendaftaran.setPekerjaan_ayah(stPekerjaanAyah);
        pendaftaran.setNama_ibu(stNamaIbu);
        pendaftaran.setPekerjaan_ibu(stPekerjaanIbu);
        pendaftaran.setImg_kip(urlKip);
        pendaftaran.setImg_pas(urlPas);
        pendaftaran.setStatus("pending");
        HashMap map = new HashMap();
        map.put("nama", stNama);
        map.put("nis", StNis);
        map.put("ttl", stTtl);
        map.put("alamat", stAlamat);
        map.put("hp", stHp);
        map.put("nama_ayah", stNamaAyah);
        map.put("pekerjaan_ayah", stPekerjaanAyah);
        map.put("nama_ibu", stNamaIbu);
        map.put("pekerjaan_ibu", stPekerjaanIbu);
        map.put("img_kip", urlKip);
        map.put("img_pas", urlPas);
        map.put("status", "pending");
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
                databaseReference.child(newItemKey).setValue(pendaftaran);
                lyLoading.setVisibility(View.GONE);
                // after adding this data we are showing toast message.
                Toast.makeText(getActivity(), "Data berhasil dikirim.", Toast.LENGTH_SHORT).show();
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                lyLoading.setVisibility(View.GONE);
                // we are displaying a failure toast message.
                Toast.makeText(getActivity(), "Gagal menambahkan data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    if(imgPasGet){
                        uriKip = data.getData();
                        Glide.with(getActivity())
                                .load(uriKip)
                                .centerCrop()
                                .into(imgKip);
                        final String timestamp = "" + System.currentTimeMillis();
                        kipPath=storageRef.child("images/"+etNis.getText().toString()+"/"+timestamp+"_kip.jpg");
                        upload(uriKip,"kip",kipPath);
                    }else{
                        imgPasGet=true;
                        uriPas = data.getData();
                        Glide.with(getActivity())
                                .load(uriPas)
                                .centerCrop()
                                .into(imgPas);
                        final String timestamp = "" + System.currentTimeMillis();
                        pasPath=storageRef.child("images/"+etNis.getText().toString()+"/"+timestamp+"_pas.jpg");
                        upload(uriPas,"pas",pasPath);
                    }
                }else {
                        Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                    }
                });
    private void pickMedia() {
        String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
        ImagePicker.Companion.with(this)
                .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                .galleryOnly()
                .galleryMimeTypes(mimeTypes)
                .crop()
                .compress(768)
                .maxResultSize(800, 800)
                .createIntent(intent -> {
                    startForMediaPickerResult.launch(intent);
                    return null;
                });
    }
    void upload(Uri imgUri,String jenis, StorageReference patt){
        UploadTask uploadTask = patt.putFile(imgUri);
        lyLoading.setVisibility(View.VISIBLE);
        if (jenis.equals("pas")){
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    lyLoading.setVisibility(View.GONE);
                    imgPasStat=true;
                    Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urlPas = uri.toString();
                            // Use the download URL to access the uploaded file
                        }
                    });
                }
            });
        }else{
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    lyLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urlKip = uri.toString();
                            // Use the download URL to access the uploaded file
                        }
                    });
                }
            });
        }
    }
    void init_fb(){
        FirebaseRecyclerOptions<Book> op;
        Query query;
        mbase = FirebaseDatabase.getInstance().getReference("data");
        if (!path.equals("")){
            query = mbase.orderByChild("nis").equalTo(path);
            op
                    = new FirebaseRecyclerOptions.Builder<Book>()
                    .setQuery( query, Book.class)
                    .build();

        }else{
            query = mbase;
            op
                    = new FirebaseRecyclerOptions.Builder<Book>()
                    .setQuery(query, Book.class)
                    .build();
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                }
                if(dataSnapshot != null){
                    sudahDaftar=true;
                    for(DataSnapshot friend: dataSnapshot.getChildren()){
                        String firebase_id = (String) friend.getKey();
                        btnKirim.setText("Sudah kirim data");
                        Log.d("ContactSync","handle number "+firebase_id+" "+"Anak-anak"+" "+friend);
                    }
                    Log.d("ContactSync","handle number outer "+dataSnapshot);
                    //user exist

                } else {
                    //user_does_not_exist
                    sudahDaftar=false;

                }
                lyLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ContactSync","handle number oncancel "+databaseError.getMessage());
            }
        });
        lyLoading.setVisibility(View.GONE);
    }
}