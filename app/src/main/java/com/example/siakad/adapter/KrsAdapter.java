package com.example.siakad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.model.KrsModel;

import java.util.List;

public class KrsAdapter extends RecyclerView.Adapter<KrsAdapter.KrsViewHolder> {

    private final Context        context;
    private final List<KrsModel> listKrs;
    private OnItemClickListener  onItemClickListener;

    public KrsAdapter(Context context, List<KrsModel> listKrs) {
        this.context = context;
        this.listKrs = listKrs;
    }

    public interface OnItemClickListener {
        void onItemClick(KrsModel item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public KrsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_krs, parent, false);
        return new KrsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KrsViewHolder holder, int position) {
        KrsModel item = listKrs.get(position);

        holder.tvNomor.setText(String.valueOf(item.getNomor()));
        holder.tvNamaMatkul.setText(item.getNamaMatkul());
        holder.tvKodeMatkul.setText(item.getKodeMatkul());
        holder.tvDosenMatkul.setText(item.getDosenMatkul());
        holder.tvSks.setText(String.valueOf(item.getSks()));
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
        return listKrs.size();
    }

    static class KrsViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomor, tvNamaMatkul, tvKodeMatkul;
        TextView tvDosenMatkul, tvSks;

        KrsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomor      = itemView.findViewById(R.id.tvNomor);
            tvNamaMatkul = itemView.findViewById(R.id.tvNamaMatkul);
            tvKodeMatkul = itemView.findViewById(R.id.tvKodeMatkul);
            tvDosenMatkul = itemView.findViewById(R.id.tvDosenMatkul);
            tvSks        = itemView.findViewById(R.id.tvSks);
        }
    }
}
