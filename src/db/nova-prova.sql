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


SELECT *
FROM event;


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
  'np5',
  '5ª Noite do Perrengue',
  NULL,
  5571,
  'MANUAL',
  NULL,
  NULL,
  NULL,
  '2017-03-11',
  '2017-03-12',
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
  '80km',
  '80km',
  42,
  1,
  '2017-03-11',
  '2017-03-12',
  80,
  1,
  'Em breve mais informações.');

INSERT INTO event_organizer (event_id,
                             organizer_id,
                             alternate_name,
                             alternate_email)
VALUES (42,
        8,
        'Gustavo Chagas',
        NULL);

INSERT INTO race_category (race_id, category_id)
  SELECT
    r.id AS race_id,
    c.id AS category_id
  FROM race r, category c
  WHERE r.id IN (89, 90)
        AND c.id IN (2, 4, 3);


DELETE FROM race_category
WHERE race_id IN (78, 79, 80, 81) AND category_id IN (2, 12);

INSERT INTO race_modality (race_id, modality_id)
  SELECT
    r.id AS race_id,
    m.id AS modality_id
  FROM race r, modality m
  WHERE r.id IN (90)
        AND m.id IN (13);

INSERT INTO championship_race (race_id, championship_id)
  SELECT
    r.id AS race_id,
    c.id AS championship_id
  FROM race r, championship c
  WHERE r.id IN (90)
        AND c.id IN (13, 14);

-- 54, 58

INSERT INTO period (race_id,
                    price,
                    beginning,
                    ending,
                    id)
VALUES (90,
        160,
        TO_DATE('11/02/2017', 'DD/MM/YYYY'),
        TO_DATE('10/03/2017', 'DD/MM/YYYY'),
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