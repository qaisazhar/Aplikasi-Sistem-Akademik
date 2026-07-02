package com.example.siakad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.adapter.NotifikasiAdapter;
import com.example.siakad.model.NotifikasiModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView      rvNotifikasi;
    private LinearLayout      layoutEmpty;
    private TextView          tvJumlahNotif;

    private NotifikasiAdapter    adapter;
    private List<NotifikasiModel> listNotifikasi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification,
                container, false);
        initViews(view);
        loadDummyData();
        setupRecyclerView();
        updateJumlahNotif();

        return view;
    }

    private void initViews(View view) {
        rvNotifikasi   = view.findViewById(R.id.rvNotifikasi);
        layoutEmpty    = view.findViewById(R.id.layoutEmpty);
        tvJumlahNotif  = view.findViewById(R.id.tvJumlahNotif);
    }

    private void loadDummyData() {
        listNotifikasi = new ArrayList<>();

        listNotifikasi.add(new NotifikasiModel(
                "Pengumuman UAS Ganjil 2025/2026",
                "Ujian Akhir Semester Ganjil akan dilaksanakan mulai tanggal 15 Januari 2026. Harap mempersiapkan diri dengan baik.",
                "28 Mei 2025",
                "Akademik",
                R.drawable.ic_info,
                R.color.uap_primary,
                false
        ));

        listNotifikasi.add(new NotifikasiModel(
                "Batas Akhir Pembayaran UKT",
                "Pembayaran UKT Semester Ganjil 2025/2026 paling lambat dilakukan pada tanggal 30 Juni 2025. Hindari keterlambatan.",
                "26 Mei 2025",
                "Keuangan",
                R.drawable.ic_payment,
                R.color.uap_gold,
                false
        ));

        listNotifikasi.add(new NotifikasiModel(
                "Pengisian KRS Semester Ganjil",
                "Pengisian Kartu Rencana Studi (KRS) dibuka mulai 1 - 14 Juni 2025 melalui portal akademik. Segera konsultasikan dengan Dosen PA.",
                "24 Mei 2025",
                "Akademik",
                R.drawable.ic_krs,
                R.color.uap_primary,
                false
        ));

        listNotifikasi.add(new NotifikasiModel(
                "Jadwal Kuliah Telah Diperbarui",
                "Jadwal perkuliahan Semester Ganjil 2025/2026 telah diperbarui. Silakan cek jadwal terbaru di menu Jadwal Kuliah.",
                "20 Mei 2025",
                "Jadwal",
                R.drawable.ic_schedule,
                R.color.uap_success,
                true
        ));

        listNotifikasi.add(new NotifikasiModel(
                "Nilai UTS Telah Keluar",
                "Nilai Ujian Tengah Semester (UTS) sudah dapat dilihat pada menu KHS. Jika ada keberatan nilai, hubungi dosen pengampu.",
                "15 Mei 2025",
                "Akademik",
                R.drawable.ic_khs,
                R.color.uap_secondary,
                true
        ));

        listNotifikasi.add(new NotifikasiModel(
                "Libur Hari Raya Waisak",
                "Kampus UAP libur pada tanggal 12 Mei 2025 dalam rangka perayaan Hari Raya Waisak. Perkuliahan kembali normal pada 13 Mei 2025.",
                "10 Mei 2025",
                "Informasi",
                R.drawable.ic_calendar,
                R.color.uap_teal,
                true
        ));
    }

    private void setupRecyclerView() {
        adapter = new NotifikasiAdapter(getContext(), listNotifikasi);

        rvNotifikasi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifikasi.setAdapter(adapter);
        rvNotifikasi.setHasFixedSize(false);

        adapter.setOnItemClickListener((item, position) -> {
            Toast.makeText(getContext(), item.getJudul(), Toast.LENGTH_SHORT).show();
            updateJumlahNotif();
        });

        if (listNotifikasi.isEmpty()) {
            rvNotifikasi.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvNotifikasi.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    private void updateJumlahNotif() {
        long belumDibaca = listNotifikasi.stream()
                .filter(n -> !n.isSudahDibaca())
                .count();

        if (belumDibaca > 0) {
            tvJumlahNotif.setText(belumDibaca + " belum dibaca");
        } else {
            tvJumlahNotif.setText("Semua terbaca");
        }
    }
}