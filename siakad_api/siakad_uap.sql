-- ===== BUAT DATABASE =====
CREATE DATABASE IF NOT EXISTS siakad_uap
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE siakad_uap;

-- ===== TABEL MAHASISWA =====
CREATE TABLE mahasiswa (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    npm         VARCHAR(20) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    nama        VARCHAR(100) NOT NULL,
    prodi       VARCHAR(100) DEFAULT 'S1 Teknik Informatika',
    fakultas    VARCHAR(100) DEFAULT 'Fakultas Teknologi & Informatika',
    semester    INT DEFAULT 5,
    tahun_masuk VARCHAR(4) DEFAULT '2022',
    status      VARCHAR(20) DEFAULT 'Aktif',
    no_hp       VARCHAR(20),
    alamat      VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== TABEL MATA KULIAH =====
CREATE TABLE mata_kuliah (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    kode        VARCHAR(10) NOT NULL UNIQUE,
    nama        VARCHAR(100) NOT NULL,
    sks         INT NOT NULL,
    semester    INT NOT NULL,
    dosen       VARCHAR(100)
);

-- ===== TABEL KRS =====
CREATE TABLE krs (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    npm         VARCHAR(20) NOT NULL,
    kode_matkul VARCHAR(10) NOT NULL,
    tahun_ajaran VARCHAR(9) DEFAULT '2025/2026',
    semester_ajaran VARCHAR(10) DEFAULT 'Ganjil',
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm),
    FOREIGN KEY (kode_matkul) REFERENCES mata_kuliah(kode)
);

-- ===== TABEL KHS =====
CREATE TABLE khs (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    npm          VARCHAR(20) NOT NULL,
    kode_matkul  VARCHAR(10) NOT NULL,
    nilai_huruf  VARCHAR(3),
    nilai_angka  DECIMAL(3,2),
    semester     INT NOT NULL,
    tahun_ajaran VARCHAR(9),
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm),
    FOREIGN KEY (kode_matkul) REFERENCES mata_kuliah(kode)
);

-- ===== TABEL ABSENSI =====
CREATE TABLE absensi (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    npm            VARCHAR(20) NOT NULL,
    kode_matkul    VARCHAR(10) NOT NULL,
    hadir          INT DEFAULT 0,
    izin           INT DEFAULT 0,
    sakit          INT DEFAULT 0,
    alpa           INT DEFAULT 0,
    total_pertemuan INT DEFAULT 0,
    semester       INT NOT NULL,
    tahun_ajaran   VARCHAR(9),
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm),
    FOREIGN KEY (kode_matkul) REFERENCES mata_kuliah(kode)
);

-- ===== TABEL PEMBAYARAN =====
CREATE TABLE pembayaran (
    id               INT PRIMARY KEY AUTO_INCREMENT,
    npm              VARCHAR(20) NOT NULL,
    jenis            VARCHAR(100) NOT NULL,
    tanggal_bayar    DATE,
    no_transaksi     VARCHAR(50) UNIQUE,
    nominal          BIGINT NOT NULL,
    status           VARCHAR(20) DEFAULT 'Belum Lunas',
    semester         INT,
    tahun_ajaran     VARCHAR(9),
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm)
);

-- ===== TABEL JADWAL =====
CREATE TABLE jadwal (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    kode_matkul VARCHAR(10) NOT NULL,
    hari        VARCHAR(10) NOT NULL,
    jam_mulai   TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    ruangan     VARCHAR(50),
    semester    INT,
    tahun_ajaran VARCHAR(9),
    FOREIGN KEY (kode_matkul) REFERENCES mata_kuliah(kode)
);


-- ===== DATA MAHASISWA =====
INSERT INTO mahasiswa (npm, password, nama, no_hp, alamat) VALUES
('2210631170001', MD5('budi123'),  'Budi Santoso',  '081234567890', 'Pringsewu, Lampung'),
('2210631170002', MD5('siti456'),  'Siti Rahayu',   '082345678901', 'Pringsewu, Lampung'),
('2210631170003', MD5('ahmad789'), 'Ahmad Fauzi',   '083456789012', 'Pringsewu, Lampung'),
('admin',         MD5('admin123'), 'Administrator UAP', '081111111111', 'UAP, Pringsewu');

