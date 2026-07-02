package com.example.siakad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.model.InformasiModel;

import java.util.List;

public class InformasiAdapter extends
        RecyclerView.Adapter<InformasiAdapter.InformasiViewHolder> {

    private final Context              context;
    private List<InformasiModel>       listInformasi;

    public interface OnItemClickListener {
        void onItemClick(InformasiModel item, int position);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public InformasiAdapter(Context context, List<InformasiModel> listInformasi) {
        this.context       = context;
        this.listInformasi = listInformasi;
    }

    public void updateData(List<InformasiModel> data) {
        this.listInformasi = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InformasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                  int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_informasi, parent, false);
        return new InformasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformasiViewHolder holder, int position) {
        InformasiModel item = listInformasi.get(position);

        holder.tvJudul.setText(item.getJudul());
        holder.tvIsi.setText(item.getIsi());
        holder.tvTanggal.setText(item.getTanggal());
        holder.tvKategori.setText(item.getKategori());

        holder.ivIcon.setImageResource(item.getIconResId());
        holder.ivIcon.setColorFilter(
                ContextCompat.getColor(context, item.getIconWarna())
        );

        holder.dotBaru.setVisibility(
                item.isBaru() ? View.VISIBLE : View.INVISIBLE
        );

        holder.tvJudul.setAlpha(item.isBaru() ? 1.0f : 0.6f);

        holder.itemView.setOnClickListener(v -> {
            if (item.isBaru()) {
                item.setBaru(false);
                notifyItemChanged(position);
            }
            if (listener != null) {
                listener.onItemClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listInformasi.size();
    }

    static class InformasiViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView  tvJudul, tvIsi, tvTanggal, tvKategori;
        View      dotBaru;

        InformasiViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon     = itemView.findViewById(R.id.ivIconInfo);
            tvJudul    = itemView.findViewById(R.id.tvJudulInfo);
            tvIsi      = itemView.findViewById(R.id.tvIsiInfo);
            tvTanggal  = itemView.findViewById(R.id.tvTanggalInfo);
            tvKategori = itemView.findViewById(R.id.tvKategoriInfo);
            dotBaru    = itemView.findViewById(R.id.dotBaru);
        }
    }
}