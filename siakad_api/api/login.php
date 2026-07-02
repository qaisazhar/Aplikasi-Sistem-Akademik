<?php
require_once '../config/database.php';
setHeaders();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(['status' => false, 'message' => 'Method tidak diizinkan']);
    exit();
}

// Ambil data dari request body JSON
$input    = json_decode(file_get_contents('php://input'), true);
$npm      = isset($input['npm'])      ? trim($input['npm'])      : '';
$password = isset($input['password']) ? trim($input['password']) : '';

if (empty($npm) || empty($password)) {
    echo json_encode(['status' => false, 'message' => 'NPM dan Password wajib diisi']);
    exit();
}

$conn = getConnection();

// Query cek akun — password di-hash MD5
$passwordHash = $password;
$stmt = $conn->prepare(
    "SELECT npm, nama, prodi, fakultas, semester, tahun_masuk,
            status, no_hp, alamat
     FROM mahasiswa
     WHERE npm = ? AND password = ?"
);
$stmt->bind_param('ss', $npm, $passwordHash);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $mahasiswa = $result->fetch_assoc();
    echo json_encode([
        'status'  => true,
        'message' => 'Login berhasil',
        'data'    => $mahasiswa
    ]);
} else {
    echo json_encode([
        'status'  => false,
        'message' => 'NPM atau Password salah'
    ]);
}

$stmt->close();
$conn->close();