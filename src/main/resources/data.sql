DELETE FROM watch_order;
DELETE FROM recipient_details;
DELETE FROM cart;
DELETE FROM watch;
DELETE FROM user_details;
DELETE FROM brand;

INSERT INTO brand (image_name, is_active, name) VALUES
('rolex.jpg', true, 'Rolex'),
('patek.jpg', true, 'Patek Philippe'),
('audemars.jpg', true, 'Audemars Piguet'),
('breitling.jpg', true, 'Breitling');

INSERT INTO user_details ( city, country, email, is_enable, name, password, phone, postcode, role, security_token, street, surname)
VALUES
('Warszawa', 'Polska', 'nowak@wp.pl', TRUE, 'Adam', '$2a$10$lNJeU19qmA2AIp6MikxcJuMBg/voquvLtUHBoAceRzkLmsiEuD8Gi', '678987345', '03-192', 'ROLE_USER', '5b780a64-7c08-4f22-a799-21ccec5d8b2b', 'Akacjowa', 'Nowak'),
('Warszawa', 'Polska', 'kowalski@wp.pl', TRUE, 'Robert', '$2a$10$If1pv7igxmdS4/sXYa6bJ.HCbWNdVx1p3lEF0YH8pjxPBAJqlZila', '654325225', '02-342', 'ROLE_ADMIN', NULL, 'Akacjowa', 'Kowalski');


INSERT INTO watch (brand, condition, description, image, is_active, model, price, stock)
VALUES
('Audemars Piguet', 'Nowy', 'Dobry zegarek', 'AUDEMARS_PIGUET_DIAMOND_LADY_18K_GOLD.jpg', TRUE, 'DIAMOND LADY 18K GOLD', 50000.00, 2),
('Audemars Piguet', 'Używany', 'Dobry zegarek', 'AUDEMARS_PIGUET_MILLENARY_DUAL_TIME_POWER_RESERVE_ROSE_GOLD.jpg', TRUE, 'MILLENARY DUAL TIME POWER RESERVE ROSE GOLD', 30000.00, 2),
('Audemars Piguet', 'Nowy', 'Bardzo dobry zegarek', 'AUDEMARS_PIGUET_ROYAL_OAK_DUAL_TIME_GOLD_18K.jpg', TRUE, 'ROYAL OAK DUAL TIME GOLD 18K', 45000.00, 0),
('Audemars Piguet', 'Nowy', 'Bardzo dobry zegarek', 'AUDEMARS_PIGUET_ROYAL_OAK_SELFWINDING.jpg', TRUE, 'ROYAL OAK SELFWINDING', 150000.00, 1),
('Audemars Piguet', 'Nowy', 'Dobry zegarek', 'AUDEMARS_PIGUET_ROYAL_OAK_SELFWINDING_STEEL_BLACK.jpg', TRUE, 'ROYAL OAK SELFWINDING STEEL BLACK', 230000.00, 4),
('Breitling', 'Nowy', 'Dobry zegarek', 'BREITLING_CHRONOMAT_EVOLUTION_CHRONOGRAPH.jpg', TRUE, 'CHRONOMAT EVOLUTION CHRONOGRAPH', 240000.00, 2),
('Breitling', 'Używany', 'Bardzo dobry zegarek', 'BREITLING_MONTBRILLANT_NAVITIMER_SPECIAL_EDITION_RED_GOLD.jpg', TRUE, 'MONTBRILLANT NAVITIMER SPECIAL EDITION RED GOLD', 120000.00, 1),
('Breitling', 'Nowy', 'Dobry zegarek', 'BREITLING_MONTBRILLANT_NAVITIMER_SPECIAL_EDITION_RED_GOLD.jpg', TRUE, 'TRANSOCEAN CHRONOGRAPH 18K GOLD', 230000.00, 1),
('Rolex', 'Nowy', 'Dobry zegarek', 'ROLEX_AIR_KING_STEEL_BLACK_DIAL.jpg', TRUE, 'AIR KING STEEL BLACK DIAL', 100000.00, 2),
('Rolex', 'Używany', 'Dobry zegarek', 'ROLEX_DATEJUST_36_JUBILEE_STEEL_ROSE_GOLD_18K.jpg', TRUE, 'DATEJUST 36 JUBILEE STEEL ROSE GOLD 18K', 123000.00, 3),
('Rolex', 'Nowy', 'Dobry zegarek', 'ROLEX_DATEJUST_PEARLMASTER_29_DIAMONDS_YELLOW_GOLD.jpg', TRUE, 'DATEJUST PEARLMASTER 29 DIAMONDS YELLOW GOLD', 231222.00, 3),
('Rolex', 'Nowy', 'Dobry zegarek', 'ROLEX_SKY_DWELLER_JUBILEE_STEEL_YELLOW_GOLD_18K.jpg', TRUE, 'SKY DWELLER JUBILEE STEEL YELLOW GOLD 18K', 342000.00, 1);
