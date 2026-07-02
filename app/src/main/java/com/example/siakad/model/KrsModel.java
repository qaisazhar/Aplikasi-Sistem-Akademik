package com.example.siakad.model;

public class KrsModel {

    private int    nomor;
    private String namaMatkul;
    private String kodeMatkul;
    private String dosenMatkul;
    private int    sks;

    public KrsModel(int nomor, String namaMatkul,
                    String kodeMatkul, String dosenMatkul, int sks) {
        this.nomor       = nomor;
        this.namaMatkul  = namaMatkul;
        this.kodeMatkul  = kodeMatkul;
        this.dosenMatkul = dosenMatkul;
        this.sks         = sks;
    }

    public int    getNomor()       { return nomor; }
    public String getNamaMatkul()  { return namaMatkul; }
    public String getKodeMatkul()  { return kodeMatkul; }
    public String getDosenMatkul() { return dosenMatkul; }
    public int    getSks()         { return sks; }
}
