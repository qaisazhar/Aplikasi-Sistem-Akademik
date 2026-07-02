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
import com.example.siakad.model.NotifikasiModel;

import java.util.List;

public class NotifikasiAdapter extends
        RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder> {

    private final Context                context;
    private final List<NotifikasiModel>  listNotifikasi;

    public interface OnItemClickListener {
        void onItemClick(NotifikasiModel item, int position);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NotifikasiAdapter(Context context, List<NotifikasiModel> listNotifikasi) {
        this.context        = context;
        this.listNotifikasi = listNotifikasi;
    }

    @NonNull
    @Override
    public NotifikasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false);
        return new NotifikasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiViewHolder holder, int position) {
        NotifikasiModel item = listNotifikasi.get(position);

        holder.tvJudul.setText(item.getJudul());
        holder.tvKonten.setText(item.getKonten());
        holder.tvTanggal.setText(item.getTanggal());
        holder.tvKategori.setText(item.getKategori());

        holder.ivIcon.setImageResource(item.getIkonResId());
        holder.ivIcon.setColorFilter(
                ContextCompat.getColor(context, item.getIkonWarna())
        );

        holder.dotBelumDibaca.setVisibility(
                item.isSudahDibaca() ? View.INVISIBLE : View.VISIBLE
        );

        holder.tvJudul.setAlpha(item.isSudahDibaca() ? 0.6f : 1.0f);

        holder.itemView.setOnClickListener(v -> {
            if (!item.isSudahDibaca()) {
                item.setSudahDibaca(true);
                notifyItemChanged(position);
            }
            if (listener != null) {
                listener.onItemClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotifikasi.size();
    }

    static class NotifikasiViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView  tvJudul, tvKonten, tvTanggal, tvKategori;
        View      dotBelumDibaca;

        NotifikasiViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon          = itemView.findViewById(R.id.ivKategoriIcon);
            tvJudul         = itemView.findViewById(R.id.tvJudulNotif);
            tvKonten        = itemView.findViewById(R.id.tvKontenNotif);
            tvTanggal       = itemView.findViewById(R.id.tvTanggal);
            tvKategori      = itemView.findViewById(R.id.tvKategori);
            dotBelumDibaca  = itemView.findViewById(R.id.dotBelumDibaca);
        }
    }
}