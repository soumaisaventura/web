SELECT *
  FROM user_account ua
 WHERE ua.email = 'monelopes17@gmail.com';

SELECT *
  FROM user_account ua
 WHERE id = 789;

SELECT *
  FROM profile p, city c
 WHERE p.city_id = c.id AND lower (p.name) LIKE '%hugo%';

SELECT *
  FROM profile p
 WHERE p.id = 789;

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
   SET user_id = 359
 WHERE registration_id = 260 AND user_id = 315;

UPDATE registration
   --   SET category_id = 359
   SET course_id = 9
 WHERE id = 274;

DELETE FROM profile p
      WHERE p.id = 581;

DELETE FROM health h
      WHERE h.id = 581;

DELETE FROM user_account ua
      WHERE id = 581;