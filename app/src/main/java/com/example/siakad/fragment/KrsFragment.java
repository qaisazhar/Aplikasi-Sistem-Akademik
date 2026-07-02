package com.example.siakad.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.siakad.adapter.KrsAdapter;
import com.example.siakad.model.KrsModel;
import com.example.siakad.network.RetrofitClient;
import com.example.siakad.network.response.KrsResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KrsFragment extends Fragment {

    private static final String ARG_NPM = "ARG_NPM";
    private String npm = "";

    private RecyclerView rvKrs;
    private TextView tvTotalSks, tvTotalMatkul;
    private ProgressBar progressKrs;
    private MaterialButton btnInputKrs;

    private KrsAdapter adapter;
    private final List<KrsModel> listKrs = new ArrayList<>();
    private final List<KrsModel> localKrs = new ArrayList<>();

    public static KrsFragment newInstance(String npm) {
        KrsFragment fragment = new KrsFragment();
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

        View view = inflater.inflate(R.layout.fragment_krs, container, false);

        initViews(view);
        setupRecyclerView();
        setupInputKrsButton();
        loadFromApi();

        return view;
    }

    private void initViews(View view) {
        rvKrs = view.findViewById(R.id.rvKrs);
        tvTotalSks = view.findViewById(R.id.tvTotalSks);
        tvTotalMatkul = view.findViewById(R.id.tvTotalMatkul);
        progressKrs = view.findViewById(R.id.progressKrs);
        btnInputKrs = view.findViewById(R.id.btnInputKrs);
    }

    private void setupRecyclerView() {
        adapter = new KrsAdapter(getContext(), listKrs);
        rvKrs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKrs.setAdapter(adapter);
        rvKrs.setHasFixedSize(false);
        adapter.setOnItemClickListener((item, position) -> showDetailKrsDialog(item));
    }

    private void setupInputKrsButton() {
        btnInputKrs.setOnClickListener(v -> showInputKrsDialog());
    }

    private void loadFromApi() {
        showLoading(true);
        RetrofitClient.getInstance()
                .getApiService()
                .getKrs(npm)
                .enqueue(new Callback<KrsResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<KrsResponse> call,
                                           @NonNull Response<KrsResponse> response) {
                        showLoading(false);
                        listKrs.clear();

                        if (response.isSuccessful() && response.body() != null) {
                            KrsResponse result = response.body();

                            if (result.isStatus()) {
                                int nomor = 1;
                                for (KrsResponse.DataKrs item : result.getData()) {
                                    listKrs.add(new KrsModel(
                                            nomor++,
                                            item.getNama(),
                                            item.getKode(),
                                            item.getDosen(),
                                            item.getSks()
                                    ));
                                }
                            } else {
                                showToast(result.getMessage());
                            }
                        } else {
                            showToast("Gagal mengambil data KRS");
                        }

                        appendLocalKrs();
                        refreshKrsSummary();
                    }

                    @Override
                    public void onFailure(@NonNull Call<KrsResponse> call,
                                          @NonNull Throwable t) {
                        showLoading(false);
                        showToast("Tidak dapat terhubung ke server");
                        listKrs.clear();
                        appendLocalKrs();
                        refreshKrsSummary();
                    }
                });
    }

    private void showDetailKrsDialog(KrsModel item) {
        if (getContext() == null) return;

        new AlertDialog.Builder(requireContext())
                .setTitle(item.getNamaMatkul())
                .setMessage(
                        "Kode Mata Kuliah: " + item.getKodeMatkul() + "\n" +
                        "Dosen Pengampu: " + item.getDosenMatkul() + "\n" +
                        "Jumlah SKS: " + item.getSks() + "\n" +
                        "Nomor Urut: " + item.getNomor()
                )
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showLoading(boolean isLoading) {
        if (progressKrs == null) return;

        progressKrs.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rvKrs.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        btnInputKrs.setEnabled(!isLoading);
    }

    private void showInputKrsDialog() {
        if (getContext() == null) return;

        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        int horizontalPadding = dp(20);
        form.setPadding(horizontalPadding, dp(8), horizontalPadding, 0);

        EditText etKode = createInput("Kode mata kuliah", InputType.TYPE_CLASS_TEXT);
        EditText etNama = createInput("Nama mata kuliah", InputType.TYPE_CLASS_TEXT);
        EditText etDosen = createInput("Nama dosen", InputType.TYPE_CLASS_TEXT);
        EditText etSks = createInput("SKS", InputType.TYPE_CLASS_NUMBER);

        form.addView(etKode);
        form.addView(etNama);
        form.addView(etDosen);
        form.addView(etSks);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Input KRS")
                .setView(form)
                .setNegativeButton("Batal", (d, which) -> d.dismiss())
                .setPositiveButton("Simpan", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String kode = etKode.getText().toString().trim().toUpperCase();
                    String namaMatkul = etNama.getText().toString().trim();
                    String dosen = etDosen.getText().toString().trim();
                    String sksText = etSks.getText().toString().trim();

                    if (kode.isEmpty() || namaMatkul.isEmpty() || dosen.isEmpty() || sksText.isEmpty()) {
                        showToast("Lengkapi semua data KRS");
                        return;
                    }

                    int sks = Integer.parseInt(sksText);
                    if (sks < 1 || sks > 6) {
                        showToast("SKS harus 1 sampai 6");
                        return;
                    }

                    if (isKodeAlreadyTaken(kode)) {
                        showToast("Mata kuliah sudah ada di KRS");
                        return;
                    }

                    KrsModel newItem = new KrsModel(
                            listKrs.size() + 1,
                            namaMatkul,
                            kode,
                            dosen,
                            sks
                    );
                    listKrs.add(newItem);
                    localKrs.add(new KrsModel(
                            localKrs.size() + 1,
                            namaMatkul,
                            kode,
                            dosen,
                            sks
                    ));
                    saveLocalKrsOnly();
                    refreshKrsSummary();
                    dialog.dismiss();
                    showToast("KRS berhasil ditambahkan");
                }));
        dialog.show();
    }

    private EditText createInput(String hint, int inputType) {
        EditText editText = new EditText(requireContext());
        editText.setHint(hint);
        editText.setInputType(inputType);
        editText.setSingleLine(true);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        return editText;
    }

    private boolean isKodeAlreadyTaken(String kode) {
        for (KrsModel item : listKrs) {
            if (item.getKodeMatkul().equalsIgnoreCase(kode)) {
                return true;
            }
        }
        return false;
    }

    private void appendLocalKrs() {
        if (getContext() == null) return;

        localKrs.clear();
        localKrs.addAll(FeatureDataManager.getLocalKrs(
                requireContext(),
                npm,
                1
        ));

        int nomor = listKrs.size() + 1;
        for (KrsModel item : localKrs) {
            listKrs.add(new KrsModel(
                    nomor++,
                    item.getNamaMatkul(),
                    item.getKodeMatkul(),
                    item.getDosenMatkul(),
                    item.getSks()
            ));
        }
    }

    private void saveLocalKrsOnly() {
        if (getContext() == null) return;

        FeatureDataManager.saveLocalKrs(requireContext(), npm, localKrs);
    }

    private void refreshKrsSummary() {
        int totalSks = 0;
        for (KrsModel item : listKrs) {
            totalSks += item.getSks();
        }

        tvTotalSks.setText(String.valueOf(totalSks));
        tvTotalMatkul.setText(String.valueOf(listKrs.size()));
        adapter.notifyDataSetChanged();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private void showToast(String pesan) {
        if (getContext() != null) {
            Toast.makeText(getContext(), pesan, Toast.LENGTH_SHORT).show();
        }
    }
}
