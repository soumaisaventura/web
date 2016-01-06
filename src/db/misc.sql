SELECT *
  FROM city c, state s
 WHERE c.state_id = s.id AND lower (c.name) LIKE '%petrolina%';

SELECT *
  FROM user_account ua
 WHERE ua.email LIKE '%manoelabb@gmail.com%';

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
 WHERE     tf.user_id = p.id
       AND tf.registration_id = re.id
       AND re.race_id = ra.id
       AND tf.user_id IN (1309, 1316);

SELECT *
  FROM user_registration tf, profile p
 WHERE tf.user_id = p.id AND tf.registration_id = 543;

SELECT *
  FROM profile p, city c
 WHERE p.city_id = c.id AND lower (p.name) LIKE '%josilene%';

UPDATE user_registration
   SET user_id = 302
 WHERE registration_id = 543 AND user_id = 309;

UPDATE registration
   --SET category_id = 2
   SET course_id = 9
 WHERE id = 320;

  SELECT count (*) AS inscritos,
         pc.name || ', ' || ps.name || ', Brazil' AS cidade
    FROM registration r,
         race a,
         user_registration t,
         city rc,
         state rs,
         city pc,
         state ps,
         profile p
   WHERE     r.race_id = a.id
         AND t.registration_id = r.id
         AND t.user_id = p.id
         AND r.status = 'CONFIRMED'
         AND a.city_id = rc.id
         AND rc.state_id = rs.id
         AND p.city_id = pc.id
         AND pc.state_id = ps.id
GROUP BY pc.name, ps.name
ORDER BY count (*) DESC;

  SELECT rc.name || ', ' || rs.name || ', Brazil' AS "Cidade da prova",
         count (*) AS "Inscrições"
    FROM registration r,
         race a,
         course c,
         user_registration t,
         city rc,
         state rs,
         city pc,
         state ps,
         profile p
   WHERE     r.race_id = a.id
         AND t.registration_id = r.id
         AND t.user_id = p.id
         AND r.status = 'CONFIRMED'
         AND a.city_id = rc.id
         AND r.course_id = c.id
         AND c.adventure_racing = TRUE
         AND rc.state_id = rs.id
         AND p.city_id = pc.id
         AND pc.state_id = ps.id
         AND rs.abbreviation IN ('BA', 'PE')
         AND r.race_id IN (1,
                           3,
                           6,
                           7,
                           9)
GROUP BY rc.name, rs.name
ORDER BY count (*) DESC;

  SELECT row_number () OVER (ORDER BY p."name") AS "#",
         p."name" AS "Federado",
         c."name" || '/' || s.abbreviation AS "Cidade"
    FROM fee_payment afp,
         profile p,
         city c,
         state s
   WHERE     afp.fee_id = 1
         AND afp.user_id = p.id
         AND p.city_id = c.id
         AND c.state_id = s.id
ORDER BY p."name" ASC;

  SELECT pc.name || ', ' || ps.name || ', Brazil'
            AS "Cidade de residência do atleta",
         count (*) AS "Inscrições"
    FROM registration r,
         race a,
         course c,
         user_registration t,
         city rc,
         state rs,
         city pc,
         state ps,
         profile p
   WHERE     r.race_id = a.id
         AND t.registration_id = r.id
         AND t.user_id = p.id
         AND r.status = 'CONFIRMED'
         AND a.city_id = rc.id
         AND r.course_id = c.id
         AND c.adventure_racing = TRUE
         AND rc.state_id = rs.id
         AND p.city_id = pc.id
         AND pc.state_id = ps.id
         AND rs.abbreviation IN ('BA', 'PE')
GROUP BY pc.name, ps.name
ORDER BY count (*) DESC;

