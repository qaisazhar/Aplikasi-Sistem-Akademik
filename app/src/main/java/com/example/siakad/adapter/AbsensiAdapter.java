package com.example.siakad.adapter;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.model.AbsensiModel;

import java.util.List;

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.AbsensiViewHolder> {

    private final Context            context;
    private final List<AbsensiModel> listAbsensi;
    private OnItemClickListener      onItemClickListener;

    public AbsensiAdapter(Context context, List<AbsensiModel> listAbsensi) {
        this.context     = context;
        this.listAbsensi = listAbsensi;
    }

    public interface OnItemClickListener {
        void onItemClick(AbsensiModel item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public AbsensiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_absensi, parent, false);
        return new AbsensiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsensiViewHolder holder, int position) {
        AbsensiModel item = listAbsensi.get(position);

        holder.tvNamaMatkul.setText(item.getNamaMatkul());
        holder.tvKode.setText(item.getKodeMatkul());
        holder.tvJmlHadir.setText(String.valueOf(item.getHadir()));
        holder.tvJmlIzin.setText(String.valueOf(item.getIzin()));
        holder.tvJmlSakit.setText(String.valueOf(item.getSakit()));
        holder.tvJmlAlpa.setText(String.valueOf(item.getAlpa()));
        holder.tvJmlTotal.setText(String.valueOf(item.getTotalPertemuan()));

        int persen = item.getPersenKehadiran();
        holder.progressAbsensi.setProgress(persen);
        holder.tvPersen.setText(persen + "%");

        int warnaProgress = item.isAman()
                ? R.color.uap_success
                : R.color.uap_error;
        holder.progressAbsensi.setProgressTintList(
                android.content.res.ColorStateList.valueOf(
                        ContextCompat.getColor(context, warnaProgress)
                )
        );

        if (item.isAman()) {
            holder.tvStatus.setText("Aman");
            holder.tvStatus.getBackground().setTint(
                    ContextCompat.getColor(context, R.color.uap_success)
            );
        } else {
            holder.tvStatus.setText("Perhatian");
            holder.tvStatus.getBackground().setTint(
                    ContextCompat.getColor(context, R.color.uap_error)
            );
        }
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (onItemClickListener != null &&
                    adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(item, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAbsensi.size();
    }

    static class AbsensiViewHolder extends RecyclerView.ViewHolder {
        TextView    tvNamaMatkul, tvKode, tvStatus;
        TextView    tvJmlHadir, tvJmlIzin, tvJmlSakit;
        TextView    tvJmlAlpa, tvJmlTotal, tvPersen;
        ProgressBar progressAbsensi;

        AbsensiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMatkul    = itemView.findViewById(R.id.tvNamaMatkulAbsensi);
            tvKode          = itemView.findViewById(R.id.tvKodeAbsensi);
            tvStatus        = itemView.findViewById(R.id.tvStatusAbsensi);
            tvJmlHadir      = itemView.findViewById(R.id.tvJmlHadir);
            tvJmlIzin       = itemView.findViewById(R.id.tvJmlIzin);
            tvJmlSakit      = itemView.findViewById(R.id.tvJmlSakit);
            tvJmlAlpa       = itemView.findViewById(R.id.tvJmlAlpa);
            tvJmlTotal      = itemView.findViewById(R.id.tvJmlTotal);
            tvPersen        = itemView.findViewById(R.id.tvPersenAbsensi);
            progressAbsensi = itemView.findViewById(R.id.progressAbsensi);
        }
    }
}
