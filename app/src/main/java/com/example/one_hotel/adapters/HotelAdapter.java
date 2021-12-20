package com.example.one_hotel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.one_hotel.R;
import com.example.one_hotel.activities.HotelActivity;
import com.example.one_hotel.activities.KamarActivity;
import com.example.one_hotel.activities.MainActivity;
import com.example.one_hotel.activities.PaymentActivity;
import com.example.one_hotel.databinding.ItemHotelBinding;
import com.example.one_hotel.models.Hotel;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> implements Filterable
{
    private ItemHotelBinding binding;
    private List<Hotel> hotelList;
    private List<Hotel> filteredHotelList;
    private Context context;
    private Hotel hotel;

    public HotelAdapter(List<Hotel> hotelList, Context context)
    {
        this.hotelList = hotelList;
        this.context = context;

        filteredHotelList = new ArrayList<>(hotelList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_hotel,parent,false);
        return new HotelAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        hotel = filteredHotelList.get(position);

        holder.tvNama.setText(hotel.getNama());
        holder.tvRating.setText(new DecimalFormat("############").format(hotel.getRating()));
        holder.tvLokasi.setText(hotel.getLokasi());

        Glide.with(context).load(hotel.getUrlfoto()).placeholder(R.drawable.loading).error(R.drawable.noimage).into(holder.ivFoto);

        holder.flRoot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                hotel = filteredHotelList.get(position);

                if(context instanceof MainActivity)
                {
                    String id = String.valueOf(hotel.getId());
                    String nama = hotel.getNama();
                    String rating = String.valueOf(hotel.getRating());
                    String lokasi = hotel.getLokasi();
                    String urlfoto = hotel.getUrlfoto();

                    ((MainActivity) context).selectHotel(id,nama,rating,lokasi,urlfoto);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return filteredHotelList.size();
    }

    public void setHotelList(List<Hotel> hotelList)
    {
        this.hotelList = hotelList;
        filteredHotelList = new ArrayList<>(hotelList);
    }

    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charSequenceString = charSequence.toString();
                List<Hotel> filtered = new ArrayList<>();

                if (charSequenceString.isEmpty())
                {
                    filtered.addAll(hotelList);
                }
                else
                {
                    for (Hotel hotel : hotelList)
                    {
                        if (hotel.getNama().toLowerCase().contains(charSequenceString.toLowerCase()))
                        {
                            filtered.add(hotel);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredHotelList.clear();
                filteredHotelList.addAll((List<Hotel>) filterResults.values);

                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private FrameLayout flRoot;
        private TextView tvNama;
        private TextView tvRating;
        private TextView tvLokasi;
        private ImageView ivFoto;

        public ViewHolder(@NonNull ItemHotelBinding binding)
        {
            super(binding.getRoot());

            flRoot = binding.flRoot;
            tvNama = binding.tvNama;
            tvRating = binding.tvRating;
            tvLokasi = binding.tvLokasi;
            ivFoto = binding.ivFoto;
        }
    }
}