<?php
require_once '../config/database.php';
setHeaders();

$npm      = isset($_GET['npm'])  ? trim($_GET['npm'])  : '';
$hari     = isset($_GET['hari']) ? trim($_GET['hari']) : '';
$semester = isset($_GET['semester']) ? intval($_GET['semester']) : 5;

if (empty($npm)) {
    echo json_encode(['status' => false, 'message' => 'NPM wajib diisi']);
    exit();
}

$conn = getConnection();

// Ambil jadwal berdasarkan KRS mahasiswa
$query = "SELECT m.kode, m.nama, m.sks, m.dosen,
                 j.hari,
                 TIME_FORMAT(j.jam_mulai,   '%H.%i') as jam_mulai,
                 TIME_FORMAT(j.jam_selesai, '%H.%i') as jam_selesai,
                 j.ruangan
          FROM jadwal j
          JOIN mata_kuliah m  ON j.kode_matkul = m.kode
          JOIN krs k          ON k.kode_matkul = m.kode
          WHERE k.npm = ? AND j.semester = ?";

// Filter hari jika ada
if (!empty($hari)) {
    $query .= " AND j.hari = ?";
}

$query .= " ORDER BY FIELD(j.hari,'Senin','Selasa','Rabu','Kamis','Jumat'),
                     j.jam_mulai ASC";

if (!empty($hari)) {
    $stmt = $conn->prepare($query);
    $stmt->bind_param('sis', $npm, $semester, $hari);
} else {
    $stmt = $conn->prepare($query);
    $stmt->bind_param('si', $npm, $semester);
}

$stmt->execute();
$result = $stmt->get_result();

$listJadwal = [];
while ($row = $result->fetch_assoc()) {
    $listJadwal[] = $row;
}

echo json_encode([
    'status'  => true,
    'message' => 'Data jadwal berhasil diambil',
    'data'    => $listJadwal
]);

$stmt->close();
$conn->close();