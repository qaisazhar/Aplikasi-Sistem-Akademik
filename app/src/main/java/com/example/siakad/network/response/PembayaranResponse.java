package com.example.siakad.network.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PembayaranResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("total_tagihan")
    private long totalTagihan;

    @SerializedName("sudah_dibayar")
    private long sudahDibayar;

    @SerializedName("sisa_tagihan")
    private long sisaTagihan;

    @SerializedName("data")
    private List<DataPembayaran> data;

    public boolean              isStatus()       { return status; }
    public String               getMessage()     { return message; }
    public long                 getTotalTagihan(){ return totalTagihan; }
    public long                 getSudahDibayar(){ return sudahDibayar; }
    public long                 getSisaTagihan() { return sisaTagihan; }
    public List<DataPembayaran> getData()        { return data; }

    public static class DataPembayaran {

        @SerializedName("jenis")
        private String jenis;

        @SerializedName("tanggal_bayar")
        private String tanggalBayar;

        @SerializedName("no_transaksi")
        private String noTransaksi;

        @SerializedName("nominal")
        private long nominal;

        @SerializedName("status")
        private String status;

        @SerializedName("semester")
        private int semester;

        public String getJenis()        { return jenis; }
        public String getTanggalBayar() { return tanggalBayar; }
        public String getNoTransaksi()  { return noTransaksi; }
        public long   getNominal()      { return nominal; }
        public String getStatus()       { return status; }
        public int    getSemester()     { return semester; }
    }
}