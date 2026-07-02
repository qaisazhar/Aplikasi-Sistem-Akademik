<?php
require_once '../config/database.php';
setHeaders();

$npm = isset($_GET['npm']) ? trim($_GET['npm']) : '';

if (empty($npm)) {
    echo json_encode(['status' => false, 'message' => 'NPM wajib diisi']);
    exit();
}

$conn = getConnection();

$stmt = $conn->prepare(
    "SELECT m.kode, m.nama, m.sks, m.dosen,
            k.tahun_ajaran, k.semester_ajaran
     FROM krs k
     JOIN mata_kuliah m ON k.kode_matkul = m.kode
     WHERE k.npm = ?
     ORDER BY m.kode ASC"
);
$stmt->bind_param('s', $npm);
$stmt->execute();
$result = $stmt->get_result();

$listKrs = [];
$totalSks = 0;
$nomor = 1;

while ($row = $result->fetch_assoc()) {
    $row['nomor'] = $nomor++;
    $totalSks += $row['sks'];
    $listKrs[] = $row;
}

echo json_encode([
    'status'    => true,
    'message'   => 'Data KRS berhasil diambil',
    'total_sks' => $totalSks,
    'data'      => $listKrs
]);

$stmt->close();
$conn->close();