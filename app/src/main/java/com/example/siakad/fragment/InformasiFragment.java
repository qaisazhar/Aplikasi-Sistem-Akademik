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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.adapter.InformasiAdapter;
import com.example.siakad.model.InformasiModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InformasiFragment extends Fragment {

    private RecyclerView  rvInformasi;
    private LinearLayout  layoutFilterKategori, layoutEmpty;
    private TextView      tvLabelKategori, tvJumlahInfo;

    private InformasiAdapter    adapter;
    private List<InformasiModel> semuaData;
    private String kategoriAktif = "Semua";

    private final String[] listKategori = {
            "Semua", "Akademik", "Keuangan", "Kegiatan", "Umum"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_informasi,
                container, false);
        initViews(view);
        loadDummyData();
        setupFilterKategori();
        setupRecyclerView();
        filterData("Semua");

        return view;
    }

    private void initViews(View view) {
        rvInformasi           = view.findViewById(R.id.rvInformasi);
        layoutFilterKategori  = view.findViewById(R.id.layoutFilterKategori);
        layoutEmpty           = view.findViewById(R.id.layoutEmpty);
        tvLabelKategori       = view.findViewById(R.id.tvLabelKategori);
        tvJumlahInfo          = view.findViewById(R.id.tvJumlahInfo);
    }

    private void loadDummyData() {
        semuaData = new ArrayList<>();

        semuaData.add(new InformasiModel(
                "Pengumuman Jadwal UAS Ganjil 2025/2026",
                "Ujian Akhir Semester Ganjil 2025/2026 akan dilaksanakan mulai " +
                        "tanggal 15 Januari 2026 s.d 25 Januari 2026. Mahasiswa wajib " +
                        "membawa kartu ujian yang telah divalidasi oleh BAK.",
                "28 Mei 2025",
                "Akademik",
                R.drawable.ic_info,
                R.color.uap_primary,
                true));

        semuaData.add(new InformasiModel(
                "Batas Akhir Pembayaran UKT Semester Ganjil",
                "Pembayaran UKT Semester Ganjil 2025/2026 paling lambat tanggal " +
                        "30 Juni 2025. Mahasiswa yang belum membayar tidak dapat mengisi KRS.",
                "26 Mei 2025",
                "Keuangan",
                R.drawable.ic_payment,
                R.color.uap_gold,
                true));

        semuaData.add(new InformasiModel(
                "Pembukaan Pengisian KRS Semester Ganjil",
                "Pengisian Kartu Rencana Studi (KRS) Semester Ganjil 2025/2026 " +
                        "dibuka mulai 1 - 14 Juni 2025. Konsultasikan pilihan mata kuliah " +
                        "dengan Dosen Pembimbing Akademik (PA) Anda.",
                "24 Mei 2025",
                "Akademik",
                R.drawable.ic_krs,
                R.color.uap_primary,
                true));

        semuaData.add(new InformasiModel(
                "Seminar Nasional Teknologi Informasi 2025",
                "Fakultas Teknologi & Informatika UAP mengadakan Seminar Nasional " +
                        "bertema 'AI & Future of Technology' pada tanggal 20 Juni 2025. " +
                        "Pendaftaran peserta dibuka hingga 15 Juni 2025.",
                "20 Mei 2025",
                "Kegiatan",
                R.drawable.ic_school,
                R.color.uap_teal,
                false));

        semuaData.add(new InformasiModel(
                "Beasiswa Prestasi Akademik 2025",
                "Universitas Aisyah Pringsewu membuka pendaftaran beasiswa " +
                        "prestasi akademik bagi mahasiswa aktif dengan IPK minimal 3.50. " +
                        "Berkas pendaftaran diterima paling lambat 10 Juni 2025.",
                "18 Mei 2025",
                "Akademik",
                R.drawable.ic_star,
                R.color.uap_gold,
                false));

        semuaData.add(new InformasiModel(
                "Pelatihan Sertifikasi Kompetensi TI",
                "Program Studi Teknik Informatika mengadakan pelatihan sertifikasi " +
                        "kompetensi untuk mahasiswa semester 5 ke atas. Pendaftaran melalui " +
                        "Ketua Program Studi masing-masing.",
                "15 Mei 2025",
                "Kegiatan",
                R.drawable.ic_badge,
                R.color.uap_secondary,
                false));

        semuaData.add(new InformasiModel(
                "Perubahan Jadwal Kuliah Minggu Ke-12",
                "Terdapat perubahan jadwal kuliah pada minggu ke-12 dikarenakan " +
                        "libur nasional Hari Waisak. Jadwal pengganti akan diinformasikan " +
                        "oleh masing-masing dosen pengampu.",
                "10 Mei 2025",
                "Akademik",
                R.drawable.ic_schedule,
                R.color.uap_success,
                false));

        semuaData.add(new InformasiModel(
                "Informasi Wisuda Periode II Tahun 2025",
                "Pendaftaran wisuda periode II tahun 2025 dibuka mulai " +
                        "1 Juli s.d 31 Agustus 2025. Syarat dan ketentuan dapat " +
                        "dilihat di website resmi UAP.",
                "05 Mei 2025",
                "Umum",
                R.drawable.ic_school,
                R.color.uap_info,
                false));
    }

    private void setupFilterKategori() {
        for (String kategori : listKategori) {
            TextView tabItem = new TextView(getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(6, 0, 6, 0);
            tabItem.setLayoutParams(params);

            tabItem.setText(kategori);
            tabItem.setTextSize(12f);
            tabItem.setPadding(24, 14, 24, 14);

            updateTabStyle(tabItem, kategori.equals(kategoriAktif));

            tabItem.setOnClickListener(v -> {
                kategoriAktif = kategori;
                updateSemuaTab();
                filterData(kategori);
            });

            layoutFilterKategori.addView(tabItem);
        }
    }

    private void updateSemuaTab() {
        for (int i = 0; i < layoutFilterKategori.getChildCount(); i++) {
            View child = layoutFilterKategori.getChildAt(i);
            if (child instanceof TextView) {
                TextView tab = (TextView) child;
                updateTabStyle(tab, tab.getText().toString().equals(kategoriAktif));
            }
        }
    }

    private void updateTabStyle(TextView tab, boolean isAktif) {
        if (isAktif) {
            tab.setBackgroundResource(R.drawable.bg_tab_active);
            tab.setTextColor(ContextCompat.getColor(
                    requireContext(), R.color.uap_on_primary));
            tab.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            tab.setBackgroundResource(R.drawable.bg_tab_inactive);
            tab.setTextColor(ContextCompat.getColor(
                    requireContext(), R.color.uap_text_secondary));
            tab.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
    }

    private void setupRecyclerView() {
        adapter = new InformasiAdapter(getContext(), new ArrayList<>());
        rvInformasi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInformasi.setAdapter(adapter);
        rvInformasi.setHasFixedSize(false);

        adapter.setOnItemClickListener((item, position) ->
                Toast.makeText(getContext(), item.getJudul(),
                        Toast.LENGTH_SHORT).show()
        );
    }

    private void filterData(String kategori) {
        List<InformasiModel> filtered;

        if (kategori.equals("Semua")) {
            filtered = new ArrayList<>(semuaData);
        } else {
            filtered = semuaData.stream()
                    .filter(item -> item.getKategori().equals(kategori))
                    .collect(Collectors.toList());
        }

        tvLabelKategori.setText(
                kategori.equals("Semua") ? "Semua Informasi" : kategori
        );
        tvJumlahInfo.setText(filtered.size() + " informasi");

        if (filtered.isEmpty()) {
            rvInformasi.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvInformasi.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            adapter.updateData(filtered);
        }
    }
}