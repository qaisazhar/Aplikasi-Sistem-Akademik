package com.example.siakad.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AbsensiResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("total_hadir")
    private int totalHadir;

    @SerializedName("total_izin")
    private int totalIzin;

    @SerializedName("total_alpa")
    private int totalAlpa;

    @SerializedName("data")
    private List<DataAbsensi> data;

    public boolean           isStatus()    { return status; }
    public String            getMessage()  { return message; }
    public int               getTotalHadir(){ return totalHadir; }
    public int               getTotalIzin() { return totalIzin; }
    public int               getTotalAlpa() { return totalAlpa; }
    public List<DataAbsensi> getData()     { return data; }

    public static class DataAbsensi {

        @SerializedName("kode")
        private String kode;

        @SerializedName("nama")
        private String nama;

        @SerializedName("hadir")
        private int hadir;

        @SerializedName("izin")
        private int izin;

        @SerializedName("sakit")
        private int sakit;

        @SerializedName("alpa")
        private int alpa;

        @SerializedName("total_pertemuan")
        private int totalPertemuan;

        @SerializedName("persen_kehadiran")
        private int persenKehadiran;

        @SerializedName("is_aman")
        private boolean isAman;

        public String getKode()            { return kode; }
        public String getNama()            { return nama; }
        public int    getHadir()           { return hadir; }
        public int    getIzin()            { return izin; }
        public int    getSakit()           { return sakit; }
        public int    getAlpa()            { return alpa; }
        public int    getTotalPertemuan()  { return totalPertemuan; }
        public int    getPersenKehadiran() { return persenKehadiran; }
        public boolean isAman()            { return isAman; }
    }
}