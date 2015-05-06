SELECT * FROM race;

SELECT *
  FROM profile pr
 WHERE lower (pr."name") LIKE '%diana%';

--426

--8

SELECT *
  FROM category ca;

SELECT *
  FROM race_organizer r
 WHERE r.race_id = 6;

INSERT INTO race_organizer (race_id, organizer_id)
     VALUES (6, 426);

SELECT *
  FROM profile pr
 WHERE lower (pr."name") LIKE '%marcel%';

SELECT *
  FROM health h
 WHERE h.id = 401;

-- 401

SELECT *
  FROM profile pr
 WHERE lower (pr."name") LIKE '%eviss%';


SELECT *
  FROM team_formation tf
 WHERE tf.registration_id = 143;

UPDATE team_formation tf
   SET user_id = 401
 WHERE tf.registration_id = 143 AND tf.user_id = 169;

SELECT *
  FROM registration r
 WHERE r.id = 151;


SELECT *
  FROM profile pr
 WHERE pr.id = 101;

SELECT *
  FROM profile pr
 WHERE lower (pr."name") LIKE '%eldi%';



SELECT *
  FROM profile pr
 WHERE lower (pr."name") LIKE '%pacheco%';

SELECT *
  FROM health h
 WHERE h.id = 405;

SELECT *
  FROM user_account w
 WHERE w.id = 401;

SELECT *
  FROM city c
 WHERE lower (c."name") LIKE '%casa nova%';

SELECT max (c.id)
  FROM city c;


SELECT *
  FROM state prs;