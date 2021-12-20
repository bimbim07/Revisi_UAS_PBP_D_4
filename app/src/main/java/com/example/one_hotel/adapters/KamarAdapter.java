package com.example.one_hotel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.one_hotel.R;
import com.example.one_hotel.activities.KamarActivity;
import com.example.one_hotel.activities.MainActivity;
import com.example.one_hotel.databinding.ItemKamarBinding;
import com.example.one_hotel.models.Kamar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class KamarAdapter extends RecyclerView.Adapter<KamarAdapter.ViewHolder> implements Filterable
{
    private ItemKamarBinding binding;
    private List<Kamar> kamarList;
    private List<Kamar> filteredKamarList;
    private Context context;
    private Kamar kamar;

    public KamarAdapter(List<Kamar> kamarList, Context context)
    {
        this.kamarList = kamarList;
        this.context = context;

        filteredKamarList = new ArrayList<>(kamarList);

    }

    @NonNull
    @Override
    public KamarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_kamar,parent,false);
        return new KamarAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull KamarAdapter.ViewHolder holder, int position)
    {
        kamar = filteredKamarList.get(position);

        holder.tvNama.setText(kamar.getNama());
        holder.tvJenis.setText(kamar.getJenis());
        holder.tvHarga.setText(new DecimalFormat("############").format(kamar.getHarga()));
        holder.tvDeskripsi.setText(kamar.getDeskripsi());

        Glide.with(context).load(kamar.getUrlfoto()).placeholder(R.drawable.loading).error(R.drawable.noimage).into(holder.ivFoto);

        holder.flRoot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                kamar = filteredKamarList.get(position);

                if(context instanceof KamarActivity)
                {
                    String id = String.valueOf(kamar.getId());
                    String nama = kamar.getNama();
                    String jenis = String.valueOf(kamar.getJenis());
                    String harga = String.valueOf(kamar.getHarga());
                    String deskripsi = kamar.getDeskripsi();
                    String urlfoto = kamar.getUrlfoto();

                    ((KamarActivity) context).selectKamar(id,nama,jenis,harga,deskripsi,urlfoto);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return filteredKamarList.size();
    }

    public void setKamarList(List<Kamar> kamarList)
    {
        this.kamarList = kamarList;
        filteredKamarList = new ArrayList<>(kamarList);
    }

    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charSequenceString = charSequence.toString();
                List<Kamar> filtered = new ArrayList<>();

                if (charSequenceString.isEmpty())
                {
                    filtered.addAll(kamarList);
                }
                else
                {
                    for (Kamar kamar : kamarList)
                    {
                        if (kamar.getNama().toLowerCase().contains(charSequenceString.toLowerCase()))
                        {
                            filtered.add(kamar);
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
                filteredKamarList.clear();
                filteredKamarList.addAll((List<Kamar>) filterResults.values);

                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private FrameLayout flRoot;
        private TextView tvNama;
        private TextView tvJenis;
        private TextView tvHarga;
        private TextView tvDeskripsi;
        private ImageView ivFoto;

        public ViewHolder(@NonNull ItemKamarBinding binding)
        {
            super(binding.getRoot());

            flRoot = binding.flRoot;
            tvNama = binding.tvNama;
            tvJenis = binding.tvJenis;
            tvHarga = binding.tvHarga;
            tvDeskripsi = binding.tvDeskripsi;
            ivFoto = binding.ivFoto;
        }
    }
}