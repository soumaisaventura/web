SELECT *
FROM city c, state s
WHERE c.state_id = s.id AND lower(c.name) LIKE '%itabaiana%';

SELECT *
FROM profile p, city c
WHERE p.city_id = c.id AND lower(p.name) LIKE lower('%Andrade%');

#1783, David de Sousa Viana foi substituído por Kenji Tago

UPDATE user_registration
SET user_id = 185
WHERE registration_id = 1445 AND user_id = 71;


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
SET beginning = '2016-06-01'
WHERE beginning = '2016-05-18';

SELECT count(*)
FROM registration r
WHERE r.status = 'CONFIRMED' AND extract(YEAR FROM date) = 2016;

SELECT count(*)
FROM user_registration ur, registration r
WHERE ur.registration_id = r.id AND r.status = 'CONFIRMED' AND extract(YEAR FROM date) = 2016;


SELECT *
FROM user_registration ur
WHERE ur.registration_id = 1358;
