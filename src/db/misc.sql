SELECT *
  FROM user_account ua
 WHERE ua.email = 'monelopes17@gmail.com';

SELECT *
  FROM profile p
 WHERE lower (p.name) LIKE '%neto%';

SELECT *
  FROM profile p
 WHERE p.id = 639;

SELECT *
  FROM health h
 WHERE h.id = 639;

SELECT *
  FROM team_formation tf
 WHERE tf.user_id IN (639);