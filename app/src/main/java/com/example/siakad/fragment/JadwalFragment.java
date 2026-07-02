package com.example.siakad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.R;
import com.example.siakad.adapter.JadwalAdapter;
import com.example.siakad.model.JadwalModel;
import com.example.siakad.network.RetrofitClient;
import com.example.siakad.network.response.JadwalResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JadwalFragment extends Fragment {

    private static final String ARG_NPM = "ARG_NPM";
    private String npm = "";

    private RecyclerView rvJadwal;
    private LinearLayout layoutTabHari, layoutEmpty;
    private ProgressBar  progressJadwal;
    private TextView     tvHariAktif, tvJumlahJadwal;

    private JadwalAdapter     adapter;
    private List<JadwalModel> listJadwal = new ArrayList<>();

    private final List<String> listHari = Arrays.asList(
            "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
    );
    private String hariAktif = "Senin";

    public static JadwalFragment newInstance(String npm) {
        JadwalFragment fragment = new JadwalFragment();
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

        View view = inflater.inflate(R.layout.fragment_jadwal, container, false);

        initViews(view);
        setupRecyclerView();
        setupTabHari();
        loadFromApi(hariAktif);

        return view;
    }

    private void initViews(View view) {
        rvJadwal      = view.findViewById(R.id.rvJadwal);
        layoutTabHari = view.findViewById(R.id.layoutTabHari);
        layoutEmpty   = view.findViewById(R.id.layoutEmpty);
        progressJadwal = view.findViewById(R.id.progressJadwal);
        tvHariAktif   = view.findViewById(R.id.tvHariAktif);
        tvJumlahJadwal = view.findViewById(R.id.tvJumlahJadwal);
    }

    private void setupRecyclerView() {
        adapter = new JadwalAdapter(getContext(), listJadwal);
        rvJadwal.setLayoutManager(new LinearLayoutManager(getContext()));
        rvJadwal.setAdapter(adapter);
        rvJadwal.setHasFixedSize(false);
        adapter.setOnItemClickListener((item, position) -> showDetailJadwalDialog(item));
    }

    private void setupTabHari() {
        for (String hari : listHari) {
            TextView tabItem = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(6, 0, 6, 0);
            tabItem.setLayoutParams(params);
            tabItem.setText(hari);
            tabItem.setTextSize(13f);
            tabItem.setPadding(28, 16, 28, 16);
            updateTabStyle(tabItem, hari.equals(hariAktif));

            tabItem.setOnClickListener(v -> {
                hariAktif = hari;
                updateSemuaTab();
                loadFromApi(hari);
            });

            layoutTabHari.addView(tabItem);
        }
    }

    private void updateSemuaTab() {
        for (int i = 0; i < layoutTabHari.getChildCount(); i++) {
            View child = layoutTabHari.getChildAt(i);
            if (child instanceof TextView) {
                TextView tab = (TextView) child;
                updateTabStyle(tab, tab.getText().toString().equals(hariAktif));
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

    private void loadFromApi(String hari) {
        tvHariAktif.setText(hari);
        showLoading(true);

        RetrofitClient.getInstance()
                .getApiService()
                .getJadwal(npm, hari, 5)
                .enqueue(new Callback<JadwalResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<JadwalResponse> call,
                                           @NonNull Response<JadwalResponse> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            JadwalResponse result = response.body();

                            if (result.isStatus()) {
                                listJadwal.clear();

                                for (JadwalResponse.DataJadwal item : result.getData()) {
                                    listJadwal.add(new JadwalModel(
                                            item.getHari(),
                                            item.getJamMulai(),
                                            item.getJamSelesai(),
                                            item.getNama(),
                                            item.getDosen(),
                                            item.getRuangan(),
                                            item.getSks()
                                    ));
                                }

                                adapter.notifyDataSetChanged();

                                if (listJadwal.isEmpty()) {
                                    rvJadwal.setVisibility(View.GONE);
                                    layoutEmpty.setVisibility(View.VISIBLE);
                                    tvJumlahJadwal.setText("Libur");
                                } else {
                                    rvJadwal.setVisibility(View.VISIBLE);
                                    layoutEmpty.setVisibility(View.GONE);
                                    tvJumlahJadwal.setText(listJadwal.size() + " mata kuliah");
                                }

                            } else {
                                showToast(result.getMessage());
                            }
                        } else {
                            showToast("Gagal mengambil data Jadwal");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JadwalResponse> call,
                                          @NonNull Throwable t) {
                        showLoading(false);
                        showToast("Tidak dapat terhubung ke server");
                    }
                });
    }

    private void showDetailJadwalDialog(JadwalModel item) {
        if (getContext() == null) return;

        new AlertDialog.Builder(requireContext())
                .setTitle(item.getNamaMatkul())
                .setMessage(
                        "Hari: " + item.getHari() + "\n" +
                        "Jam: " + item.getJamMulai() + " - " + item.getJamSelesai() + "\n" +
                        "Dosen Pengampu: " + item.getDosen() + "\n" +
                        "Ruangan: " + item.getRuangan() + "\n" +
                        "Jumlah SKS: " + item.getSks()
                )
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showLoading(boolean isLoading) {
        if (progressJadwal == null) return;

        progressJadwal.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rvJadwal.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        if (isLoading) {
            layoutEmpty.setVisibility(View.GONE);
        }

        for (int i = 0; i < layoutTabHari.getChildCount(); i++) {
            layoutTabHari.getChildAt(i).setEnabled(!isLoading);
        }
    }

    private void showToast(String pesan) {
        if (getContext() != null) {
            Toast.makeText(getContext(), pesan, Toast.LENGTH_SHORT).show();
        }
    }
}
