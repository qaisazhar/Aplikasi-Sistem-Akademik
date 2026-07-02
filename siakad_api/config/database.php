<?php
// Konfigurasi koneksi database
define('DB_HOST', '');
define('DB_USER', '');
define('DB_PASS', '');           
define('DB_NAME', '');

function getConnection() {
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

    if ($conn->connect_error) {
        http_response_code(500);
        echo json_encode([
            'status'  => false,
            'message' => 'Koneksi database gagal: ' . $conn->connect_error
        ]);
        exit();
    }

    $conn->set_charset('utf8mb4');
    return $conn;
}

// Header response JSON & CORS
function setHeaders() {
    header('Content-Type: application/json');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST');
    header('Access-Control-Allow-Headers: Content-Type');
}