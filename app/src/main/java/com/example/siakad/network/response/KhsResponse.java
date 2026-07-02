package com.example.siakad.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KhsResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("ipk")
    private double ipk;

    @SerializedName("ips")
    private double ips;

    @SerializedName("sks_lulus")
    private int sksLulus;

    @SerializedName("data")
    private List<DataKhs> data;

    public boolean       isStatus()  { return status; }
    public String        getMessage(){ return message; }
    public double        getIpk()    { return ipk; }
    public double        getIps()    { return ips; }
    public int           getSksLulus(){ return sksLulus; }
    public List<DataKhs> getData()   { return data; }

    public static class DataKhs {

        @SerializedName("kode")
        private String kode;

        @SerializedName("nama")
        private String nama;

        @SerializedName("sks")
        private int sks;

        @SerializedName("nilai_huruf")
        private String nilaiHuruf;

        @SerializedName("nilai_angka")
        private double nilaiAngka;

        @SerializedName("semester")
        private int semester;

        public String getKode()       { return kode; }
        public String getNama()       { return nama; }
        public int    getSks()        { return sks; }
        public String getNilaiHuruf() { return nilaiHuruf; }
        public double getNilaiAngka() { return nilaiAngka; }
        public int    getSemester()   { return semester; }
    }
}