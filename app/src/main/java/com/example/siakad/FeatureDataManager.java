package com.example.siakad;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.siakad.model.KrsModel;
import com.example.siakad.model.PembayaranModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class FeatureDataManager {

    private static final String PREF_NAME = "student_feature_data";
    private static final String KEY_KRS = "krs_";
    private static final String KEY_ATTENDANCE = "attendance_";
    private static final String KEY_ATTENDANCE_HISTORY = "attendance_history_";
    private static final String KEY_PAYMENTS = "payments_";

    private FeatureDataManager() {
    }

    public static List<KrsModel> getLocalKrs(Context context, String npm, int startNomor) {
        List<KrsModel> result = new ArrayList<>();
        String rawJson = getPreferences(context).getString(key(KEY_KRS, npm), "[]");

        try {
            JSONArray jsonArray = new JSONArray(rawJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                result.add(new KrsModel(
                        startNomor + i,
                        item.optString("nama"),
                        item.optString("kode"),
                        item.optString("dosen"),
                        item.optInt("sks")
                ));
            }
        } catch (JSONException ignored) {
            return new ArrayList<>();
        }

        return result;
    }

    public static void saveLocalKrs(Context context, String npm, List<KrsModel> listKrs) {
        JSONArray jsonArray = new JSONArray();
        for (KrsModel item : listKrs) {
            JSONObject object = new JSONObject();
            try {
                object.put("nama", item.getNamaMatkul());
                object.put("kode", item.getKodeMatkul());
                object.put("dosen", item.getDosenMatkul());
                object.put("sks", item.getSks());
                jsonArray.put(object);
            } catch (JSONException ignored) {
            }
        }
        getPreferences(context).edit().putString(key(KEY_KRS, npm), jsonArray.toString()).apply();
    }

    public static AttendanceAdjustment getAttendanceAdjustment(
            Context context,
            String npm,
            String kodeMatkul
    ) {
        JSONObject allData = getJsonObject(context, key(KEY_ATTENDANCE, npm));
        JSONObject item = allData.optJSONObject(kodeMatkul);
        if (item == null) {
            return new AttendanceAdjustment(0, 0, 0, 0);
        }

        return new AttendanceAdjustment(
                item.optInt("hadir"),
                item.optInt("izin"),
                item.optInt("sakit"),
                item.optInt("total")
        );
    }

    public static boolean recordAttendance(
            Context context,
            String npm,
            String kodeMatkul,
            String status
    ) {
        String todayKey = kodeMatkul + "_" + getTodayKey();
        JSONObject history = getJsonObject(context, key(KEY_ATTENDANCE_HISTORY, npm));
        if (history.has(todayKey)) {
            return false;
        }

        JSONObject allData = getJsonObject(context, key(KEY_ATTENDANCE, npm));
        JSONObject item = allData.optJSONObject(kodeMatkul);
        if (item == null) {
            item = new JSONObject();
        }

        try {
            if ("Izin".equals(status)) {
                item.put("izin", item.optInt("izin") + 1);
            } else if ("Sakit".equals(status)) {
                item.put("sakit", item.optInt("sakit") + 1);
            } else {
                item.put("hadir", item.optInt("hadir") + 1);
            }
            item.put("total", item.optInt("total") + 1);
            allData.put(kodeMatkul, item);
            history.put(todayKey, status);
        } catch (JSONException ignored) {
            return false;
        }

        getPreferences(context)
                .edit()
                .putString(key(KEY_ATTENDANCE, npm), allData.toString())
                .putString(key(KEY_ATTENDANCE_HISTORY, npm), history.toString())
                .apply();
        return true;
    }

    public static List<PembayaranModel> getLocalPayments(Context context, String npm) {
        List<PembayaranModel> result = new ArrayList<>();
        String rawJson = getPreferences(context).getString(key(KEY_PAYMENTS, npm), "[]");

        try {
            JSONArray jsonArray = new JSONArray(rawJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                result.add(new PembayaranModel(
                        item.optString("jenis"),
                        item.optString("tanggal"),
                        item.optString("transaksi"),
                        item.optLong("nominal"),
                        item.optString("status"),
                        item.optInt("semester")
                ));
            }
        } catch (JSONException ignored) {
            return new ArrayList<>();
        }

        return result;
    }

    public static void saveLocalPayments(
            Context context,
            String npm,
            List<PembayaranModel> payments
    ) {
        JSONArray jsonArray = new JSONArray();
        for (PembayaranModel item : payments) {
            JSONObject object = new JSONObject();
            try {
                object.put("jenis", item.getJenisPembayaran());
                object.put("tanggal", item.getTanggalBayar());
                object.put("transaksi", item.getNoTransaksi());
                object.put("nominal", item.getNominal());
                object.put("status", item.getStatus());
                object.put("semester", item.getSemester());
                jsonArray.put(object);
            } catch (JSONException ignored) {
            }
        }
        getPreferences(context).edit().putString(key(KEY_PAYMENTS, npm), jsonArray.toString()).apply();
    }

    private static JSONObject getJsonObject(Context context, String key) {
        String rawJson = getPreferences(context).getString(key, "{}");
        try {
            return new JSONObject(rawJson);
        } catch (JSONException ignored) {
            return new JSONObject();
        }
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static String key(String prefix, String npm) {
        return prefix + (npm == null ? "" : npm);
    }

    private static String getTodayKey() {
        return new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
    }

    public static final class AttendanceAdjustment {
        public final int hadir;
        public final int izin;
        public final int sakit;
        public final int total;

        public AttendanceAdjustment(int hadir, int izin, int sakit, int total) {
            this.hadir = hadir;
            this.izin = izin;
            this.sakit = sakit;
            this.total = total;
        }
    }
}
