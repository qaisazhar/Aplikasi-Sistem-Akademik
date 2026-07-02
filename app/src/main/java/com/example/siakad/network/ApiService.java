package com.example.siakad.network;

import com.example.siakad.network.response.AbsensiResponse;
import com.example.siakad.network.response.JadwalResponse;
import com.example.siakad.network.response.KhsResponse;
import com.example.siakad.network.response.KrsResponse;
import com.example.siakad.network.response.LoginResponse;
import com.example.siakad.network.response.PembayaranResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @POST("api/login.php")
    Call<LoginResponse> login(@Body Map<String, String> body);

    @GET("api/krs.php")
    Call<KrsResponse> getKrs(@Query("npm") String npm);

    @GET("api/khs.php")
    Call<KhsResponse> getKhs(
            @Query("npm") String npm,
            @Query("semester") int semester
    );

    @GET("api/absensi.php")
    Call<AbsensiResponse> getAbsensi(
            @Query("npm") String npm,
            @Query("semester") int semester
    );

    @GET("api/pembayaran.php")
    Call<PembayaranResponse> getPembayaran(@Query("npm") String npm);

    @GET("api/jadwal.php")
    Call<JadwalResponse> getJadwal(
            @Query("npm") String npm,
            @Query("hari") String hari,
            @Query("semester") int semester
    );
}