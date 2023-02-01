package com.example.appppdbsditibnuabbas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appppdbsditibnuabbas.DetailActivity;
import com.example.appppdbsditibnuabbas.Model.Book;
import com.example.appppdbsditibnuabbas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BookAdapter extends FirebaseRecyclerAdapter<
        Book, BookAdapter.BookViewholder> {
    Context mContext;
    String stJudul, stKategori, stKode, stHal, stDeskripsi, stCover;

    public BookAdapter(
            @NonNull FirebaseRecyclerOptions<Book> options, Context context)
    {
        super(options);
        mContext = context;
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull BookViewholder holder,
                     int position, @NonNull Book model)
    {
        holder.judul.setText(model.getJudul());
        holder.kategori.setText("Kategori : "+model.getKategori());
        holder.kode.setText("Kode buku : "+model.getKode());
        holder.hal.setText("Jumlah hal : "+model.getHal()+" halaman");
        holder.deskripsi.setText(model.getDeskripsi());

        Glide.with(mContext).load(model.getCover()).fitCenter().into(holder.gambar);
        holder.cdProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("judul", model.getJudul());
                intent.putExtra("kategori", model.getKategori());
                intent.putExtra("kode", model.getKode());
                intent.putExtra("cover", model.getCover());
                intent.putExtra("hal", model.getHal());
                intent.putExtra("deskripsi", model.getDeskripsi());
                mContext.startActivity(intent);
                notifyItemRangeChanged(0,getItemCount());
            }
        });
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public BookViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ly_book, parent, false);

        return new BookAdapter.BookViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class BookViewholder
            extends RecyclerView.ViewHolder {
        TextView judul, kategori, kode, hal, deskripsi;
        ImageView gambar;
        CardView cdProduk;
        public BookViewholder(@NonNull View itemView)
        {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            kategori = itemView.findViewById(R.id.kategori);
            kode = itemView.findViewById(R.id.kode);
            hal = itemView.findViewById(R.id.halaman);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            gambar = itemView.findViewById(R.id.img_produk);
            cdProduk = itemView.findViewById(R.id.cd_produk);
        }
    }
}