-- ===== DATA MATA KULIAH =====
INSERT INTO mata_kuliah (kode, nama, sks, semester, dosen) VALUES
('TI501', 'Mobile Programming 1',       3, 5, 'Ratnasari, S.Kom., M.Kom'),
('TI502', 'Rekayasa Perangkat Lunak',   3, 5, 'Andi Wijaya, S.T., M.T.'),
('TI503', 'Basis Data Lanjut',          3, 5, 'Siti Nurhaliza, S.Kom., M.Cs.'),
('TI504', 'Jaringan Komputer',          3, 5, 'Budi Hartono, S.T., M.Kom.'),
('TI505', 'Kecerdasan Buatan',          3, 5, 'Ahmad Fauzan, S.Kom., M.T.'),
('TI506', 'Metodologi Penelitian',      2, 5, 'Dewi Sartika, S.Pd., M.Pd.'),
('TI507', 'Praktikum Mobile Programming', 1, 5, 'Ratnasari, S.Kom., M.Kom'),
('TI508', 'Etika Profesi',              2, 5, 'Hendra Gunawan, S.H., M.H.');

-- ===== DATA KRS =====
INSERT INTO krs (npm, kode_matkul) VALUES
('2210631170001', 'TI501'),
('2210631170001', 'TI502'),
('2210631170001', 'TI503'),
('2210631170001', 'TI504'),
('2210631170001', 'TI505'),
('2210631170001', 'TI506'),
('2210631170001', 'TI507'),
('2210631170001', 'TI508');

-- ===== DATA KHS =====
INSERT INTO khs (npm, kode_matkul, nilai_huruf, nilai_angka, semester, tahun_ajaran) VALUES
('2210631170001', 'TI501', 'A',  4.00, 5, '2025/2026'),
('2210631170001', 'TI502', 'A',  4.00, 5, '2025/2026'),
('2210631170001', 'TI503', 'B+', 3.50, 5, '2025/2026'),
('2210631170001', 'TI504', 'A-', 3.75, 5, '2025/2026'),
('2210631170001', 'TI505', 'B+', 3.50, 5, '2025/2026'),
('2210631170001', 'TI506', 'A',  4.00, 5, '2025/2026');

-- ===== DATA ABSENSI =====
INSERT INTO absensi (npm, kode_matkul, hadir, izin, sakit, alpa, total_pertemuan, semester, tahun_ajaran) VALUES
('2210631170001', 'TI501', 12, 1, 1, 0, 14, 5, '2025/2026'),
('2210631170001', 'TI502', 13, 0, 0, 1, 14, 5, '2025/2026'),
('2210631170001', 'TI503', 14, 0, 0, 0, 14, 5, '2025/2026'),
('2210631170001', 'TI504', 10, 1, 0, 3, 14, 5, '2025/2026'),
('2210631170001', 'TI505', 13, 1, 0, 0, 14, 5, '2025/2026'),
('2210631170001', 'TI506', 11, 0, 1, 0, 12, 5, '2025/2026');

-- ===== DATA PEMBAYARAN =====
INSERT INTO pembayaran (npm, jenis, tanggal_bayar, no_transaksi, nominal, status, semester, tahun_ajaran) VALUES
('2210631170001', 'UKT Semester 5', '2025-08-15', 'TRX-20250815-001', 3500000, 'Lunas', 5, '2025/2026'),
('2210631170001', 'UKT Semester 4', '2025-02-10', 'TRX-20250210-001', 3500000, 'Lunas', 4, '2024/2025'),
('2210631170001', 'UKT Semester 3', '2024-08-12', 'TRX-20240812-001', 3200000, 'Lunas', 3, '2024/2025'),
('2210631170001', 'UKT Semester 2', '2024-02-08', 'TRX-20240208-001', 3200000, 'Lunas', 2, '2023/2024'),
('2210631170001', 'UKT Semester 1', '2023-08-05', 'TRX-20230805-001', 3000000, 'Lunas', 1, '2023/2024');

-- ===== DATA JADWAL =====
INSERT INTO jadwal (kode_matkul, hari, jam_mulai, jam_selesai, ruangan, semester, tahun_ajaran) VALUES
('TI501', 'Senin',  '07:30:00', '09:10:00', 'Lab. Komputer 1', 5, '2025/2026'),
('TI502', 'Senin',  '09:30:00', '11:10:00', 'Ruang B-201',     5, '2025/2026'),
('TI506', 'Senin',  '13:00:00', '14:40:00', 'Ruang A-101',     5, '2025/2026'),
('TI503', 'Selasa', '07:30:00', '09:10:00', 'Lab. Komputer 2', 5, '2025/2026'),
('TI505', 'Selasa', '10:00:00', '11:40:00', 'Ruang B-202',     5, '2025/2026'),
('TI504', 'Rabu',   '08:00:00', '09:40:00', 'Lab. Jaringan',   5, '2025/2026'),
('TI508', 'Rabu',   '10:00:00', '10:50:00', 'Ruang A-102',     5, '2025/2026'),
('TI507', 'Kamis',  '13:00:00', '15:30:00', 'Lab. Komputer 1', 5, '2025/2026');