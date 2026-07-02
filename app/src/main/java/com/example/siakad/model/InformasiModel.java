package com.example.siakad.model;

public class InformasiModel {

    private String judul;
    private String isi;
    private String tanggal;
    private String kategori;
    private int    iconResId;
    private int    iconWarna;
    private boolean isBaru;

    public InformasiModel(String judul, String isi, String tanggal,
                          String kategori, int iconResId,
                          int iconWarna, boolean isBaru) {
        this.judul      = judul;
        this.isi        = isi;
        this.tanggal    = tanggal;
        this.kategori   = kategori;
        this.iconResId  = iconResId;
        this.iconWarna  = iconWarna;
        this.isBaru     = isBaru;
    }

    public String  getJudul()     { return judul; }
    public String  getIsi()       { return isi; }
    public String  getTanggal()   { return tanggal; }
    public String  getKategori()  { return kategori; }
    public int     getIconResId() { return iconResId; }
    public int     getIconWarna() { return iconWarna; }
    public boolean isBaru()       { return isBaru; }
    public void    setBaru(boolean baru) { this.isBaru = baru; }
}