SELECT count (*)
  FROM (SELECT DISTINCT p.*
          FROM registration r,
               race a,
               course c,
               user_registration t,
               city rc,
               state rs,
               city pc,
               state ps,
               profile p
         WHERE     r.race_id = a.id
               AND t.registration_id = r.id
               AND t.user_id = p.id
               AND r.status = 'CONFIRMED'
               AND a.city_id = rc.id
               AND r.course_id = c.id
               AND c.adventure_racing = TRUE
               AND rc.state_id = rs.id
               AND p.city_id = pc.id
               AND pc.state_id = ps.id
               AND r.race_id IN (1,
                                 3,
                                 6,
                                 7,
                                 9)
               AND rs.abbreviation IN ('BA', 'PE')) x;

  SELECT DISTINCT
         e.email, e.name, e.city || ', ' || e.state || ', Brazil' AS location
    FROM (SELECT ro.alternate_email AS email,
                 p.name,
                 c."name" AS city,
                 s.abbreviation AS state
            FROM race_organizer ro,
                 profile p,
                 race r,
                 city c,
                 state s
           WHERE     ro.race_id = r.id
                 AND ro.organizer_id = p.id
                 AND r.city_id = c.id
                 AND c.state_id = s.id
                 AND s.abbreviation IN ('BA', 'PE')
                 AND ro.alternate_email IS NOT NULL
          UNION
          SELECT u.email,
                 p.name,
                 c."name",
                 s.abbreviation AS state
            FROM race_organizer ro,
                 user_account u,
                 profile p,
                 race r,
                 city c,
                 state s
           WHERE     ro.organizer_id = u.id
                 AND u.id = p.id
                 AND ro.race_id = r.id
                 AND r.city_id = c.id
                 AND c.state_id = s.id
                 AND s.abbreviation IN ('BA', 'PE')) e
ORDER BY e.email ASC;

  SELECT p."name",
         u.email,
         p.gender,
         p.birthday
    FROM profile p, user_account u
   WHERE p.id = u.id AND u.activation IS NOT NULL
ORDER BY p."name";

  SELECT p."name" AS nome,
         u.email,
         c."name" AS cidade,
         s."name" AS estado,
         (SELECT count (*)
            FROM fee_payment _a
           WHERE _a.user_id = u.id AND _a.fee_id = 1) > 0
            AS federado,
         (SELECT count (*)
            FROM race_organizer _ro
           WHERE _ro.organizer_id = u.id) > 0
            AS organizador,
         (SELECT count (*)
            FROM user_registration _t,
                 registration _re,
                 race _ra,
                 course _co
           WHERE     _t.registration_id = _re.id
                 AND _re.race_id = _ra.id
                 AND _re.course_id = _co.id
                 AND _t.user_id = u.id
                 AND _co.adventure_racing = TRUE
                 AND _re.status = 'CONFIRMED') > 0
            AS aventureiro
    FROM user_account u,
         profile p,
         city c,
         state s
   WHERE u.id = p.id AND p.city_id = c.id AND c.state_id = s.id
ORDER BY p."name";

  SELECT e.name AS event_name, r.name AS race_name, r.id AS race_id
    FROM event e, race r
   WHERE r.event_id = e.id AND r.id > 11
ORDER BY r.event_id, e.id;

  SELECT DISTINCT p.name, u.email
    FROM registration r,
         course c,
         user_account u,
         user_registration tf,
         profile p
   WHERE     r.status = 'CONFIRMED'
         AND r.id = tf.registration_id
         AND tf.user_id = u.id
         AND p.id = u.id
         AND r.course_id = c.id
         AND r.race_id IN (2,
                           5,
                           7,
                           9,
                           11)
         AND c.adventure_racing = TRUE
ORDER BY p."name";

  SELECT e.slug,
         r.id,
         r.name,
         r.distance
    FROM race r, event e
   WHERE r.event_id = e.id AND r.id > 11
ORDER BY e.id, r.id;

----



---------------------

SELECT * FROM tabelao;

SELECT * FROM questao;

DROP TABLE IF EXISTS questao;



SELECT attname, replace (attname, 'q_', '') AS num
  FROM pg_attribute
 WHERE attrelid = 'tabelao'::regclass AND attname LIKE 'q_%';


-----
