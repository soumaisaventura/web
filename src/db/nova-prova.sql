SELECT *
FROM event
ORDER BY id DESC;

SELECT *
FROM race
ORDER BY id DESC;

SELECT *
FROM sport;

SELECT max(id) + 1
FROM race;

INSERT INTO event (id,
                   alias,
                   name,
                   site,
                   city_id,
                   payment_type,
                   payment_info,
                   payment_account,
                   payment_token,
                   _beginning,
                   _ending,
                   _status_id,
                   description)
VALUES (
  (SELECT max(id) + 1
   FROM event),
  'eapachallenge/serras/2016',
  'EAPA Challenge Serras',
  NULL,
  5377,
  'MANUAL',
  NULL,
  NULL,
  NULL,
  '2016-12-11',
  '2016-12-11',
  1,
  'Em breve, mais informações sobre o evento.');

INSERT INTO race (id,
                  alias,
                  name,
                  event_id,
                  sport_id,
                  beginning,
                  ending,
                  distance,
                  _status_id,
                  description)
VALUES (
  (SELECT max(id) + 1
   FROM race),
  'trbasic',
  'Trail Run Basic',
  34,
  2,
  '2016-09-25',
  '2016-09-25',
  5,
  1,
  'Em breve mais informações.');

INSERT INTO event_organizer (event_id,
                             organizer_id,
                             alternate_name,
                             alternate_email)
VALUES (35,
        998,
        'Cadu Silva',
        NULL);

INSERT INTO race_category (race_id, category_id)
  SELECT
    r.id AS race_id,
    c.id AS category_id
  FROM race r, category c
  WHERE r.id IN (71)
        AND c.id IN (17, 18);

INSERT INTO race_modality (race_id, modality_id)
  SELECT
    r.id AS race_id,
    m.id AS modality_id
  FROM race r, modality m
  WHERE r.id IN (38)
        AND m.id IN (1, 2, 6);

-- 54, 58

INSERT INTO period (race_id,
                    price,
                    beginning,
                    ending,
                    id)
VALUES (71,
        60.00,
        --         TO_DATE('06/06/2016', 'DD/MM/YYYY'),
        --         TO_DATE('31/07/2016', 'DD/MM/YYYY'),
        TO_DATE('01/08/2016', 'DD/MM/YYYY'),
        TO_DATE('18/09/2016', 'DD/MM/YYYY'),
        (SELECT max(id) + 1
         FROM period));

R$50 por pessoa – 06/06/2016 a 31/07/2016
R$60 por pessoa – 01/08/2016 a 18/09/2016


INSERT INTO kit (id,
                 race_id,
                 alias,
                 name,
                 description,
                 price)
VALUES ((SELECT max(id) + 1
         FROM kit),
        38,
        'completo',
        'Kit Completo',
        'escrever...',
        50);


UPDATE user_registration x
SET kit_id = 5
WHERE exists(
    SELECT *
    FROM user_registration ur, registration r
    WHERE ur.registration_id = r.id
          AND kit_id IS NULL
          AND r.race_id = 63
          AND ur.user_id = x.user_id
          AND ur.registration_id = x.registration_id);

UPDATE
  period
SET beginning = '2016-05-11'
WHERE beginning = '2016-06-06';

