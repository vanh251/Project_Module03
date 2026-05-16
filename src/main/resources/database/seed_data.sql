-- Xóa dữ liệu cũ (nếu có) để tránh lỗi trùng lặp khi chạy lại
-- Chú ý thứ tự xóa: bảng con xóa trước, bảng cha xóa sau
TRUNCATE TABLE reviews, notifications, lesson_progress, enrollments, lessons, courses, users RESTART IDENTITY CASCADE;

-- 1. Thêm dữ liệu bảng users
-- Mật khẩu mặc định cho tất cả user là 'password' (đã được băm bằng BCrypt)
INSERT INTO users (username, password_hash, email, full_name, role, is_active, created_at, updated_at) VALUES
                                                                                                           ('admin01', '$2a$10$wYOMPq.0E86h8J5/oX.oJ.LzB01nK6Z30n9.53kX3k2/qL3/j5/yO', 'admin@example.com', 'Quản Trị Viên', 'ADMIN', true, NOW(), NOW()),
                                                                                                           ('teacher01', '$2a$10$wYOMPq.0E86h8J5/oX.oJ.LzB01nK6Z30n9.53kX3k2/qL3/j5/yO', 'teacher1@example.com', 'Giảng Viên Một', 'TEACHER', true, NOW(), NOW()),
                                                                                                           ('teacher02', '$2a$10$wYOMPq.0E86h8J5/oX.oJ.LzB01nK6Z30n9.53kX3k2/qL3/j5/yO', 'teacher2@example.com', 'Giảng Viên Hai', 'TEACHER', true, NOW(), NOW()),
                                                                                                           ('student01', '$2a$10$wYOMPq.0E86h8J5/oX.oJ.LzB01nK6Z30n9.53kX3k2/qL3/j5/yO', 'student1@example.com', 'Học Viên Một', 'STUDENT', true, NOW(), NOW()),
                                                                                                           ('student02', '$2a$10$wYOMPq.0E86h8J5/oX.oJ.LzB01nK6Z30n9.53kX3k2/qL3/j5/yO', 'student2@example.com', 'Học Viên Hai', 'STUDENT', true, NOW(), NOW());

-- 2. Thêm dữ liệu bảng courses (teacher_id tham chiếu tới users)
INSERT INTO courses (title, description, teacher_id, price, duration_hours, status, created_at, updated_at) VALUES
                                                                                                                ('Khóa học Spring Boot Cơ Bản', 'Làm quen với Spring Boot 3 và RESTful API.', 2, 299000.00, 20, 'PUBLISHED', NOW(), NOW()),
                                                                                                                ('Khóa học Hibernate Nâng Cao', 'Đi sâu vào JPA và tối ưu hóa truy vấn.', 2, 450000.00, 35, 'DRAFT', NOW(), NOW()),
                                                                                                                ('Frontend với ReactJS', 'Xây dựng giao diện tương tác với React và Tailwind.', 3, 300000.00, 25, 'PUBLISHED', NOW(), NOW());

-- 3. Thêm dữ liệu bảng lessons (course_id tham chiếu tới courses)
INSERT INTO lessons (course_id, title, content_url, text_content, order_index, is_published, created_at, updated_at) VALUES
                                                                                                                         (1, 'Bài 1: Giới thiệu Spring Boot', 'https://video.example.com/spring-1', 'Nội dung bài 1: Giới thiệu kiến trúc...', 1, true, NOW(), NOW()),
                                                                                                                         (1, 'Bài 2: Dependency Injection', 'https://video.example.com/spring-2', 'Nội dung bài 2: DI và IoC Container...', 2, true, NOW(), NOW()),
                                                                                                                         (1, 'Bài 3: Tạo REST API', NULL, 'Nội dung bài 3: Sử dụng @RestController...', 3, true, NOW(), NOW()),
                                                                                                                         (2, 'Bài 1: Entity Lifecycle', 'https://video.example.com/hibernate-1', 'Vòng đời của một Entity trong Hibernate...', 1, false, NOW(), NOW()),
                                                                                                                         (3, 'Bài 1: React Component', 'https://video.example.com/react-1', 'Khái niệm về Component và JSX...', 1, true, NOW(), NOW());

-- 4. Thêm dữ liệu bảng enrollments (student_id và course_id)
INSERT INTO enrollments (student_id, course_id, enrollment_date, status, progress_percentage) VALUES
                                                                                                  (4, 1, NOW() - INTERVAL '10 days', 'ENROLLED', 66.67), -- student01 đăng ký khóa Spring Boot
                                                                                                  (5, 1, NOW() - INTERVAL '20 days', 'COMPLETED', 100.00), -- student02 đăng ký và đã hoàn thành khóa Spring Boot
                                                                                                  (4, 3, NOW() - INTERVAL '2 days', 'ENROLLED', 0.00); -- student01 đăng ký khóa ReactJS

-- Cập nhật completion_date cho enrollment đã hoàn thành
UPDATE enrollments SET completion_date = NOW() - INTERVAL '2 days' WHERE enrollment_id = 2;

-- 5. Thêm dữ liệu bảng lesson_progress (enrollment_id và lesson_id)
INSERT INTO lesson_progress (enrollment_id, lesson_id, is_completed, completed_at, last_accessed_at) VALUES
-- student01 (enrollment 1) học khóa 1 (gồm lesson 1, 2, 3)
(1, 1, true, NOW() - INTERVAL '9 days', NOW() - INTERVAL '9 days'),
(1, 2, true, NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days'),
(1, 3, false, NULL, NOW() - INTERVAL '2 days'),
-- student02 (enrollment 2) học khóa 1
(2, 1, true, NOW() - INTERVAL '18 days', NOW() - INTERVAL '18 days'),
(2, 2, true, NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days'),
(2, 3, true, NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days'),
-- student01 (enrollment 3) học khóa 3 (gồm lesson 5)
(3, 5, false, NULL, NOW() - INTERVAL '1 day');

-- 6. Thêm dữ liệu bảng notifications (user_id)
INSERT INTO notifications (user_id, title, message, is_read, created_at) VALUES
                                                                             (4, 'Chào mừng!', 'Chào mừng bạn đã gia nhập hệ thống học tập.', true, NOW() - INTERVAL '10 days'),
                                                                             (4, 'Đăng ký thành công', 'Bạn đã đăng ký thành công khóa học Spring Boot Cơ Bản.', true, NOW() - INTERVAL '10 days'),
                                                                             (2, 'Khóa học được phê duyệt', 'Khóa học Spring Boot Cơ Bản của bạn đã được xuất bản.', false, NOW() - INTERVAL '5 days');

-- 7. Thêm dữ liệu bảng reviews (student_id, course_id)
INSERT INTO reviews (student_id, course_id, rating, comment, created_at, updated_at) VALUES
                                                                                         (5, 1, 5, 'Khóa học rất hay và thực tế! Giảng viên nhiệt tình.', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'),
                                                                                         (4, 1, 4, 'Nội dung tốt, nhưng bài 3 hơi khó hiểu một chút.', NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days');
