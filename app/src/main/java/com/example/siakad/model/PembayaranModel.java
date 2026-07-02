package com.example.siakad.model;

public class PembayaranModel {

    private String jenisPembayaran;
    private String tanggalBayar;
    private String noTransaksi;
    private long   nominal;
    private String status;
    private int    semester;

    public PembayaranModel(String jenisPembayaran, String tanggalBayar,
                           String noTransaksi, long nominal,
                           String status, int semester) {
        this.jenisPembayaran = jenisPembayaran;
        this.tanggalBayar    = tanggalBayar;
        this.noTransaksi     = noTransaksi;
        this.nominal         = nominal;
        this.status          = status;
        this.semester        = semester;
    }

    public String getJenisPembayaran() { return jenisPembayaran; }
    public String getTanggalBayar()    { return tanggalBayar; }
    public String getNoTransaksi()     { return noTransaksi; }
    public long   getNominal()         { return nominal; }
    public String getStatus()          { return status; }
    public int    getSemester()        { return semester; }

    public String getNominalFormatted() {
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
        return "Rp " + result.toString();
    }
}