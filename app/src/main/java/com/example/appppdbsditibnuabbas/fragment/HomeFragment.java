package com.example.appppdbsditibnuabbas.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appppdbsditibnuabbas.LoginActivity;
import com.example.appppdbsditibnuabbas.Model.Book;
import com.example.appppdbsditibnuabbas.R;
import com.example.appppdbsditibnuabbas.adapter.BookAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences sharedPreferences;
    String nama ="";
    TextView tvNama, tvDataJudul, tvDataText;
    LinearLayout lyData, lyNodata;

    //
    private RecyclerView recyclerView;
    BookAdapter bookAdapter;
    RelativeLayout lyLoading;
    String path = "";
    DatabaseReference mbase;
    Boolean sudahDaftar=false;
    Boolean start=false;
    CardView btnAll, btnIPA, btnIPS, btnMat, btnIndo, btnSby, btnAgama;
    View view;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }
    void init(){
        lyLoading = view.findViewById(R.id.ly_loading);
        btnAll = view.findViewById(R.id.btn_all);
        btnMat = view.findViewById(R.id.btn_mat);
        btnIPA = view.findViewById(R.id.btn_ipa);
        btnIPS = view.findViewById(R.id.btn_ips);
        btnAgama = view.findViewById(R.id.btn_agama);
        btnIndo = view.findViewById(R.id.btn_indo);
        btnSby = view.findViewById(R.id.btn_sby);
        lyLoading.setVisibility(View.VISIBLE);
        //
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nama = sharedPreferences.getString("nama","kosong");
        tvNama = view.findViewById(R.id.tvNama);
        tvNama.setText(nama);
        lyData = view.findViewById(R.id.ly_data);
        lyNodata = view.findViewById(R.id.ly_no_data);
        tvDataJudul = view.findViewById(R.id.tv_data_judul);
        tvDataText=view.findViewById(R.id.tv_data_text);
        init_fb();
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
                        tvDataJudul.setText(friend.child("status").getValue().toString());
                        if(friend.child("status").getValue().toString().equals("pending")){

                        }else{
                            tvDataText.setText("Berhasil di verifikasi");
                        }
                        Log.d("ContactSync","handle number "+firebase_id+" "+"Anak-anak"+" "+friend);
                    }
                    Log.d("ContactSync","handle number outer "+dataSnapshot);
                    //user exist
                    lyData.setVisibility(View.VISIBLE);
                    lyNodata.setVisibility(View.GONE);
                } else {
                    //user_does_not_exist
                    sudahDaftar=false;
                    lyNodata.setVisibility(View.VISIBLE);
                    lyData.setVisibility(View.GONE);
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