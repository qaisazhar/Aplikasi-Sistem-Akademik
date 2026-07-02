package com.example.siakad.model;

public class JadwalModel {

    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String namaMatkul;
    private String dosen;
    private String ruangan;
    private int    sks;

    public JadwalModel(String hari, String jamMulai, String jamSelesai,
                       String namaMatkul, String dosen,
                       String ruangan, int sks) {
        this.hari       = hari;
        this.jamMulai   = jamMulai;
        this.jamSelesai = jamSelesai;
        this.namaMatkul = namaMatkul;
        this.dosen      = dosen;
        this.ruangan    = ruangan;
        this.sks        = sks;
    }

    public String getHari()       { return hari; }
    public String getJamMulai()   { return jamMulai; }
    public String getJamSelesai() { return jamSelesai; }
    public String getNamaMatkul() { return namaMatkul; }
    public String getDosen()      { return dosen; }
    public String getRuangan()    { return ruangan; }
    public int    getSks()        { return sks; }
}