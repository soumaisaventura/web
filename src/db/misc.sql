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
  FROM user_registration tf,
       profile p,
       registration re,
       race ra
 WHERE     tf.user_id = p.id
       AND tf.registration_id = re.id
       AND re.race_id = ra.id
       AND tf.user_id = 359;

SELECT *
  FROM user_registration tf, profile p
 WHERE tf.user_id = p.id AND tf.registration_id = 543;

SELECT *
  FROM profile p, city c
 WHERE p.city_id = c.id AND lower (p.name) LIKE '%zadson%';

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

UPDATE race
   SET slug = id;

INSERT INTO race (id,
                  name,
                  date,
                  payment_type,
                  event_id,
                  sport_id,
                  beginning,
                  ending,
                  slug,
                  name_2,
                  visible)
     SELECT 11 + row_number () OVER (ORDER BY r.id, c.id),
            c."name",
            r."date",
            'MANUAL',
            r.event_id,
            CASE WHEN c.adventure_racing THEN 1 ELSE 2 END,
            r."date",
            r."date",
            c."name",
            c."name",
            FALSE
       FROM course c, race r
      WHERE c.race_id = r.id AND c.name NOT LIKE '0'
   ORDER BY r.id, c.id;

CREATE TABLE course_new_race
AS
     SELECT 11 + row_number () OVER (ORDER BY r.id, c.id) AS new_race_id,
            c.id AS course_id
       FROM course c, race r
      WHERE c.race_id = r.id AND c.name NOT LIKE '0'
   ORDER BY r.id, c.id;

SELECT * FROM course_new_race;

INSERT INTO race_category (category_id, race_id, course_id)
     SELECT rc.category_id, cnr.new_race_id, 0 AS course_id
       FROM race_category rc, course_new_race cnr
      WHERE rc.course_id = cnr.course_id
   ORDER BY cnr.new_race_id, rc.category_id;

DELETE FROM race_category rc
      WHERE race_id > 11;

SELECT *
  FROM championship_race c
 WHERE race_id > 11;

DELETE FROM championship_race c
      WHERE race_id <= 11;

SELECT *
  FROM championship c;

  SELECT e.name, r.name, r.id
    FROM event e, race r
   WHERE r.event_id = e.id AND r.id > 11
ORDER BY e.id, r.id;

DELETE FROM race_modality
      WHERE race_id <= 11;

INSERT INTO race_modality (race_id, modality_id)
   --"Noite do Perrengue 3";"50Km";12
   SELECT 12, 1
   UNION
   SELECT 12, 2
   UNION
   SELECT 12, 3
   UNION
   SELECT 12, 6
   UNION
   --"Sol do Salitre";"50Km";13
   SELECT 13, 1
   UNION
   SELECT 13, 2
   UNION
   SELECT 13, 5
   UNION
   SELECT 13, 6
   UNION
   --"Mandacaru";"60Km";14
   SELECT 14, 1
   UNION
   SELECT 14, 2
   UNION
   SELECT 14, 5
   UNION
   SELECT 14, 6
   UNION
   --"Peleja";"59Km";15
   SELECT 15, 1
   UNION
   SELECT 15, 2
   UNION
   SELECT 15, 6
   UNION
   --"Casco de Peba";"Emotion 40Km";16
   SELECT 16, 1
   UNION
   SELECT 16, 2
   UNION
   SELECT 16, 3
   UNION
   SELECT 16, 4
   UNION
   SELECT 16, 5
   UNION
   SELECT 16, 6
   UNION
   SELECT 16, 7
   UNION
   --"Casco de Peba";"Turbinado 90Km";17
   SELECT 17, 1
   UNION
   SELECT 17, 2
   UNION
   SELECT 17, 3
   UNION
   SELECT 17, 4
   UNION
   SELECT 17, 5
   UNION
   SELECT 17, 6
   UNION
   SELECT 17, 7
   UNION
   --"Casco de Peba";"Cross Country 12Km";18
   SELECT 18, 7
   UNION
   --"Casco de Peba";"Off-Road Aventura 4x4";19
   SELECT 19, 8
   UNION
   --"Corrida do CT Gantuá";"Aventura";20
   SELECT 20, 1
   UNION
   SELECT 20, 2
   UNION
   SELECT 20, 4
   UNION
   SELECT 20, 6
   UNION
   --"Corrida do CT Gantuá";"Trail Run";21
   SELECT 21, 7
   UNION
   --"Laskpé";"SPORT ou PRO";22
   SELECT 22, 1
   UNION
   SELECT 22, 2
   UNION
   SELECT 22, 3
   UNION
   SELECT 22, 5
   UNION
   SELECT 22, 6
   UNION
   --"Cangaço";"60km";23
   SELECT 23, 1
   UNION
   SELECT 23, 2
   UNION
   SELECT 23, 6
   UNION
   SELECT 23, 7
   UNION
   --"Cangaço";"30km";24
   SELECT 24, 1
   UNION
   SELECT 24, 2
   UNION
   SELECT 24, 6
   UNION
   SELECT 24, 7
   UNION
   --"Desafio dos Sertões";"SPORT ou PRO";25
   SELECT 25, 1
   UNION
   SELECT 25, 2
   UNION
   SELECT 25, 3
   UNION
   SELECT 25, 4
   UNION
   SELECT 25, 6
   UNION
   --"2ª Etapa do Circuito Native";"Extreme 42km";26
   SELECT 26, 1
   UNION
   SELECT 26, 2
   UNION
   SELECT 26, 6
   UNION
   --"2ª Etapa do Circuito Native";"Expedição 16km";27
   SELECT 27, 2
   UNION
   SELECT 27, 6
   UNION
   --"2ª Etapa do Circuito Native";"Aventura 10km";28
   SELECT 28, 2
   UNION
   SELECT 28, 6
   UNION
   --"Integração Aventura Urbana";"Race 50km";29
   SELECT 29, 1
   UNION
   SELECT 29, 2
   UNION
   SELECT 29, 3
   UNION
   SELECT 29, 6;

-- 1;"Mountain Biking";"mtb";""
-- 2;"Trekking";"trk";""
-- 3;"Canoagem";"can";""
-- 4;"Técnicas Verticais";"vrt";""
-- 5;"Natação";"nat";""
-- 6;"Orientação";"ORI";""


INSERT INTO championship_race (championship_id, race_id)
     VALUES (1, 12);


INSERT INTO championship_race (championship_id, race_id)
     VALUES (1, 14);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (1, 20);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (1, 22);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (1, 25);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (2, 14);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (2, 15);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (2, 23);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (3, 13);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (3, 17);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (3, 22);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (3, 25);

INSERT INTO championship_race (championship_id, race_id)
     VALUES (3, 29);

SELECT * FROM race_fee;



INSERT INTO period (id,
                    race_id,
                    beginning,
                    ending,
                    price)
     SELECT 24 + row_number () OVER (ORDER BY cnr.new_race_id, p.beginning)
               AS id,
            cnr.new_race_id,
            p.beginning,
            p.ending,
            p.price
       FROM period p, course_new_race cnr, course c
      WHERE cnr.course_id = c.id AND c.race_id = p.race_id
   ORDER BY cnr.new_race_id, p.beginning;

UPDATE race _r
   SET city_id =
          (SELECT r.city_id
             FROM course c, race r, course_new_race c1
            WHERE     c.race_id = r.id
                  AND c1.course_id = c.id
                  AND c1.new_race_id = _r.id)
 WHERE id > 11;

SELECT *
  FROM race_modality r;