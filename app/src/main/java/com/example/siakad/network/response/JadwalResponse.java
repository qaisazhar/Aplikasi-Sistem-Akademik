package com.example.siakad.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JadwalResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<DataJadwal> data;

    public boolean          isStatus()  { return status; }
    public String           getMessage(){ return message; }
    public List<DataJadwal> getData()   { return data; }

    public static class DataJadwal {

        @SerializedName("kode")
        private String kode;

        @SerializedName("nama")
        private String nama;

        @SerializedName("sks")
        private int sks;

        @SerializedName("dosen")
        private String dosen;

        @SerializedName("hari")
        private String hari;

        @SerializedName("jam_mulai")
        private String jamMulai;

        @SerializedName("jam_selesai")
        private String jamSelesai;

        @SerializedName("ruangan")
        private String ruangan;

        public String getKode()       { return kode; }
        public String getNama()       { return nama; }
        public int    getSks()        { return sks; }
        public String getDosen()      { return dosen; }
        public String getHari()       { return hari; }
        public String getJamMulai()   { return jamMulai; }
        public String getJamSelesai() { return jamSelesai; }
        public String getRuangan()    { return ruangan; }
    }
}