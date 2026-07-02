package com.example.siakad.fragment;

import android.os.Bundle;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.siakad.MainActivity;
import com.example.siakad.ProfilePhotoManager;
import com.example.siakad.R;

public class DashboardFragment extends Fragment {

    private static final String ARG_NPM  = "ARG_NPM";
    private static final String ARG_NAMA = "ARG_NAMA";

    private static final String PRODI    = "Teknik Informatika";
    private static final String SEMESTER = "Semester 5";

    private String npm  = "";
    private String nama = "Mahasiswa";

    private TextView tvNamaMahasiswa, tvInisial;
    private TextView tvNpm, tvProdi, tvSemester;
    private ImageView ivFotoDashboard;
    private CardView cardKrs, cardKhs, cardAbsensi;
    private CardView cardPembayaran, cardInfo, cardJadwal;

    public static DashboardFragment newInstance(String npm, String nama) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NPM,  npm);
        args.putString(ARG_NAMA, nama);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            npm  = getArguments().getString(ARG_NPM,  npm);
            nama = getArguments().getString(ARG_NAMA, nama);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initViews(view);
        populateData();
        setupMenuClickListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfilePhoto();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            updateProfilePhoto();
        }
    }

    private void initViews(View view) {
        tvNamaMahasiswa = view.findViewById(R.id.tvNamaMahasiswa);
        tvInisial       = view.findViewById(R.id.tvInisial);
        ivFotoDashboard = view.findViewById(R.id.ivFotoDashboard);

        tvNpm      = view.findViewById(R.id.tvNpm);
        tvProdi    = view.findViewById(R.id.tvProdi);
        tvSemester = view.findViewById(R.id.tvSemester);

        cardKrs        = view.findViewById(R.id.cardKrs);
        cardKhs        = view.findViewById(R.id.cardKhs);
        cardAbsensi    = view.findViewById(R.id.cardAbsensi);
        cardPembayaran = view.findViewById(R.id.cardPembayaran);
        cardInfo       = view.findViewById(R.id.cardInfo);
        cardJadwal     = view.findViewById(R.id.cardJadwal);
    }

    private void populateData() {
        tvNamaMahasiswa.setText(nama);
        tvInisial.setText(getInisial(nama));
        tvNpm.setText(": " + npm);
        tvProdi.setText(": " + PRODI);
        tvSemester.setText(": " + SEMESTER);
        updateProfilePhoto();
    }

    private void updateProfilePhoto() {
        if (getContext() == null || ivFotoDashboard == null || tvInisial == null) {
            return;
        }

        String photoUri = ProfilePhotoManager.getProfilePhotoUri(requireContext(), npm);
        if (photoUri == null || photoUri.isEmpty()) {
            ivFotoDashboard.setVisibility(View.GONE);
            tvInisial.setVisibility(View.VISIBLE);
            return;
        }

        ivFotoDashboard.setImageURI(Uri.parse(photoUri));
        ivFotoDashboard.setVisibility(View.VISIBLE);
        tvInisial.setVisibility(View.GONE);
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

    private void setupMenuClickListeners() {

        cardKrs.setOnClickListener(v ->
                bukaFragment(KrsFragment.newInstance(npm))
        );

        cardKhs.setOnClickListener(v ->
                bukaFragment(KhsFragment.newInstance(npm))
        );

        cardAbsensi.setOnClickListener(v ->
                bukaFragment(AbsensiFragment.newInstance(npm))
        );

        cardPembayaran.setOnClickListener(v ->
                bukaFragment(PembayaranFragment.newInstance(npm))
        );

        cardInfo.setOnClickListener(v ->
                bukaFragment(new InformasiFragment())
        );

        cardJadwal.setOnClickListener(v ->
                bukaFragment(JadwalFragment.newInstance(npm))
        );
    }

    private void bukaFragment(Fragment fragment) {
        ((MainActivity) requireActivity()).openFeatureFragment(fragment);
    }
}
