-- Course Evaluation Survey System - Database Schema
-- Run this script to initialize the database

CREATE DATABASE IF NOT EXISTS course_eval_db;
USE course_eval_db;

-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    full_name VARCHAR(200) NOT NULL,
    role_id INT NOT NULL,
    status ENUM('PENDING','ACTIVE','REJECTED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Teacher-Course assignments
CREATE TABLE IF NOT EXISTS teacher_courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    teacher_id INT NOT NULL,
    course_id INT NOT NULL,
    UNIQUE KEY uq_teacher_course (teacher_id, course_id),
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- Surveys table
CREATE TABLE IF NOT EXISTS surveys (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    course_id INT NOT NULL,
    initiator_id INT NOT NULL,
    access_type ENUM('AUTHENTICATED','GUEST') DEFAULT 'AUTHENTICATED',
    status ENUM('DRAFT','PUBLISHED','CLOSED') DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (initiator_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Survey questions
CREATE TABLE IF NOT EXISTS survey_questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    survey_id INT NOT NULL,
    question_text TEXT NOT NULL,
    question_type ENUM('SINGLE','MULTIPLE','TEXT') DEFAULT 'SINGLE',
    display_order INT DEFAULT 0,
    FOREIGN KEY (survey_id) REFERENCES surveys(id) ON DELETE CASCADE
);

-- Survey options
CREATE TABLE IF NOT EXISTS survey_options (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    option_text VARCHAR(500) NOT NULL,
    display_order INT DEFAULT 0,
    FOREIGN KEY (question_id) REFERENCES survey_questions(id) ON DELETE CASCADE
);

-- Respondents table (tracks who responded)
CREATE TABLE IF NOT EXISTS respondents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    survey_id INT NOT NULL,
    user_id INT DEFAULT NULL,
    guest_email VARCHAR(150) DEFAULT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Survey responses (individual answers)
CREATE TABLE IF NOT EXISTS survey_responses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    respondent_id INT NOT NULL,
    question_id INT NOT NULL,
    option_id INT DEFAULT NULL,
    text_answer TEXT DEFAULT NULL,
    FOREIGN KEY (respondent_id) REFERENCES respondents(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES survey_questions(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES survey_options(id) ON DELETE SET NULL
);

-- Seed roles
INSERT IGNORE INTO roles (name) VALUES ('ADMIN'), ('INITIATOR'), ('TEACHER'), ('RESPONDENT');

-- Seed admin user (password: admin123)
INSERT IGNORE INTO users (username, password, email, full_name, role_id, status)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'admin@courseeval.com', 'System Administrator',
        (SELECT id FROM roles WHERE name='ADMIN'), 'ACTIVE');

-- Seed a survey initiator (password: init123)
INSERT IGNORE INTO users (username, password, email, full_name, role_id, status)
VALUES ('initiator1', '$2a$10$8K1p/a0dR7ox/dGIbEAi7OhS3JVCv9yREK5.K8hU8JrkXKFKIThDe',
        'initiator@courseeval.com', 'Survey Initiator One',
        (SELECT id FROM roles WHERE name='INITIATOR'), 'ACTIVE');

-- Seed sample courses
INSERT IGNORE INTO courses (code, title, description)
VALUES 
('CS101', 'Introduction to Programming', 'Fundamentals of programming using Python'),
('CS201', 'Data Structures', 'Arrays, Lists, Trees, Graphs and algorithms'),
('MATH101', 'Calculus I', 'Limits, derivatives and integrals');
