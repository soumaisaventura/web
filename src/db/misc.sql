SELECT sum (CASE WHEN pr.pendencies = 0 THEN 1 ELSE 0 END) AS ok,
       sum (CASE WHEN pr.pendencies = 1 THEN 1 ELSE 0 END) AS pendente,
       sum (CASE WHEN pr.pendencies > 1 THEN 1 ELSE 0 END) AS outras
  FROM profile pr;

SELECT count (*)
  FROM profile pr
 WHERE pr.pendencies = 1 AND pr.tshirt IS NULL;

SELECT count (*)
  FROM user_account u
 WHERE u.activation IS NOT NULL;

SELECT count (*)
  FROM profile pr
 WHERE pr.pendencies = 0;

SELECT count (*)
  FROM annual_fee_payment a
 WHERE a.year = 2015;

SELECT * FROM race;

SELECT *
  FROM profile pr
 WHERE pr.pendencies = 1 AND tshirt IS NOT NULL;

  SELECT *
    FROM profile pr
   WHERE lower (pr."name") LIKE '%anne%'
ORDER BY id;

UPDATE profile
   SET pendencies = 1
 WHERE id = 2;

SELECT *
  FROM user_account
 WHERE id IN (665);

SELECT count (*)
  FROM user_account ua
 WHERE ua.activation IS NOT NULL;

SELECT *
  FROM team_formation t
 WHERE user_id IN (665);

SELECT count(DISTINCT t.user_id)
  FROM team_formation t;

SELECT *
  FROM profile
 WHERE id IN (29);

  SELECT p.name, count (p.*)
    FROM profile p, user_account u
   WHERE p.id = u.id AND u.deleted IS NULL
GROUP BY p.name
  HAVING count (p.*) > 1
ORDER BY count (p.*) DESC, p.name;

SELECT *
  FROM user_account u
 WHERE u.deleted IS NULL;

DELETE FROM user_account
      WHERE deleted IS NOT NULL;

SELECT *
  FROM team_formation tf
 WHERE tf.registration_id = 248;


UPDATE team_formation
   SET user_id = 616
 WHERE user_id = 401 AND registration_id = 248;


-- 401 marcel
-- 616



-- 56

-- 484
-- 61

UPDATE team_formation
   SET user_id = 61
 WHERE registration_id = 191 AND user_id = 484;




--SELECT count (*)

SELECT *
  FROM (  SELECT DISTINCT
                 p.name,
                 p.pendencies + h.pendencies,
                 CASE
                    WHEN a.activation IS NULL
                    THEN
                       to_date ('1900-01-01', 'YYYY-MM-DD')
                    ELSE
                       a.activation
                 END,
                 a.*
            --  SELECT count (a.id)
            FROM user_account a,
                 profile p,
                 health h,
                 profile p2
           WHERE     a.id = p.id
                 AND a.id = h.id
                 --AND (   p.pendencies + h.pendencies > 0
                 --     OR (p.pendencies = 1 AND p.tshirt IS NULL))
                 AND (p.pendencies + h.pendencies > 0)
                 --               AND (    p.pendencies = 1
                 --                    AND h.pendencies = 0
                 --                    AND p.tshirt IS NULL)
                 AND p.name = p2.name
                 AND p.id <> p2.id
                 AND NOT EXISTS
                        (SELECT *
                           FROM team_formation _t
                          WHERE _t.user_id = a.id)
                 AND NOT EXISTS
                        (SELECT *
                           FROM annual_fee_payment _a
                          WHERE _a.user_id = a.id)
                 AND NOT EXISTS
                        (SELECT *
                           FROM race_organizer _r
                          WHERE _r.organizer_id = a.id)
        ORDER BY p.name,
                 p.pendencies + h.pendencies DESC,
                 CASE
                    WHEN a.activation IS NULL
                    THEN
                       to_date ('1900-01-01', 'YYYY-MM-DD')
                    ELSE
                       a.activation
                 END,
                 a.creation) x;

SELECT to_date ('1900-01-01', 'YYYY-MM-DD');

--ORDER BY a.id;

SELECT *
  FROM profile pr
 WHERE lower (pr."name") LIKE '%jacicarmen%';

SELECT count (*) FROM user_account;

SELECT *
  FROM user_account a
 WHERE a.email = 'tataizinhabela@hotmail.com';

UPDATE user_account
   SET email = 'jacicarmemsousa@hotmail.com'
 WHERE email = 'jacicarmensousa@hotmail.com';


UPDATE user_account
   SET deleted = now ()
 WHERE email = 'jacicarmensousa@hotmail.com';



  SELECT *
    FROM profile pr
   WHERE pr.id = 515
ORDER BY id;

SELECT *
  FROM category ca;

UPDATE registration
   SET category_id = 12
 WHERE race_id = 6 AND course_id = 9 AND category_id = 4;

SELECT *
  FROM registration rc
 WHERE rc.race_id = 6 AND rc.course_id = 9 AND rc.category_id = 4;

SELECT *
  FROM race_category rc
 WHERE rc.race_id = 6 AND rc.course_id = 9 AND rc.category_id = 4;

SELECT *
  FROM user_account
 WHERE id IN (505, 516);

DELETE FROM user_account
      WHERE id NOT IN (SELECT _x.id
                         FROM health _x);

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
 WHERE h.id = 616;

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
 WHERE lower (pr."name") LIKE '%vinicius eduardo%';

SELECT c.*
  FROM city c, state s
 WHERE     c.state_id = s.id
       AND s.abbreviation = 'BA'
       AND lower (c."name") LIKE '%vale%';

SELECT *
  FROM profile pr
 WHERE lower (pr."name") LIKE '%pacheco%';

SELECT *
  FROM health h
 WHERE h.id = 405;

SELECT *
  FROM user_account w
 WHERE w.id = 471;

SELECT *
  FROM city c
 WHERE lower (c."name") LIKE '%casa nova%';

SELECT max (c.id)
  FROM city c;

SELECT *
  FROM state prs;