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
import com.example.siakad.adapter.KhsAdapter;
import com.example.siakad.model.KhsModel;
import com.example.siakad.network.RetrofitClient;
import com.example.siakad.network.response.KhsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhsFragment extends Fragment {

    private static final String ARG_NPM = "ARG_NPM";
    private String npm = "";

    private RecyclerView  rvKhs;
    private LinearLayout  layoutFilterSemester;
    private ProgressBar   progressKhs;
    private TextView      tvIpk, tvIps, tvSksLulus;
    private TextView      tvLabelSemester, tvJumlahMatkul;

    private KhsAdapter     adapter;
    private List<KhsModel> listKhs    = new ArrayList<>();
    private int semesterAktif         = 5;
    private final int[] listSemester  = {1, 2, 3, 4, 5};

    public static KhsFragment newInstance(String npm) {
        KhsFragment fragment = new KhsFragment();
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

        View view = inflater.inflate(R.layout.fragment_khs, container, false);

        initViews(view);
        setupRecyclerView();
        setupFilterSemester();
        loadFromApi(semesterAktif);

        return view;
    }

    private void initViews(View view) {
        rvKhs                = view.findViewById(R.id.rvKhs);
        layoutFilterSemester = view.findViewById(R.id.layoutFilterSemester);
        progressKhs          = view.findViewById(R.id.progressKhs);
        tvIpk                = view.findViewById(R.id.tvIpk);
        tvIps                = view.findViewById(R.id.tvIps);
        tvSksLulus           = view.findViewById(R.id.tvSksLulus);
        tvLabelSemester      = view.findViewById(R.id.tvLabelSemester);
        tvJumlahMatkul       = view.findViewById(R.id.tvJumlahMatkul);
    }

    private void setupRecyclerView() {
        adapter = new KhsAdapter(getContext(), listKhs);
        rvKhs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKhs.setAdapter(adapter);
        rvKhs.setHasFixedSize(false);
        adapter.setOnItemClickListener((item, position) -> showDetailKhsDialog(item));
    }

    private void setupFilterSemester() {
        for (int sem : listSemester) {
            TextView tabItem = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(6, 0, 6, 0);
            tabItem.setLayoutParams(params);
            tabItem.setText("Sem " + sem);
            tabItem.setTextSize(12f);
            tabItem.setPadding(24, 14, 24, 14);
            updateTabStyle(tabItem, sem == semesterAktif);

            int finalSem = sem;
            tabItem.setOnClickListener(v -> {
                semesterAktif = finalSem;
                updateSemuaTab();
                loadFromApi(finalSem);
            });

            layoutFilterSemester.addView(tabItem);
        }
    }

    private void updateSemuaTab() {
        for (int i = 0; i < layoutFilterSemester.getChildCount(); i++) {
            View child = layoutFilterSemester.getChildAt(i);
            if (child instanceof TextView) {
                TextView tab = (TextView) child;
                int sem = i + 1;
                updateTabStyle(tab, sem == semesterAktif);
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

    private void loadFromApi(int semester) {
        tvLabelSemester.setText("Semester " + semester);
        showLoading(true);

        RetrofitClient.getInstance()
                .getApiService()
                .getKhs(npm, semester)
                .enqueue(new Callback<KhsResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<KhsResponse> call,
                                           @NonNull Response<KhsResponse> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            KhsResponse result = response.body();

                            if (result.isStatus()) {
                                listKhs.clear();

                                for (KhsResponse.DataKhs item : result.getData()) {
                                    listKhs.add(new KhsModel(
                                            item.getNama(),
                                            item.getKode(),
                                            item.getSks(),
                                            item.getNilaiHuruf(),
                                            item.getNilaiAngka(),
                                            item.getSemester()
                                    ));
                                }

                                adapter.notifyDataSetChanged();

                                tvIpk.setText(String.format("%.2f", result.getIpk()));
                                tvIps.setText(String.format("%.2f", result.getIps()));
                                tvSksLulus.setText(String.valueOf(result.getSksLulus()));
                                tvJumlahMatkul.setText(listKhs.size() + " matkul");

                            } else {
                                showToast(result.getMessage());
                            }
                        } else {
                            showToast("Gagal mengambil data KHS");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<KhsResponse> call,
                                          @NonNull Throwable t) {
                        showLoading(false);
                        showToast("Tidak dapat terhubung ke server");
                    }
                });
    }

    private void showDetailKhsDialog(KhsModel item) {
        if (getContext() == null) return;

        new AlertDialog.Builder(requireContext())
                .setTitle(item.getNamaMatkul())
                .setMessage(
                        "Kode Mata Kuliah: " + item.getKodeMatkul() + "\n" +
                        "Semester: " + item.getSemester() + "\n" +
                        "Jumlah SKS: " + item.getSks() + "\n" +
                        "Nilai Huruf: " + item.getNilaiHuruf() + "\n" +
                        "Nilai Angka: " + String.format("%.2f", item.getNilaiAngka())
                )
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showLoading(boolean isLoading) {
        if (progressKhs == null) return;

        progressKhs.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rvKhs.setVisibility(isLoading ? View.GONE : View.VISIBLE);

        for (int i = 0; i < layoutFilterSemester.getChildCount(); i++) {
            layoutFilterSemester.getChildAt(i).setEnabled(!isLoading);
        }
    }

    private void showToast(String pesan) {
        if (getContext() != null) {
            Toast.makeText(getContext(), pesan, Toast.LENGTH_SHORT).show();
        }
    }
}
