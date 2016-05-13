SELECT *
FROM city c, state s
WHERE c.state_id = s.id AND lower(c.name) LIKE '%osa%';

SELECT *
FROM profile p, city c
WHERE p.city_id = c.id AND lower(p.name) LIKE '%alan%';

SELECT *
FROM profile p
WHERE lower(p.name) LIKE '%arnaldo%';

SELECT *
FROM user_account ua
WHERE ua.email LIKE '%santiagojulliana@gmail.com%';

SELECT *
FROM user_account ua
WHERE id IN (5);

SELECT *
FROM profile p
WHERE p.id IN (998);

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
WHERE tf.user_id = p.id AND tf.registration_id = 1301;

UPDATE user_registration
SET user_id = 947
WHERE registration_id = 1301 AND user_id = 1446;

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


select * from period p where p.ending = '2016-05-06';

update period set ending = '2016-05-07' where ending = '2016-05-06';