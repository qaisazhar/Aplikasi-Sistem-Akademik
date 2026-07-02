package com.example.siakad.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KrsResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("total_sks")
    private int totalSks;

    @SerializedName("data")
    private List<DataKrs> data;

    public boolean      isStatus()  { return status; }
    public String       getMessage(){ return message; }
    public int          getTotalSks(){ return totalSks; }
    public List<DataKrs> getData()  { return data; }

    public static class DataKrs {

        @SerializedName("nomor")
        private int nomor;

        @SerializedName("kode")
        private String kode;

        @SerializedName("nama")
        private String nama;

        @SerializedName("sks")
        private int sks;

        @SerializedName("dosen")
        private String dosen;

        public int    getNomor() { return nomor; }
        public String getKode()  { return kode; }
        public String getNama()  { return nama; }
        public int    getSks()   { return sks; }
        public String getDosen() { return dosen; }
    }
}