package com.example.siakad.model;

public class KhsModel {

    private String namaMatkul;
    private String kodeMatkul;
    private int    sks;
    private String nilaiHuruf;
    private double nilaiAngka;
    private int    semester;

    public KhsModel(String namaMatkul, String kodeMatkul, int sks,
                    String nilaiHuruf, double nilaiAngka, int semester) {
        this.namaMatkul = namaMatkul;
        this.kodeMatkul = kodeMatkul;
        this.sks        = sks;
        this.nilaiHuruf = nilaiHuruf;
        this.nilaiAngka = nilaiAngka;
        this.semester   = semester;
    }

    public String getNamaMatkul() { return namaMatkul; }
    public String getKodeMatkul() { return kodeMatkul; }
    public int    getSks()        { return sks; }
    public String getNilaiHuruf() { return nilaiHuruf; }
    public double getNilaiAngka() { return nilaiAngka; }
    public int    getSemester()   { return semester; }
}