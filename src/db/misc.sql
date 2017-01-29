SELECT *
FROM city c, state s
WHERE c.state_id = s.id AND lower(c.name) LIKE '%' || lower('humildes') || '%';

SELECT *
FROM profile p, city c
WHERE p.city_id = c.id AND lower(p.name) LIKE '%' || lower('luiz') || '%';

UPDATE user_registration
SET user_id = 3114
WHERE user_id = 1603 AND registration_id = 1070;

SELECT *
FROM profile p
WHERE lower(p.name) LIKE '%arnaldo%';

SELECT *
FROM user_account ua
WHERE ua.email LIKE '%luizsemlimite%';

SELECT *
FROM user_account ua
WHERE id IN (1753);

SELECT *
FROM profile p
WHERE p.id IN (4790);

SELECT *
FROM health h
WHERE h.id IN (1309, 1316);

SELECT *
FROM user_registration tf,
  profile p,
  registration re,
  race ra
WHERE tf.user_id = p.id
      AND tf.registration_id = re.id
      AND re.race_id = ra.id
      AND tf.user_id IN (1444);

SELECT *
FROM user_registration tf, profile p
WHERE tf.user_id = p.id AND tf.registration_id = 1311;

----

SELECT *
FROM city c, state s
WHERE c.state_id = s.id AND lower(c.name) LIKE '%petrolina%';

SELECT *
FROM user_account ua
WHERE id IN (457);

SELECT *
FROM profile p
WHERE p.id IN (116, 284);

SELECT *
FROM health h
WHERE h.id IN (1309, 1316);

SELECT *
FROM user_registration tf,
  profile p,
  registration re,
  race ra
WHERE tf.user_id = p.id
      AND tf.registration_id = re.id
      AND re.race_id = ra.id
      AND tf.user_id IN (1309, 1316);

SELECT *
FROM period p
WHERE p.race_id = 72;

UPDATE period
SET ending = TO_DATE('06/09/2016', 'DD/MM/YYYY')
WHERE id IN (132, 133);

SELECT count(*)
FROM registration r
WHERE r.status = 'CONFIRMED' AND extract(YEAR FROM date) = 2016;

SELECT count(*)
FROM user_registration ur, registration r
WHERE ur.registration_id = r.id AND r.status = 'CONFIRMED' AND extract(YEAR FROM date) = 2016;

SELECT *
FROM user_registration ur, registration r
WHERE ur.registration_id = r.id AND r.id IN (1055) AND ur.user_id IN (2);

SELECT *
FROM registration r
WHERE r.id IN (1534);

SELECT picture
FROM profile
WHERE id = 71;

-- federados

SELECT *
FROM championship;


SELECT ENCODE(data, 'base64')
FROM pg_largeobject
WHERE LOID = 21607;


CREATE EXTENSION unaccent;

CREATE EXTENSION fuzzystrmatch;

CREATE EXTENSION tsearch2;

SELECT *
FROM pg_available_extensions;


SELECT unaccent('ão');

UPDATE registration
SET payment_transaction_code = NULL
WHERE id = 1558;

SELECT *
FROM registration
WHERE id = 1615;

SELECT
  p.mobile,
  c.name
FROM profile p, city c
WHERE p.city_id = c.id AND
      p.mobile !~ '^\(\d{2}\) \d{4,5}-\d{4}$';

SELECT mobile
FROM profile
WHERE mobile !~ '^\(\d{2}\) \d{4}-\d{4}$';

UPDATE profile
SET mobile = '(' || SUBSTRING(mobile, 1, 2) || ') ' || SUBSTRING(mobile, 3, 4) || '-' || SUBSTRING(mobile, 7, 4)
WHERE mobile ~ '^\d{10}$';

UPDATE
  profile
SET mobile = SUBSTRING(mobile, 1, 5) || '9' || SUBSTRING(mobile, 6, 10)
WHERE mobile ~ '^\(\d{2}\) \d{4}-\d{4}$' AND
      SUBSTRING(mobile, 2, 2) IN
      ('61', '62', '64', '63', '65', '66', '67', '68', '69', '21', '22', '24', '27', '28', '91', '93', '94', '92', '97', '95', '96', '98', '99', '12', '13', '14', '15', '16', '17', '18', '19', '31', '32', '33', '34', '35', '37', '38', '71', '73', '74', '75', '77', '79', '81', '87', '82', '83', '84', '85', '88', '89');
--'41', '42', '43', '44', '45', '46', '47', '48', '49', '51', '53', '54', '55'

UPDATE race
SET _status_id = 5
WHERE id = 39;

SELECT
  e.name,
  r.*
FROM event e, race r
WHERE r.event_id = e.id
ORDER BY e._beginning, r.name;

SELECT *
FROM race
WHERE id IN (31, 65);





