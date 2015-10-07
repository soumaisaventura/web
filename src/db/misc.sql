SELECT *
  FROM city c, state s
 WHERE c.state_id = s.id AND lower (c.name) LIKE '%petrolina%';

SELECT *
  FROM user_account ua
 WHERE ua.email LIKE '%manoelabb@gmail.com%';

SELECT *
  FROM user_account ua
 WHERE id IN (50);

SELECT *
  FROM profile p
 WHERE p.id IN (116, 284);

SELECT *
  FROM health h
 WHERE h.id = 581;


SELECT *
  FROM team_formation tf,
       profile p,
       registration re,
       race ra
 WHERE     tf.user_id = p.id
       AND tf.registration_id = re.id
       AND re.race_id = ra.id
       AND tf.user_id = 359;

SELECT *
  FROM team_formation tf, profile p
 WHERE tf.user_id = p.id AND tf.registration_id = 543;

SELECT *
  FROM profile p, city c
 WHERE p.city_id = c.id AND lower (p.name) LIKE '%zadson%';

UPDATE team_formation
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
         team_formation t,
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
         team_formation t,
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

  SELECT pc.name || ', ' || ps.name || ', Brazil'
            AS "Cidade de residência do atleta",
         count (*) AS "Inscrições"
    FROM registration r,
         race a,
         course c,
         team_formation t,
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
               team_formation t,
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