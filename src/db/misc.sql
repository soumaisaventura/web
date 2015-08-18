SELECT *
  FROM profile p, city c
 WHERE p.city_id = c.id AND lower (p.name) LIKE '%vitor%';

SELECT *
  FROM city c, state s
 WHERE c.state_id = s.id AND lower (c.name) LIKE '%socorro%';

SELECT *
  FROM user_account ua
 WHERE ua.email LIKE '%ss3%';

SELECT *
  FROM user_account ua
 WHERE id  IN (50);

SELECT *
  FROM profile p
 WHERE p.id IN (116, 284);

SELECT *
  FROM health h
 WHERE h.id = 581;

SELECT *
  FROM team_formation tf, profile p
 WHERE tf.user_id = p.id AND tf.registration_id = 302;

SELECT *
  FROM team_formation tf,
       profile p,
       registration re,
       race ra
 WHERE     tf.user_id = p.id
       AND tf.registration_id = re.id
       AND re.race_id = ra.id
       AND tf.user_id = 359;

UPDATE team_formation
   SET user_id = 959
 WHERE registration_id = 309 AND user_id = 745;

SELECT *
  FROM team_formation tf, registration r, race a
 WHERE     tf.registration_id = r.id
       AND r.race_id = a.id
       AND user_id IN (116, 284);

UPDATE registration
   --SET category_id = 2
   SET course_id = 9
 WHERE id = 320;

DELETE FROM profile p
      WHERE p.id = 581;

DELETE FROM health h
      WHERE h.id = 581;

DELETE FROM user_account ua
      WHERE id = 581;