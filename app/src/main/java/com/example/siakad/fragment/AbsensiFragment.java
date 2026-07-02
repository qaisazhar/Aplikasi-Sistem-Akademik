package com.example.siakad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.FeatureDataManager;
import com.example.siakad.R;
import com.example.siakad.adapter.AbsensiAdapter;
import com.example.siakad.model.AbsensiModel;
import com.example.siakad.network.RetrofitClient;
import com.example.siakad.network.response.AbsensiResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsensiFragment extends Fragment {

    private static final String ARG_NPM = "ARG_NPM";
    private String npm = "";

    private RecyclerView rvAbsensi;
    private TextView tvTotalHadir, tvTotalIzin, tvTotalAlpa;
    private ProgressBar progressLoadingAbsensi;
    private MaterialButton btnAbsen;

    private AbsensiAdapter adapter;
    private final List<AbsensiModel> listAbsensi = new ArrayList<>();

    public static AbsensiFragment newInstance(String npm) {
        AbsensiFragment fragment = new AbsensiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NPM, npm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            npm = getArguments().getString(ARG_NPM, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_absensi, container, false);

        initViews(view);
        setupRecyclerView();
        setupAbsenButton();
        loadFromApi();

        return view;
    }

    private void initViews(View view) {
        rvAbsensi = view.findViewById(R.id.rvAbsensi);
        tvTotalHadir = view.findViewById(R.id.tvTotalHadir);
        tvTotalIzin = view.findViewById(R.id.tvTotalIzin);
        tvTotalAlpa = view.findViewById(R.id.tvTotalAlpa);
        progressLoadingAbsensi = view.findViewById(R.id.progressLoadingAbsensi);
        btnAbsen = view.findViewById(R.id.btnAbsen);
    }

    private void setupRecyclerView() {
        adapter = new AbsensiAdapter(getContext(), listAbsensi);
        rvAbsensi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAbsensi.setAdapter(adapter);
        rvAbsensi.setHasFixedSize(false);
        adapter.setOnItemClickListener((item, position) -> showDetailAbsensiDialog(item));
    }

    private void setupAbsenButton() {
        btnAbsen.setOnClickListener(v -> showAbsenDialog());
    }

    private void loadFromApi() {
        showLoading(true);
        RetrofitClient.getInstance()
                .getApiService()
                .getAbsensi(npm, 5)
                .enqueue(new Callback<AbsensiResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<AbsensiResponse> call,
                                           @NonNull Response<AbsensiResponse> response) {
                        showLoading(false);
                        listAbsensi.clear();

                        if (response.isSuccessful() && response.body() != null) {
                            AbsensiResponse result = response.body();

                            if (result.isStatus()) {
                                for (AbsensiResponse.DataAbsensi item : result.getData()) {
                                    AbsensiModel model = new AbsensiModel(
                                            item.getNama(),
                                            item.getKode(),
                                            item.getHadir(),
                                            item.getIzin(),
                                            item.getSakit(),
                                            item.getAlpa(),
                                            item.getTotalPertemuan()
                                    );
                                    applyLocalAttendance(model);
                                    listAbsensi.add(model);
                                }
                            } else {
                                showToast(result.getMessage());
                            }
                        } else {
                            showToast("Gagal mengambil data Absensi");
                        }

                        refreshAbsensiSummary();
                    }

                    @Override
                    public void onFailure(@NonNull Call<AbsensiResponse> call,
                                          @NonNull Throwable t) {
                        showLoading(false);
                        showToast("Tidak dapat terhubung ke server");
                        listAbsensi.clear();
                        refreshAbsensiSummary();
                    }
                });
    }

    private void showDetailAbsensiDialog(AbsensiModel item) {
        if (getContext() == null) return;

        new AlertDialog.Builder(requireContext())
                .setTitle(item.getNamaMatkul())
                .setMessage(
                        "Kode Mata Kuliah: " + item.getKodeMatkul() + "\n" +
                        "Hadir: " + item.getHadir() + "\n" +
                        "Izin: " + item.getIzin() + "\n" +
                        "Sakit: " + item.getSakit() + "\n" +
                        "Alpa: " + item.getAlpa() + "\n" +
                        "Total Pertemuan: " + item.getTotalPertemuan() + "\n" +
                        "Persentase Kehadiran: " + item.getPersenKehadiran() + "%\n" +
                        "Status: " + (item.isAman() ? "Aman" : "Perlu Perhatian")
                )
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showLoading(boolean isLoading) {
        if (progressLoadingAbsensi == null) return;

        progressLoadingAbsensi.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rvAbsensi.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        btnAbsen.setEnabled(!isLoading);
    }

    private void showAbsenDialog() {
        if (getContext() == null) return;

        if (listAbsensi.isEmpty()) {
            showToast("Data mata kuliah belum tersedia");
            return;
        }

        String[] matkulItems = new String[listAbsensi.size()];
        for (int i = 0; i < listAbsensi.size(); i++) {
            AbsensiModel item = listAbsensi.get(i);
            matkulItems[i] = item.getKodeMatkul() + " - " + item.getNamaMatkul();
        }

        final int[] selectedMatkul = {0};
        final String[] selectedStatus = {"Hadir"};
        String[] statusItems = {"Hadir", "Izin", "Sakit"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Pilih Mata Kuliah")
                .setSingleChoiceItems(matkulItems, 0, (dialog, which) -> selectedMatkul[0] = which)
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Lanjut", (dialog, which) -> {
                    dialog.dismiss();
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Status Kehadiran")
                            .setSingleChoiceItems(statusItems, 0,
                                    (statusDialog, selected) -> selectedStatus[0] = statusItems[selected])
                            .setNegativeButton("Batal", (statusDialog, selected) -> statusDialog.dismiss())
                            .setPositiveButton("Simpan Absen", (statusDialog, selected) -> {
                                AbsensiModel selectedCourse = listAbsensi.get(selectedMatkul[0]);
                                boolean saved = FeatureDataManager.recordAttendance(
                                        requireContext(),
                                        npm,
                                        selectedCourse.getKodeMatkul(),
                                        selectedStatus[0]
                                );

                                if (!saved) {
                                    showToast("Anda sudah absen mata kuliah ini hari ini");
                                    return;
                                }

                                if ("Izin".equals(selectedStatus[0])) {
                                    selectedCourse.addKehadiran(0, 1, 0, 1);
                                } else if ("Sakit".equals(selectedStatus[0])) {
                                    selectedCourse.addKehadiran(0, 0, 1, 1);
                                } else {
                                    selectedCourse.addKehadiran(1, 0, 0, 1);
                                }

                                refreshAbsensiSummary();
                                showToast("Absen berhasil disimpan");
                            })
                            .show();
                })
                .show();
    }

    private void applyLocalAttendance(AbsensiModel model) {
        if (getContext() == null) return;

        FeatureDataManager.AttendanceAdjustment adjustment =
                FeatureDataManager.getAttendanceAdjustment(
                        requireContext(),
                        npm,
                        model.getKodeMatkul()
                );
        model.addKehadiran(
                adjustment.hadir,
                adjustment.izin,
                adjustment.sakit,
                adjustment.total
        );
    }

    private void refreshAbsensiSummary() {
        int totalHadir = 0;
        int totalIzin = 0;
        int totalAlpa = 0;

        for (AbsensiModel item : listAbsensi) {
            totalHadir += item.getHadir();
            totalIzin += item.getIzin();
            totalAlpa += item.getAlpa();
        }

        tvTotalHadir.setText(String.valueOf(totalHadir));
        tvTotalIzin.setText(String.valueOf(totalIzin));
        tvTotalAlpa.setText(String.valueOf(totalAlpa));
        adapter.notifyDataSetChanged();
    }

    private void showToast(String pesan) {
        if (getContext() != null) {
            Toast.makeText(getContext(), pesan, Toast.LENGTH_SHORT).show();
        }
    }
}
