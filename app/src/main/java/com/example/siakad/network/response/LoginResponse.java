package com.example.siakad.network.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataMahasiswa data;

    public boolean isStatus()        { return status; }
    public String  getMessage()      { return message; }
    public DataMahasiswa getData()   { return data; }

    public static class DataMahasiswa {

        @SerializedName("npm")
        private String npm;

        @SerializedName("nama")
        private String nama;

        @SerializedName("prodi")
        private String prodi;

        @SerializedName("fakultas")
        private String fakultas;

        @SerializedName("semester")
        private int semester;

        @SerializedName("status")
        private String status;

        public String getNpm()      { return npm; }
        public String getNama()     { return nama; }
        public String getProdi()    { return prodi; }
        public String getFakultas() { return fakultas; }
        public int    getSemester() { return semester; }
        public String getStatus()   { return status; }
    }
}