
create database java_demo;

use java_demo;

-- ========================
-- 1. USERS - Thông tin đăng nhập
-- ========================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    full_name VARCHAR(100),
    active BOOLEAN DEFAULT true
);

-- ========================
-- 2. ROLES - Vai trò người dùng
-- ========================
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL -- Ví dụ: ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN
);

-- ========================
-- 3. USER_ROLES - Gán nhiều vai trò cho 1 người dùng
-- ========================
CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ========================
-- 4. STUDENTS - Thông tin sinh viên
-- ========================
CREATE TABLE students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    student_code VARCHAR(20) UNIQUE NOT NULL,
    class_code VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10),
    phone VARCHAR(15),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ========================
-- 5. TEACHERS - Thông tin giảng viên
-- ========================
CREATE TABLE teachers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    teacher_code VARCHAR(20) UNIQUE NOT NULL,
    department VARCHAR(100),
    date_of_birth DATE,
    gender VARCHAR(10),
    phone VARCHAR(15),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ========================
-- 6. CLASSES - Lớp học
-- ========================
CREATE TABLE classes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    class_code VARCHAR(20) UNIQUE NOT NULL,
    class_name VARCHAR(100),
    department VARCHAR(100)
);

-- ========================
-- 7. SUBJECTS - Môn học
-- ========================
CREATE TABLE subjects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    subject_name VARCHAR(100),
    credit INT
);

-- ========================
-- 8. SCORES - Điểm số của sinh viên
-- ========================
CREATE TABLE scores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT,
    subject_id BIGINT,
    semester VARCHAR(20),
    score FLOAT,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id)
);
