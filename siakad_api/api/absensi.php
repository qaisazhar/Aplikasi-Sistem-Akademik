<?php
require_once '../config/database.php';
setHeaders();

$npm      = isset($_GET['npm'])      ? trim($_GET['npm'])        : '';
$semester = isset($_GET['semester']) ? intval($_GET['semester'])  : 5;

if (empty($npm)) {
    echo json_encode(['status' => false, 'message' => 'NPM wajib diisi']);
    exit();
}

$conn = getConnection();

$stmt = $conn->prepare(
    "SELECT m.kode, m.nama,
            a.hadir, a.izin, a.sakit, a.alpa, a.total_pertemuan
     FROM absensi a
     JOIN mata_kuliah m ON a.kode_matkul = m.kode
     WHERE a.npm = ? AND a.semester = ?
     ORDER BY m.kode ASC"
);
$stmt->bind_param('si', $npm, $semester);
$stmt->execute();
$result = $stmt->get_result();

$listAbsensi = [];
$totalHadir  = 0;
$totalIzin   = 0;
$totalAlpa   = 0;

while ($row = $result->fetch_assoc()) {
    // Hitung persentase kehadiran
    $kehadiran = $row['hadir'] + $row['izin'] + $row['sakit'];
    $persen    = $row['total_pertemuan'] > 0
                 ? round(($kehadiran / $row['total_pertemuan']) * 100)
                 : 0;
    $row['persen_kehadiran'] = $persen;
    $row['is_aman']          = $persen >= 75;

    $totalHadir += $row['hadir'];
    $totalIzin  += $row['izin'] + $row['sakit'];
    $totalAlpa  += $row['alpa'];

    $listAbsensi[] = $row;
}

echo json_encode([
    'status'       => true,
    'message'      => 'Data absensi berhasil diambil',
    'total_hadir'  => $totalHadir,
    'total_izin'   => $totalIzin,
    'total_alpa'   => $totalAlpa,
    'data'         => $listAbsensi
]);

$stmt->close();
$conn->close();