package com.example.siakad.model;

public class NotifikasiModel {

    private String judul;
    private String konten;
    private String tanggal;
    private String kategori;
    private int    ikonResId;
    private int    ikonWarna;
    private boolean sudahDibaca;

    public NotifikasiModel(String judul, String konten, String tanggal,
                           String kategori, int ikonResId,
                           int ikonWarna, boolean sudahDibaca) {
        this.judul       = judul;
        this.konten      = konten;
        this.tanggal     = tanggal;
        this.kategori    = kategori;
        this.ikonResId   = ikonResId;
        this.ikonWarna   = ikonWarna;
        this.sudahDibaca = sudahDibaca;
    }

    public String  getJudul()       { return judul; }
    public String  getKonten()      { return konten; }
    public String  getTanggal()     { return tanggal; }
    public String  getKategori()    { return kategori; }
    public int     getIkonResId()   { return ikonResId; }
    public int     getIkonWarna()   { return ikonWarna; }
    public boolean isSudahDibaca()  { return sudahDibaca; }

    public void setSudahDibaca(boolean sudahDibaca) {
        this.sudahDibaca = sudahDibaca;
    }
}