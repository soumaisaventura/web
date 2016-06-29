SELECT *
FROM city c, state s
WHERE c.state_id = s.id AND lower(c.name) LIKE '%' || lower('sobra') || '%';

SELECT *
FROM profile p, city c
WHERE p.city_id = c.id AND lower(p.name) LIKE '%' || lower('augusta') || '%';

UPDATE user_registration
SET user_id = 3114
WHERE user_id = 1603 AND registration_id = 1070;

SELECT *
FROM profile p
WHERE lower(p.name) LIKE '%arnaldo%';

SELECT *
FROM user_account ua
WHERE ua.email LIKE '%cfautomoveis@hotmail.com%';

SELECT *
FROM user_account ua
WHERE id IN (5);

SELECT *
FROM profile p
WHERE p.id IN (224);

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
WHERE id in (132, 133) ;

SELECT count(*)
FROM registration r
WHERE r.status = 'CONFIRMED' AND extract(YEAR FROM date) = 2016;

SELECT count(*)
FROM user_registration ur, registration r
WHERE ur.registration_id = r.id AND r.status = 'CONFIRMED' AND extract(YEAR FROM date) = 2016;


SELECT *
FROM user_registration ur
WHERE ur.registration_id = 1358;
