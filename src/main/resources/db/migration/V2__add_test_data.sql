-- 1. Rollid
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_VET'), ('ROLE_SHELTER'), ('ROLE_ADMIN');

-- 2. Liigid ja tõud
INSERT INTO species (name) VALUES ('Koer'), ('Kass'), ('Hobune');

INSERT INTO breeds (name, species_id) VALUES 
('Saksa lambakoer', 1), 
('Kuldne retriiver', 1), 
('Siiami kass', 2), 
('Munchkin', 2),
('Eesti hobune', 3);

-- 3. Varjupaigad
INSERT INTO shelters (name, address, phone_number, email) VALUES 
('Tartu Koduta Loomade Varjupaik', 'Roosi 91, Tartu', '+37253339272', 'tartu@varjupaik.ee'),
('Tallinna Loomade Hoiupaik', 'Viljandi mnt 24D, Tallinn', '+3726312749', 'tallinn@varjupaik.ee');

-- 4. Mikrokiibid (Ladu)
INSERT INTO microchips (microchip_number, importer, in_use) VALUES 
('643094100123456', 'PetID Estonia', true),
('643094100123457', 'PetID Estonia', true),
('643094100999001', 'VetSupply', false),
('643094100999002', 'VetSupply', false);

-- 5. Kasutajad (Password on kõigil 'password123' - testi jaoks võid kasutada BCrypti hiljem)
INSERT INTO users (personal_code, first_name, last_name, email, phone_number, password) VALUES 
('39001010011', 'Kalle', 'Kasutaja', 'kalle@gmail.com', '+3725511223', 'password123'), -- ID 1
('48505050022', 'Viivi', 'Veterinaar', 'viivi@loomaarst.ee', '+3725599887', 'password123'), -- ID 2
('38808080033', 'Toomas', 'Töötaja', 'toomas@varjupaik.ee', '+3725544332', 'password123'); -- ID 3

-- Seostame rollid
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- Kalle: ROLE_USER
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- Viivi: ROLE_VET
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3); -- Toomas: ROLE_SHELTER

-- Kasutajate profiilid
INSERT INTO owner_profiles (user_id, address) VALUES (1, 'Tähe 4, Tartu');
INSERT INTO veterinary_profiles (user_id, license_number, specialization) VALUES (2, 'V-12345', 'Väikeloomade kirurgia');
INSERT INTO shelter_worker_profiles (user_id, shelter_id, job_title) VALUES (3, 1, 'Vanemhooldaja');

-- 6. Lemmikloomad
INSERT INTO pets (microchip_id, breed_id, owner_id, name, sex, birth_date, color, status) VALUES 
(1, 1, 1, 'Rex', 'M', '2021-06-15', 'Must/Pruun', 'ACTIVE'),
(2, 3, 1, 'Miisu', 'E', '2023-02-10', 'Hall kirju', 'ACTIVE');

-- 7. Toimingud ja ajalugu
-- Veterinaari kontroll
INSERT INTO veterinary_checks (pet_id, user_id, health_assessment, weight) VALUES 
(1, 2, 'Loom on terve ja heas toitumises.', 32.5);

-- Ravi (seotud eelmise kontrolliga)
INSERT INTO treatments (pet_id, veterinary_check_id, description, medication, date) VALUES 
(1, 1, 'Iga-aastane ussitõrje', 'Drontal Plus', '2026-03-17');

-- Varjupaika toomise ajalugu (näitena, kui Miisu oli kunagi leitud)
INSERT INTO shelter_admissions (shelter_id, pet_id, finding_location, finding_date, shelter_checkin_time, shelter_health_assessment) VALUES 
(1, 2, 'Annelinna Prisma parkla', '2023-04-01', '2023-04-01 14:30:00', 'Nälginud, kuid muidu vigastusteta.');

-- Elutsükli sündmused
INSERT INTO pet_lifecycle_events (pet_id, user_id, user_role_id, event_type, description) VALUES 
(1, 2, 2, 'REGISTRATION', 'Loom registreeritud süsteemi veterinaari poolt.'),
(2, 3, 3, 'FOUND', 'Loom leitud ja toodud varjupaika.');

-- 8. Pildid (Lisame tühja picture andmevälja, et struktuur klapiks)
INSERT INTO pictures (pet_id, file_name, picture) VALUES 
(1, 'rex_profiil.jpg', NULL),
(2, 'miisu_leidmine.png', NULL);
