package com.example.siakad.model;

public class AbsensiModel {

    private String namaMatkul;
    private String kodeMatkul;
    private int    hadir;
    private int    izin;
    private int    sakit;
    private int    alpa;
    private int    totalPertemuan;

    public AbsensiModel(String namaMatkul, String kodeMatkul,
                        int hadir, int izin, int sakit,
                        int alpa, int totalPertemuan) {
        this.namaMatkul     = namaMatkul;
        this.kodeMatkul     = kodeMatkul;
        this.hadir          = hadir;
        this.izin           = izin;
        this.sakit          = sakit;
        this.alpa           = alpa;
        this.totalPertemuan = totalPertemuan;
    }

    public String getNamaMatkul()     { return namaMatkul; }
    public String getKodeMatkul()     { return kodeMatkul; }
    public int    getHadir()          { return hadir; }
    public int    getIzin()           { return izin; }
    public int    getSakit()          { return sakit; }
    public int    getAlpa()           { return alpa; }
    public int    getTotalPertemuan() { return totalPertemuan; }

    public void addKehadiran(int hadir, int izin, int sakit, int totalPertemuan) {
        this.hadir += hadir;
        this.izin += izin;
        this.sakit += sakit;
        this.totalPertemuan += totalPertemuan;
    }

    public int getPersenKehadiran() {
        if (totalPertemuan == 0) return 0;
        int kehadiran = hadir + izin + sakit;
        return (kehadiran * 100) / totalPertemuan;
    }

    public boolean isAman() {
        return getPersenKehadiran() >= 75;
    }
}
