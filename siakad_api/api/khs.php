<?php
require_once '../config/database.php';
setHeaders();

$npm      = isset($_GET['npm'])      ? trim($_GET['npm'])      : '';
$semester = isset($_GET['semester']) ? intval($_GET['semester']) : 5;

if (empty($npm)) {
    echo json_encode(['status' => false, 'message' => 'NPM wajib diisi']);
    exit();
}

$conn = getConnection();

// Ambil KHS per semester
$stmt = $conn->prepare(
    "SELECT m.kode, m.nama, m.sks,
            k.nilai_huruf, k.nilai_angka, k.semester
     FROM khs k
     JOIN mata_kuliah m ON k.kode_matkul = m.kode
     WHERE k.npm = ? AND k.semester = ?
     ORDER BY m.kode ASC"
);
$stmt->bind_param('si', $npm, $semester);
$stmt->execute();
$result = $stmt->get_result();

$listKhs  = [];
$totalBobot = 0;
$totalSks   = 0;

while ($row = $result->fetch_assoc()) {
    $totalBobot += $row['nilai_angka'] * $row['sks'];
    $totalSks   += $row['sks'];
    $listKhs[]   = $row;
}

$ips = $totalSks > 0 ? round($totalBobot / $totalSks, 2) : 0;

// Hitung IPK dari semua semester
$stmtIpk = $conn->prepare(
    "SELECT SUM(k.nilai_angka * m.sks) as total_bobot,
            SUM(m.sks) as total_sks
     FROM khs k
     JOIN mata_kuliah m ON k.kode_matkul = m.kode
     WHERE k.npm = ?"
);
$stmtIpk->bind_param('s', $npm);
$stmtIpk->execute();
$resultIpk  = $stmtIpk->get_result();
$rowIpk     = $resultIpk->fetch_assoc();
$ipk        = $rowIpk['total_sks'] > 0
              ? round($rowIpk['total_bobot'] / $rowIpk['total_sks'], 2)
              : 0;
$sksLulus   = $rowIpk['total_sks'] ?? 0;

echo json_encode([
    'status'    => true,
    'message'   => 'Data KHS berhasil diambil',
    'ipk'       => $ipk,
    'ips'       => $ips,
    'sks_lulus' => $sksLulus,
    'data'      => $listKhs
]);

$stmt->close();
$stmtIpk->close();
$conn->close();