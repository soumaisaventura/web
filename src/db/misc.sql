SELECT *
  FROM user_account ua
 WHERE ua.email = 'monelopes17@gmail.com';

SELECT *
  FROM user_account ua
 WHERE id = 789;

SELECT *
  FROM profile p
 WHERE lower (p.name) LIKE '%claudionor%';

SELECT *
  FROM profile p
 WHERE p.id = 789;

SELECT *
  FROM health h
 WHERE h.id = 581;

SELECT *
  FROM team_formation tf, profile p
 WHERE tf.user_id = p.id AND tf.registration_id = 166;

UPDATE team_formation
   SET user_id = 789
 WHERE registration_id = 166 AND user_id = 38;


DELETE FROM profile p
      WHERE p.id = 581;

DELETE FROM health h
      WHERE h.id = 581;

DELETE FROM user_account ua
      WHERE id = 581;