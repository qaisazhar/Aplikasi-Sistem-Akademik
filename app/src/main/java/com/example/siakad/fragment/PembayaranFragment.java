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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siakad.FeatureDataManager;
import com.example.siakad.R;
import com.example.siakad.adapter.PembayaranAdapter;
import com.example.siakad.model.PembayaranModel;
import com.example.siakad.network.RetrofitClient;
import com.example.siakad.network.response.PembayaranResponse;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembayaranFragment extends Fragment {

    private static final String ARG_NPM = "ARG_NPM";
    private String npm = "";

    private RecyclerView rvPembayaran;
    private TextView tvTotalTagihan, tvSudahDibayar;
    private TextView tvSisaTagihan, tvStatusLunas;
    private ProgressBar progressPembayaran;
    private MaterialButton btnBayarUkt;

    private PembayaranAdapter adapter;
    private final List<PembayaranModel> listPembayaran = new ArrayList<>();
    private final List<PembayaranModel> localPayments = new ArrayList<>();

    private long totalTagihan = 3500000;
    private long sudahDibayar = 0;
    private long sisaTagihan = 3500000;

    public static PembayaranFragment newInstance(String npm) {
        PembayaranFragment fragment = new PembayaranFragment();
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

        View view = inflater.inflate(R.layout.fragment_pembayaran, container, false);

        initViews(view);
        setupRecyclerView();
        setupBayarButton();
        loadFromApi();

        return view;
    }

    private void initViews(View view) {
        rvPembayaran = view.findViewById(R.id.rvPembayaran);
        tvTotalTagihan = view.findViewById(R.id.tvTotalTagihan);
        tvSudahDibayar = view.findViewById(R.id.tvSudahDibayar);
        tvSisaTagihan = view.findViewById(R.id.tvSisaTagihan);
        tvStatusLunas = view.findViewById(R.id.tvStatusLunas);
        progressPembayaran = view.findViewById(R.id.progressPembayaran);
        btnBayarUkt = view.findViewById(R.id.btnBayarUkt);
    }

    private void setupRecyclerView() {
        adapter = new PembayaranAdapter(getContext(), listPembayaran);
        rvPembayaran.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPembayaran.setAdapter(adapter);
        rvPembayaran.setHasFixedSize(false);
        adapter.setOnItemClickListener((item, position) -> showDetailPembayaranDialog(item));
    }

    private void setupBayarButton() {
        btnBayarUkt.setOnClickListener(v -> showBayarUktDialog());
    }

    private void loadFromApi() {
        showLoading(true);
        RetrofitClient.getInstance()
                .getApiService()
                .getPembayaran(npm)
                .enqueue(new Callback<PembayaranResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<PembayaranResponse> call,
                                           @NonNull Response<PembayaranResponse> response) {
                        showLoading(false);
                        listPembayaran.clear();

                        if (response.isSuccessful() && response.body() != null) {
                            PembayaranResponse result = response.body();

                            if (result.isStatus()) {
                                totalTagihan = result.getTotalTagihan();
                                sudahDibayar = result.getSudahDibayar();
                                sisaTagihan = result.getSisaTagihan();

                                for (PembayaranResponse.DataPembayaran item : result.getData()) {
                                    listPembayaran.add(new PembayaranModel(
                                            item.getJenis(),
                                            item.getTanggalBayar(),
                                            item.getNoTransaksi(),
                                            item.getNominal(),
                                            item.getStatus(),
                                            item.getSemester()
                                    ));
                                }
                            } else {
                                showToast(result.getMessage());
                            }
                        } else {
                            showToast("Gagal mengambil data Pembayaran");
                        }

                        appendLocalPayments();
                        refreshPaymentSummary();
                    }

                    @Override
                    public void onFailure(@NonNull Call<PembayaranResponse> call,
                                          @NonNull Throwable t) {
                        showLoading(false);
                        showToast("Tidak dapat terhubung ke server");
                        listPembayaran.clear();
                        appendLocalPayments();
                        refreshPaymentSummary();
                    }
                });
    }

    private void showDetailPembayaranDialog(PembayaranModel item) {
        if (getContext() == null) return;

        new AlertDialog.Builder(requireContext())
                .setTitle(item.getJenisPembayaran())
                .setMessage(
                        "Nomor Transaksi: " + item.getNoTransaksi() + "\n" +
                        "Tanggal Bayar: " + item.getTanggalBayar() + "\n" +
                        "Nominal: " + item.getNominalFormatted() + "\n" +
                        "Status: " + item.getStatus() + "\n" +
                        "Semester: " + item.getSemester()
                )
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showLoading(boolean isLoading) {
        if (progressPembayaran == null) return;

        progressPembayaran.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rvPembayaran.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        btnBayarUkt.setEnabled(!isLoading);
    }

    private void showBayarUktDialog() {
        if (getContext() == null) return;

        if (sisaTagihan <= 0) {
            showToast("Tagihan UKT sudah lunas");
            return;
        }

        String[] paymentMethods = {
                "QRIS",
                "BNI Virtual Account",
                "BRI Virtual Account"
        };
        final int[] selectedMethod = {0};

        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        int horizontalPadding = dp(20);
        form.setPadding(horizontalPadding, dp(8), horizontalPadding, 0);

        TextView tvInfo = new TextView(requireContext());
        tvInfo.setText("Sisa tagihan: " + formatRupiah(sisaTagihan));
        tvInfo.setTextColor(ContextCompat.getColor(requireContext(), R.color.uap_text_primary));
        tvInfo.setTextSize(14f);

        EditText etNominal = new EditText(requireContext());
        etNominal.setHint("Nominal pembayaran");
        etNominal.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNominal.setSingleLine(true);

        form.addView(tvInfo);
        form.addView(etNominal);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Bayar UKT")
                .setSingleChoiceItems(paymentMethods, 0, (d, which) -> selectedMethod[0] = which)
                .setView(form)
                .setNegativeButton("Batal", (d, which) -> d.dismiss())
                .setPositiveButton("Bayar", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String nominalText = etNominal.getText().toString().trim();
                    if (nominalText.isEmpty()) {
                        showToast("Masukkan nominal pembayaran");
                        return;
                    }

                    long nominal = Long.parseLong(nominalText);
                    if (nominal <= 0) {
                        showToast("Nominal harus lebih dari 0");
                        return;
                    }

                    if (nominal > sisaTagihan) {
                        showToast("Nominal melebihi sisa tagihan");
                        return;
                    }

                    String method = paymentMethods[selectedMethod[0]];
                    PembayaranModel payment = new PembayaranModel(
                            "UKT Semester 5 - " + method,
                            getTodayLabel(),
                            generateTransactionNumber(method),
                            nominal,
                            nominal == sisaTagihan ? "Lunas" : "Cicilan",
                            5
                    );

                    localPayments.add(0, payment);
                    listPembayaran.add(0, payment);
                    sudahDibayar += nominal;
                    sisaTagihan -= nominal;

                    FeatureDataManager.saveLocalPayments(requireContext(), npm, localPayments);
                    refreshPaymentSummary();
                    dialog.dismiss();
                    showToast("Pembayaran berhasil dicatat");
                }));
        dialog.show();
    }

    private void appendLocalPayments() {
        if (getContext() == null) return;

        localPayments.clear();
        localPayments.addAll(FeatureDataManager.getLocalPayments(requireContext(), npm));
        listPembayaran.addAll(0, localPayments);

        long totalLocalPaid = 0;
        for (PembayaranModel payment : localPayments) {
            totalLocalPaid += payment.getNominal();
        }
        sudahDibayar += totalLocalPaid;
        sisaTagihan = Math.max(0, totalTagihan - sudahDibayar);
    }

    private void refreshPaymentSummary() {
        tvTotalTagihan.setText(formatRupiah(totalTagihan));
        tvSudahDibayar.setText(formatRupiah(sudahDibayar));
        tvSisaTagihan.setText(formatRupiah(sisaTagihan));

        if (sisaTagihan <= 0) {
            tvStatusLunas.setText("LUNAS");
            tvStatusLunas.getBackground().setTint(
                    ContextCompat.getColor(requireContext(), R.color.uap_success)
            );
            btnBayarUkt.setEnabled(false);
        } else {
            tvStatusLunas.setText("BELUM LUNAS");
            tvStatusLunas.getBackground().setTint(
                    ContextCompat.getColor(requireContext(), R.color.uap_error)
            );
            btnBayarUkt.setEnabled(true);
        }

        adapter.notifyDataSetChanged();
    }

    private String generateTransactionNumber(String method) {
        String prefix;
        if ("QRIS".equals(method)) {
            prefix = "QRIS";
        } else if (method.startsWith("BNI")) {
            prefix = "BNI";
        } else {
            prefix = "BRI";
        }
        return prefix + "-" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
    }

    private String getTodayLabel() {
        return new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")).format(new Date());
    }

    private String formatRupiah(long nominal) {
        String nominalStr = String.valueOf(nominal);
        StringBuilder result = new StringBuilder();
        int counter = 0;

        for (int i = nominalStr.length() - 1; i >= 0; i--) {
            if (counter > 0 && counter % 3 == 0) {
                result.insert(0, ".");
            }
            result.insert(0, nominalStr.charAt(i));
            counter++;
        }
        return "Rp " + result;
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
