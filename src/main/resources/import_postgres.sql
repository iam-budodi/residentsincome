-- INDIVIDUAL_INCOME
INSERT INTO INDIVIDUAL_INCOME (id, category, class_amount, class_limit, tax_per_class, tax_on_excess_income, description) VALUES (1, 0, 0, 270000, 0, 0, 'Total monthly income does not exceed Tshs 270000');
INSERT INTO INDIVIDUAL_INCOME (id, category, class_amount, class_limit, tax_per_class, tax_on_excess_income, description) VALUES (2, 1, 270000, 520000, 0, 8, 'Total monthly income exceeds 270000 but does not exceed Tshs 520,000 and tax rates of 8% of the amount in excess of Tshs 270000');
INSERT INTO INDIVIDUAL_INCOME (id, category, class_amount, class_limit, tax_per_class, tax_on_excess_income, description) VALUES (3, 2, 520000, 760000, 20000, 20, 'Total monthly income exceeds 520000 but does not exceed Tshs 760000 and tax amount of 20000 plus tax rates of 20 percent of the amount in excess of Tshs 520000');
INSERT INTO INDIVIDUAL_INCOME (id, category, class_amount, class_limit, tax_per_class, tax_on_excess_income, description) VALUES (4, 3, 760000, 1000000, 68000, 25, 'Total monthly income exceeds 760000 but does not exceed Tshs 1000000 and tax amount of 68000 plus tax rates of 25% of the amount in excess of Tshs 760000');
INSERT INTO INDIVIDUAL_INCOME (id, category, class_amount, class_limit, tax_per_class, tax_on_excess_income, description) VALUES (5, 4, 1000000, 9999999999, 128000, 30, 'Total monthly income exceeds 520000 but does not exceed Tshs 760000 and tax amount of 20000 plus tax rates of 20 percent of the amount in excess of Tshs 520000');


-- USER
INSERT INTO RESIDENT (id, user_name, password, first_name, last_name, phone_number, email, date_of_birth, user_role) VALUES (5, 'japhets', 'B12js6dy$', 'Japhet', 'Sebastian', '255744608510', 'japhets@gmail.com', '1935-07-01', 0)
INSERT INTO RESIDENT (id, user_name, password, first_name, last_name, phone_number, email, date_of_birth, user_role) VALUES (6, 'budodi', 'B12js6dy$', 'Jeff', 'Sebastian', '255744608511', 'japhet@gmail.com', '1935-07-01', 1)
INSERT INTO RESIDENT (id, user_name, password, first_name, last_name, phone_number, email, date_of_birth, user_role) VALUES (7, 'japhets', 'B12js6dy$', 'Budodi', 'Sebastian', '255744608511', 'japhetb@gmail.com', '1935-07-01', 1)