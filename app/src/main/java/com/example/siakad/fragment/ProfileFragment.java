package com.example.siakad.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.siakad.LoginActivity;
import com.example.siakad.ProfilePhotoManager;
import com.example.siakad.R;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private static final String ARG_NPM = "ARG_NPM";
    private static final String ARG_NAMA = "ARG_NAMA";

    private TextView tvFotoInisial;
    private ImageView ivFotoProfil;
    private TextView tvNamaProfil;
    private TextView tvNpmProfil;

    private View rowProdi;
    private View rowFakultas;
    private View rowSemester;
    private View rowTahunMasuk;
    private View rowStatus;

    private View rowEmail;
    private View rowNoHp;
    private View rowAlamat;

    private MaterialButton btnUbahFotoProfil;
    private MaterialButton btnLogout;

    private String npm = "2210631170001";
    private String nama = "Budi Santoso";

    private static final String PRODI = "S1 Teknik Informatika";
    private static final String FAKULTAS = "Fakultas Teknologi & Informatika";
    private static final String SEMESTER = "5 (Lima)";
    private static final String TAHUN_MASUK = "2022";
    private static final String STATUS = "Aktif";
    private static final String NO_HP = "081234567890";
    private static final String ALAMAT = "Pringsewu, Lampung";

    private ActivityResultLauncher<String[]> pilihFotoLauncher;

    public static ProfileFragment newInstance(String npm, String nama) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NPM, npm);
        args.putString(ARG_NAMA, nama);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            npm = getArguments().getString(ARG_NPM, npm);
            nama = getArguments().getString(ARG_NAMA, nama);
        }

        pilihFotoLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri == null || getContext() == null) {
                        return;
                    }

                    try {
                        requireContext().getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                    } catch (SecurityException ignored) {
                    }

                    ProfilePhotoManager.saveProfilePhotoUri(
                            requireContext(),
                            npm,
                            uri.toString()
                    );
                    updateProfilePhoto();
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        populateData();
        setupPhotoButton();
        setupLogoutButton();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            updateProfilePhoto();
        }
    }

    private void initViews(View view) {
        tvFotoInisial = view.findViewById(R.id.tvFotoInisial);
        ivFotoProfil = view.findViewById(R.id.ivFotoProfil);
        tvNamaProfil = view.findViewById(R.id.tvNamaProfil);
        tvNpmProfil = view.findViewById(R.id.tvNpmProfil);

        rowProdi = view.findViewById(R.id.rowProdi);
        rowFakultas = view.findViewById(R.id.rowFakultas);
        rowSemester = view.findViewById(R.id.rowSemester);
        rowTahunMasuk = view.findViewById(R.id.rowTahunMasuk);
        rowStatus = view.findViewById(R.id.rowStatus);

        rowEmail = view.findViewById(R.id.rowEmail);
        rowNoHp = view.findViewById(R.id.rowNoHp);
        rowAlamat = view.findViewById(R.id.rowAlamat);

        btnUbahFotoProfil = view.findViewById(R.id.btnUbahFotoProfil);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void populateData() {
        tvFotoInisial.setText(getInisial(nama));
        tvNamaProfil.setText(nama);
        tvNpmProfil.setText(npm);
        updateProfilePhoto();

        String emailDynamic = generateEmail(nama);

        setRow(rowProdi, R.drawable.ic_school, "Program Studi", PRODI);
        setRow(rowFakultas, R.drawable.ic_school, "Fakultas", FAKULTAS);
        setRow(rowSemester, R.drawable.ic_calendar, "Semester", SEMESTER);
        setRow(rowTahunMasuk, R.drawable.ic_star, "Tahun Masuk", TAHUN_MASUK);
        setRow(rowStatus, R.drawable.ic_badge, "Status", STATUS);

        setRow(rowEmail, R.drawable.ic_info, "Email", emailDynamic);
        setRow(rowNoHp, R.drawable.ic_person, "No. HP", NO_HP);
        setRow(rowAlamat, R.drawable.ic_location, "Alamat", ALAMAT);
    }

    private void setupPhotoButton() {
        btnUbahFotoProfil.setOnClickListener(v ->
                pilihFotoLauncher.launch(new String[]{"image/*"})
        );
    }

    private void updateProfilePhoto() {
        if (getContext() == null || ivFotoProfil == null || tvFotoInisial == null) {
            return;
        }

        String photoUri = ProfilePhotoManager.getProfilePhotoUri(requireContext(), npm);
        if (photoUri == null || photoUri.isEmpty()) {
            ivFotoProfil.setVisibility(View.GONE);
            tvFotoInisial.setVisibility(View.VISIBLE);
            return;
        }

        ivFotoProfil.setImageURI(Uri.parse(photoUri));
        ivFotoProfil.setVisibility(View.VISIBLE);
        tvFotoInisial.setVisibility(View.GONE);
    }

    private String generateEmail(String namaLengkap) {
        if (namaLengkap == null || namaLengkap.isEmpty()) {
            return "mahasiswa@mhs.aisyahpringsewu.ac.id";
        }
        String emailPrefix = namaLengkap.trim()
                .toLowerCase()
                .replaceAll("\\s+", ".");
        return emailPrefix + "@mhs.aisyahpringsewu.ac.id";
    }

    private void setRow(View row, int iconResId, String label, String value) {
        if (row == null) return;

        ImageView icon = row.findViewById(R.id.ivRowIcon);
        TextView tvLabel = row.findViewById(R.id.tvRowLabel);
        TextView tvValue = row.findViewById(R.id.tvRowValue);

        icon.setImageResource(iconResId);
        tvLabel.setText(label);
        tvValue.setText(value);
    }

    private String getInisial(String namaLengkap) {
        if (namaLengkap == null || namaLengkap.isEmpty()) return "?";

        StringBuilder inisial = new StringBuilder();
        String[] parts = namaLengkap.trim().split("\\s+");

        for (int i = 0; i < Math.min(2, parts.length); i++) {
            if (!parts[i].isEmpty()) {
                inisial.append(parts[i].charAt(0));
            }
        }
        return inisial.toString().toUpperCase();
    }

    private void setupLogoutButton() {
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setIcon(R.drawable.ic_logout)
                .setPositiveButton("Ya, Keluar", (dialog, which) -> {
                    dialog.dismiss();
                    processLogout();
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void processLogout() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
