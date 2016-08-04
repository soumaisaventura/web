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
  'probike/2016_',
  'Pró Bike',
  NULL,
  650,
  'MANUAL',
  NULL,
  NULL,
  NULL,
  '2016-11-24',
  '2016-11-25',
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
  'light',
  'Light',
  39,
  7,
  '2016-11-24',
  '2016-11-25',
  31,
  1,
  'Em breve mais informações.');

INSERT INTO event_organizer (event_id,
                             organizer_id,
                             alternate_name,
                             alternate_email)
VALUES (39,
        3338,
        'Neuber Bonfim',
        NULL);

INSERT INTO race_category (race_id, category_id)
  SELECT
    r.id AS race_id,
    c.id AS category_id
  FROM race r, category c
  WHERE r.id IN (83)
        AND c.id IN (32, 33, 34, 35, 2, 4);


DELETE FROM race_category
WHERE race_id IN (78, 79, 80, 81) AND category_id IN (2, 12);

INSERT INTO race_modality (race_id, modality_id)
  SELECT
    r.id AS race_id,
    m.id AS modality_id
  FROM race r, modality m
  WHERE r.id IN (82, 83)
        AND m.id IN (12);

INSERT INTO championship_race (race_id, championship_id)
  SELECT
    r.id AS race_id,
    c.id AS championship_id
  FROM race r, championship c
  WHERE r.id IN (82)
        AND c.id IN (11, 12);

-- 54, 58

INSERT INTO period (race_id,
                    price,
                    beginning,
                    ending,
                    id)
VALUES (83,
        80,
        TO_DATE('01/09/2016', 'DD/MM/YYYY'),
        TO_DATE('18/09/2016', 'DD/MM/YYYY'),
        (SELECT max(id) + 1
         FROM period));


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
SET beginning = TO_DATE('26/06/2016', 'DD/MM/YYYY')
WHERE id IN (144, 145, 146, 147);

DELETE FROM race_category
WHERE race_id IN (72, 73) AND category_id IN (3, 4, 14, 15);