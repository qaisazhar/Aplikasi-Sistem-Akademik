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
    "SELECT jenis, DATE_FORMAT(tanggal_bayar, '%d %M %Y') as tanggal_bayar,
            no_transaksi, nominal, status, semester
     FROM pembayaran
     WHERE npm = ?
     ORDER BY semester DESC"
);
$stmt->bind_param('s', $npm);
$stmt->execute();
$result = $stmt->get_result();

$listPembayaran = [];
$totalTagihan   = 3500000; // UKT semester ini
$sudahDibayar   = 0;

while ($row = $result->fetch_assoc()) {
    if ($row['semester'] == 5 && $row['status'] == 'Lunas') {
        $sudahDibayar += $row['nominal'];
    }
    $listPembayaran[] = $row;
}

echo json_encode([
    'status'        => true,
    'message'       => 'Data pembayaran berhasil diambil',
    'total_tagihan' => $totalTagihan,
    'sudah_dibayar' => $sudahDibayar,
    'sisa_tagihan'  => $totalTagihan - $sudahDibayar,
    'data'          => $listPembayaran
]);

$stmt->close();
$conn->close();