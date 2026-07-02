package com.example.siakad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.model.KhsModel;

import java.util.List;

public class KhsAdapter extends RecyclerView.Adapter<KhsAdapter.KhsViewHolder> {

    private final Context       context;
    private final List<KhsModel> listKhs;
    private OnItemClickListener onItemClickListener;

    public KhsAdapter(Context context, List<KhsModel> listKhs) {
        this.context = context;
        this.listKhs = listKhs;
    }

    public interface OnItemClickListener {
        void onItemClick(KhsModel item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public KhsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_khs, parent, false);
        return new KhsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhsViewHolder holder, int position) {
        KhsModel item = listKhs.get(position);

        holder.tvNamaMatkul.setText(item.getNamaMatkul());
        holder.tvKode.setText(item.getKodeMatkul());
        holder.tvSks.setText(item.getSks() + " SKS");
        holder.tvNilaiHuruf.setText(item.getNilaiHuruf());
        holder.tvNilaiAngka.setText(String.format("%.2f", item.getNilaiAngka()));

        int warnaNilai = getWarnaNilai(item.getNilaiHuruf());
        holder.tvNilaiHuruf.getBackground().setTint(
                ContextCompat.getColor(context, warnaNilai)
        );
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (onItemClickListener != null &&
                    adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(item, adapterPosition);
            }
        });
    }

    private int getWarnaNilai(String nilaiHuruf) {
        switch (nilaiHuruf) {
            case "A":
            case "A-":
                return R.color.uap_success;
            case "B+":
            case "B":
                return R.color.uap_primary;
            case "B-":
            case "C+":
                return R.color.uap_info;
            case "C":
                return R.color.uap_warning;
            default:
                return R.color.uap_error;
        }
    }

    @Override
    public int getItemCount() {
        return listKhs.size();
    }

    static class KhsViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMatkul, tvKode, tvSks;
        TextView tvNilaiHuruf, tvNilaiAngka;

        KhsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMatkul = itemView.findViewById(R.id.tvNamaMatkulKhs);
            tvKode       = itemView.findViewById(R.id.tvKodeKhs);
            tvSks        = itemView.findViewById(R.id.tvSksKhs);
            tvNilaiHuruf = itemView.findViewById(R.id.tvNilaiHuruf);
            tvNilaiAngka = itemView.findViewById(R.id.tvNilaiAngka);
        }
    }
}
