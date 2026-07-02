package com.example.siakad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.model.JadwalModel;

import java.util.List;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder> {

    private final Context          context;
    private final List<JadwalModel> listJadwal;
    private OnItemClickListener     onItemClickListener;

    public JadwalAdapter(Context context, List<JadwalModel> listJadwal) {
        this.context    = context;
        this.listJadwal = listJadwal;
    }

    public interface OnItemClickListener {
        void onItemClick(JadwalModel item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public JadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_jadwal, parent, false);
        return new JadwalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalViewHolder holder, int position) {
        JadwalModel item = listJadwal.get(position);

        holder.tvJamMulai.setText(item.getJamMulai());
        holder.tvJamSelesai.setText(item.getJamSelesai());
        holder.tvNamaMatkul.setText(item.getNamaMatkul());
        holder.tvDosen.setText(item.getDosen());
        holder.tvRuangan.setText(item.getRuangan());
        holder.tvSks.setText(item.getSks() + " SKS");
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
        return listJadwal.size();
    }

    static class JadwalViewHolder extends RecyclerView.ViewHolder {
        TextView tvJamMulai, tvJamSelesai;
        TextView tvNamaMatkul, tvDosen, tvRuangan, tvSks;

        JadwalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJamMulai   = itemView.findViewById(R.id.tvJamMulai);
            tvJamSelesai = itemView.findViewById(R.id.tvJamSelesai);
            tvNamaMatkul = itemView.findViewById(R.id.tvNamaMatkulJadwal);
            tvDosen      = itemView.findViewById(R.id.tvDosenJadwal);
            tvRuangan    = itemView.findViewById(R.id.tvRuanganJadwal);
            tvSks        = itemView.findViewById(R.id.tvSksJadwal);
        }
    }
}
