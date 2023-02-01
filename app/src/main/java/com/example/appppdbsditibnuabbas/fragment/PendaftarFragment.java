package com.example.appppdbsditibnuabbas.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * Use the {@link PendaftarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendaftarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    BookAdapter bookAdapter;
    DatabaseReference mbase;
    RelativeLayout lyLoading;
    String path = "";
    Boolean start=false;
    CardView btnAll, btnIPA, btnIPS, btnMat, btnIndo, btnSby, btnAgama;
    View view;
    public PendaftarFragment() {
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
    public static PendaftarFragment newInstance(String param1, String param2) {
        PendaftarFragment fragment = new PendaftarFragment();
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
        init_fb();
        klik();
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
    }
    void klik(){
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter(btnAll);
                path="";
                lyLoading.setVisibility(View.VISIBLE);
                init_fb();
            }
        });
        btnMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapter.stopListening();
                filter(btnMat);
                path="Matematika";
                lyLoading.setVisibility(View.VISIBLE);
                init_fb();
            }
        });
        btnIPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapter.stopListening();
                filter(btnIPS);
                path="IPS";
                lyLoading.setVisibility(View.VISIBLE);
                init_fb();
            }
        });
        btnIPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapter.stopListening();
                filter(btnIPA);
                path="IPA";
                lyLoading.setVisibility(View.VISIBLE);
                init_fb();
            }
        });
        btnAgama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapter.stopListening();
                filter(btnAgama);
                path="Pendidikan Agama";
                lyLoading.setVisibility(View.VISIBLE);
                init_fb();
            }
        });
        btnIndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapter.stopListening();
                filter(btnIndo);
                path="Bahasa Indonesia";
                lyLoading.setVisibility(View.VISIBLE);
                init_fb();
            }
        });
        btnSby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapter.stopListening();
                filter(btnSby);
                path="Seni Budaya";
                lyLoading.setVisibility(View.VISIBLE);
                init_fb();
            }
        });
    }
    void init_fb(){
        FirebaseRecyclerOptions<Book> op;
        Query query;
        mbase = FirebaseDatabase.getInstance().getReference("book");
        if (!path.equals("")){
            query = mbase.orderByChild("kategori").equalTo(path);
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
        if (start){
            bookAdapter.stopListening();
            bookAdapter.updateOptions(op);
        }
        bookAdapter = new BookAdapter(op, getActivity());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                }
                if(dataSnapshot != null){
                    for(DataSnapshot friend: dataSnapshot.getChildren()){
                        String firebase_id = (String) friend.getKey();
                        Log.d("ContactSync","handle number "+firebase_id+" "+"Anak-anak"+" "+friend);
                    }
                    Log.d("ContactSync","handle number outer "+dataSnapshot);
                    //user exist
                }
                else {
                    //user_does_not_exist
                }
                lyLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ContactSync","handle number oncancel "+databaseError.getMessage());
            }
        });
        bookAdapter.startListening();
        recyclerView = view.findViewById(R.id.rc_book);
        recyclerView.setItemAnimator(null);
        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(),1));
        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data

        // Connecting object of required Adapter class to
        // the Adapter class itself
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(bookAdapter);
        start=true;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        bookAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop()
    {
        super.onStop();
        bookAdapter.stopListening();
    }
    void filter(CardView cardView){
        btnAll.setCardBackgroundColor(getResources().getColor(R.color.blue_200));
        btnMat.setCardBackgroundColor(getResources().getColor(R.color.blue_200));
        btnIPA.setCardBackgroundColor(getResources().getColor(R.color.blue_200));
        btnIPS.setCardBackgroundColor(getResources().getColor(R.color.blue_200));
        btnAgama.setCardBackgroundColor(getResources().getColor(R.color.blue_200));
        btnIndo.setCardBackgroundColor(getResources().getColor(R.color.blue_200));
        btnSby.setCardBackgroundColor(getResources().getColor(R.color.blue_200));
        cardView.setCardBackgroundColor(getResources().getColor(R.color.blue_800));
    }
}