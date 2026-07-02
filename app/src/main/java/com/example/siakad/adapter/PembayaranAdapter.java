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
import com.example.siakad.model.PembayaranModel;

import java.util.List;

public class PembayaranAdapter extends
        RecyclerView.Adapter<PembayaranAdapter.PembayaranViewHolder> {

    private final Context               context;
    private final List<PembayaranModel> listPembayaran;
    private OnItemClickListener         onItemClickListener;

    public PembayaranAdapter(Context context, List<PembayaranModel> listPembayaran) {
        this.context        = context;
        this.listPembayaran = listPembayaran;
    }

    public interface OnItemClickListener {
        void onItemClick(PembayaranModel item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public PembayaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_pembayaran, parent, false);
        return new PembayaranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PembayaranViewHolder holder, int position) {
        PembayaranModel item = listPembayaran.get(position);

        holder.tvJenis.setText(item.getJenisPembayaran());
        holder.tvTanggal.setText(item.getTanggalBayar());
        holder.tvNoTransaksi.setText(item.getNoTransaksi());
        holder.tvNominal.setText(item.getNominalFormatted());
        holder.tvStatus.setText(item.getStatus());

        int warnaBadge = getWarnaBadge(item.getStatus());
        holder.tvStatus.getBackground().setTint(
                ContextCompat.getColor(context, warnaBadge)
        );

        int warnaNominal = item.getStatus().equals("Lunas")
                ? R.color.uap_success
                : R.color.uap_error;
        holder.tvNominal.setTextColor(
                ContextCompat.getColor(context, warnaNominal)
        );

        holder.ivIcon.setColorFilter(
                ContextCompat.getColor(context, warnaBadge)
        );
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (onItemClickListener != null &&
                    adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(item, adapterPosition);
            }
        });
    }

    private int getWarnaBadge(String status) {
        switch (status) {
            case "Lunas":    return R.color.uap_success;
            case "Cicilan":  return R.color.uap_warning;
            default:         return R.color.uap_error;
        }
    }

    @Override
    public int getItemCount() {
        return listPembayaran.size();
    }

    static class PembayaranViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView  tvJenis, tvTanggal, tvNoTransaksi;
        TextView  tvNominal, tvStatus;

        PembayaranViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon        = itemView.findViewById(R.id.ivIconPembayaran);
            tvJenis       = itemView.findViewById(R.id.tvJenisPembayaran);
            tvTanggal     = itemView.findViewById(R.id.tvTanggalBayar);
            tvNoTransaksi = itemView.findViewById(R.id.tvNoTransaksi);
            tvNominal     = itemView.findViewById(R.id.tvNominalPembayaran);
            tvStatus      = itemView.findViewById(R.id.tvStatusPembayaran);
        }
    }
}
