package com.example.appppdbsditibnuabbas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appppdbsditibnuabbas.Model.Borrow;
import com.example.appppdbsditibnuabbas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BorrowAdapter extends FirebaseRecyclerAdapter<
        Borrow, BorrowAdapter.BorrowViewholder> {
    Context mContext;
    String stJudul, stKategori, stKode, stHal, stDeskripsi, stCover, stPeminjam, stDurasi;

    public BorrowAdapter(
            @NonNull FirebaseRecyclerOptions<Borrow> options, Context context)
    {
        super(options);
        mContext = context;
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull BorrowViewholder holder,
                     int position, @NonNull Borrow model)
    {
        holder.judul.setText(model.getJudul());
        holder.kategori.setText("Kategori : "+model.getKategori());
        holder.kode.setText("Kode buku : "+model.getKode());
        holder.hal.setText("Jumlah hal : "+model.getHal()+" halaman");
        holder.hal.setText("Peminjam : "+model.getPeminjam());
        holder.hal.setText("Durasi : "+model.getHal()+" hari");

        Glide.with(mContext).load(model.getCover()).fitCenter().into(holder.gambar);
        holder.cdProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public BorrowViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ly_borrow, parent, false);

        return new BorrowAdapter.BorrowViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class BorrowViewholder
            extends RecyclerView.ViewHolder {
        TextView judul, kategori, kode, hal, peminjam, durasi;
        ImageView gambar;
        CardView cdProduk;
        public BorrowViewholder(@NonNull View itemView)
        {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            kategori = itemView.findViewById(R.id.kategori);
            kode = itemView.findViewById(R.id.kode);
            hal = itemView.findViewById(R.id.halaman);
            peminjam = itemView.findViewById(R.id.deskripsi);
            durasi = itemView.findViewById(R.id.deskripsi);
            gambar = itemView.findViewById(R.id.img_produk);
            cdProduk = itemView.findViewById(R.id.cd_produk);
        }
    }
}
