CREATE DATABASE IF NOT EXISTS donations_db;
USE donations_db;

---
## Tabela de Usuários (users)
---
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    address VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    bio TEXT,
    profile_image_url VARCHAR(255),
    role ENUM('DONOR', 'REQUESTER', 'ADMIN') NOT NULL DEFAULT 'DONOR',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6)
);

---
## Tabela de Doações (donations)
---
CREATE TABLE IF NOT EXISTS donations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    expiration_date DATE,
    perishable BOOLEAN DEFAULT FALSE,
    storage_instructions TEXT,
    location VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    status ENUM('AVAILABLE', 'RESERVED', 'COMPLETED', 'EXPIRED', 'CANCELLED') NOT NULL,
    image_urls TEXT,
    pickup_instructions TEXT,
    expires_at DATETIME(6),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    donor_id BIGINT NOT NULL,
    CONSTRAINT fk_donation_donor FOREIGN KEY (donor_id) REFERENCES users(id)
);

---
## Tabela de Requisições de Doação (matches)
---
CREATE TABLE IF NOT EXISTS matches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL,
    requested_at DATETIME(6) NOT NULL,
    responded_at DATETIME(6),
    completed_at DATETIME(6),
    pickup_date DATETIME(6),
    pickup_notes TEXT,
    donor_notes TEXT,
    requester_rating INTEGER,
    donor_rating INTEGER,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    donation_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    CONSTRAINT fk_match_donation FOREIGN KEY (donation_id) REFERENCES donations(id),
    CONSTRAINT fk_match_requester FOREIGN KEY (requester_id) REFERENCES users(id)
);