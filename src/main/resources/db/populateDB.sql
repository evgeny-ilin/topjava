DELETE
FROM meals;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (description, calories, datetime, user_id)
VALUES ('Завтрак', 500, to_timestamp('30.01.2020 10:00', 'DD.MM.YYYY HH24:MI'), 100000);
INSERT INTO meals (description, calories, datetime, user_id)
VALUES ('Обед', 1000, to_timestamp('30.01.2020 13:00', 'DD.MM.YYYY HH24:MI'), 100000);
INSERT INTO meals (description, calories, datetime, user_id)
VALUES ('Ужин', 500, to_timestamp('30.01.2020 20:00', 'DD.MM.YYYY HH24:MI'), 100000);
INSERT INTO meals (description, calories, datetime, user_id)
VALUES ('Еда в пограничное время', 100, to_timestamp('31.01.2020 00:00', 'DD.MM.YYYY HH24:MI'), 100001);
INSERT INTO meals (description, calories, datetime, user_id)
VALUES ('Завтрак', 1000, to_timestamp('31.01.2020 10:00', 'DD.MM.YYYY HH24:MI'), 100001);
INSERT INTO meals (description, calories, datetime, user_id)
VALUES ('Обед', 500, to_timestamp('31.01.2020 13:00', 'DD.MM.YYYY HH24:MI'), 100001);
INSERT INTO meals (description, calories, datetime, user_id)
VALUES ('Ужин', 410, to_timestamp('31.01.2020 20:00', 'DD.MM.YYYY HH24:MI'), 100001);