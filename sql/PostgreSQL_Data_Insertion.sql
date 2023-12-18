INSERT INTO authority (name, description) VALUES
('ROLE_ADMIN', 'Administrator'),
('ROLE_USER', 'Użytkownik'),
('USERS_ADM', 'Zarządzanie użytkownikami'),
('WEEKLY_REP', 'Tygodniowe raporty');

INSERT INTO users (login, email, password_hash, first_name, last_name, active) VALUES
('admin', 'admin@localhost', '$2a$10$r9Qn2rkN.oMpnoPiWUfyVOke4duL5IpbbAPhgidvlpUhPObeznVy.', 'Administrator', 'Administrator', true),
('user', 'user@localhost', '$2a$10$OYAhYMvnFTXD27aCdQnRreJNt5sUXPVoPnZaZS8Q9ARN7yJEEcJcW', 'User', 'User', true);

INSERT INTO user_authority (user_id, authority_name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

INSERT INTO subproject_type (name) VALUES
('L4'),
('Produkcja'),
('Rozwój'),
('Urlop'),
('Prace wewnętrzne'),
('Wsparcie'),
('Wyjście'),
('Nadgodziny');

INSERT INTO project (code, name, date_from, date_to, hours_predicted) VALUES
('1234', 'Aplikacja X', '2023-10-01', '2024-12-31', 2000),
('0000', 'Wewnętrzne', NULL, NULL, NULL);

INSERT INTO subproject (name, date_from, date_to, subproject_type_id, project_id, hours_predicted, code) VALUES
('Rozwój', '2023-10-01', '2024-12-31', 3, 1, 1000, '1234_03'),
('Wsparcie', '2023-10-01', '2024-12-31', 6, 1, 1000, '1234_06'),
('L4', NULL, NULL, 1, 2, NULL, '0000_01'),
('Urlopy', NULL, NULL, 4, 2, NULL, '0000_04'),
('Prace wewnętrzne', NULL, NULL, 5, 2, NULL, '0000_05'),
('Wyjścia', NULL, NULL, 7, 2, NULL, '0000_07'),
('Nadgodziny', NULL, NULL, 8, 2, NULL, '0000_08');

INSERT INTO task (name, from_date, to_date, subproject_id, hours_predicted) VALUES
('Back-end', '2023-10-01', '2024-12-31', 1, 300),
('Front-end', '2023-10-01', '2024-12-31', 1, 350),
('Projekt graficzny', '2023-10-01', '2024-12-31', 1, 350),
('Naprawa błędów', '2023-10-01', '2024-12-31', 2, 330),
('Nowe funkcjonalności', '2023-10-01', '2024-12-31', 2, 335),
('Spotkania z klientem', '2023-10-01', '2024-12-31', 2, 335),
('L4', NULL, NULL, 3, NULL),
('Urlopy', NULL, NULL, 4, NULL),
('Prace wewnętrzne', NULL, NULL, 5, NULL),
('Wyjścia', NULL, NULL, 6, NULL),
('Nadgodziny', NULL, NULL, 7, NULL);

INSERT INTO task_report (status, description, date, hours, task_id, user_id) VALUES
(false, '', '2023-11-13', 8.0, 1, 1),
(false, '', '2023-11-14', 8.0, 1, 1),
(false, '', '2023-11-15', 8.0, 1, 1),
(false, '', '2023-11-16', 4.0, 1, 1),
(false, '', '2023-11-17', 4.0, 1, 1),
(false, '', '2023-11-20', 16.0, 1, 1),
(false, '', '2023-11-21', 12.0, 1, 1),
(false, '', '2023-11-22', 6.0, 1, 1),
(false, '', '2023-11-23', 6.0, 1, 1),
(false, '', '2023-11-30', 8.0, 1, 1),
(false, '', '2023-11-27', 8.0, 1, 1),
(false, '', '2023-11-28', 8.0, 1, 1),
(false, '', '2023-11-29', 8.0, 1, 1),
(false, '', '2023-11-06', 8.0, 1, 1),
(false, '', '2023-11-07', 8.0, 1, 1),
(false, '', '2023-11-08', 8.0, 1, 1),
(false, '', '2023-11-10', 8.0, 1, 1),
(false, '', '2023-12-01', 8.0, 1, 1),
(false, '', '2023-12-04', 8.0, 1, 1),
(false, '', '2023-12-05', 8.0, 1, 1),
(false, '', '2023-12-06', 8.0, 1, 1),
(false, '', '2023-12-07', 8.0, 1, 1),
(false, '', '2023-12-08', 8.0, 1, 1),
(false, '', '2023-12-11', 8.0, 1, 1),
(false, '', '2023-12-12', 8.0, 1, 1),
(false, '', '2023-12-13', 8.0, 1, 1),
(false, '', '2023-12-14', 8.0, 1, 1),
(false, '', '2023-12-15', 8.0, 1, 1),
(false, '', '2023-12-18', 8.0, 1, 1),
(false, '', '2023-12-19', 8.0, 1, 1),
(false, '', '2023-12-22', 8.0, 1, 1),
(false, '', '2023-12-25', 8.0, 1, 1),
(false, '', '2023-12-27', 4.0, 1, 1),
(false, '', '2023-12-28', 4.0, 1, 1);

INSERT INTO default_task (user_id, task_id, date) VALUES
(1, 1, '2023-11-06');
