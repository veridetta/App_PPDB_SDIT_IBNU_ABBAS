package com.example.appppdbsditibnuabbas.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.appppdbsditibnuabbas.Model.Borrow;
import com.example.appppdbsditibnuabbas.R;
import com.example.appppdbsditibnuabbas.adapter.BorrowAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BorrowListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BorrowListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    BorrowAdapter borrowAdapter;
    DatabaseReference mbase;
    RelativeLayout lyLoading;
    Boolean start=false;
    View view;
    public BorrowListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BorrowListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BorrowListFragment newInstance(String param1, String param2) {
        BorrowListFragment fragment = new BorrowListFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_borrow_list, container, false);
        init();
        init_fb();
        return view;
    }
    void init(){
        lyLoading = view.findViewById(R.id.ly_loading);
        lyLoading.setVisibility(View.VISIBLE);
    }
    void init_fb(){
        FirebaseRecyclerOptions<Borrow> op;
        Query query;
        mbase = FirebaseDatabase.getInstance().getReference("borrow");
            query = mbase;
            op
                    = new FirebaseRecyclerOptions.Builder<Borrow>()
                    .setQuery(query, Borrow.class)
                    .build();
        if (start){
            borrowAdapter.stopListening();
            borrowAdapter.updateOptions(op);
        }
        borrowAdapter = new BorrowAdapter(op, getActivity());
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
        borrowAdapter.startListening();
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
        recyclerView.setAdapter(borrowAdapter);
        start=true;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        borrowAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop()
    {
        super.onStop();
        borrowAdapter.stopListening();
    }
}