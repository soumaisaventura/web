DROP VIEW vw_user_registration;

CREATE OR REPLACE VIEW vw_user_registration
AS
   SELECT u.id AS user_id,
          u.email AS user_email,
          p.name AS user_name,
          CASE WHEN p.gender LIKE 'MALE' THEN 'M' ELSE 'F' END AS user_gender,
          pc.id AS user_city_id,
          pc.name AS user_city_name,
          ps.id AS user_state_id,
          ps.name AS user_state_name,
          ps.abbreviation AS user_state_abbreviation,
          p.birthday AS user_birthday,
          extract (YEAR FROM age (p.birthday))::integer AS user_age,
          r.id AS registration_id,
          r.team_name AS registration_team_name,
          r.date AS registration_date,
          extract (MONTH FROM r.date)::integer AS registration_date_month,
          extract (YEAR FROM r.date)::integer AS registration_date_year,
          ra.id AS race_id,
          ra.name AS race_name,
          ra.date AS race_date,
          extract (MONTH FROM ra.date)::integer AS race_date_month,
          extract (YEAR FROM ra.date)::integer AS race_date_year,
          rac.id AS race_city_id,
          rac.name AS race_city_name,
          ras.id AS race_state_id,
          ras.name AS race_state_name,
          ras.abbreviation AS race_state_abbreviation,
          co.id AS course_id,
          co.name AS course_name,
          ct.id AS category_id,
          ct.name AS category_name,
          ct.team_size AS category_team_size,
          ur.race_price AS race_price,
          pe.price AS period_price,
          r.id IN (1,
                   3,
                   6,
                   7,
                   9)
             AS baiano
     FROM user_registration ur,
          user_account u,
          profile p,
          city pc,
          state ps,
          registration r,
          race ra,
          city rac,
          state ras,
          course co,
          category ct,
          period pe
    WHERE     ur.user_id = u.id
          AND u.id = p.id
          AND p.city_id = pc.id
          AND pc.state_id = ps.id
          AND ur.registration_id = r.id
          AND r.race_id = ra.id
          AND r.period_id = pe.id
          AND ra.city_id = rac.id
          AND rac.state_id = ras.id
          AND r.course_id = co.id
          AND r.category_id = ct.id
          AND co.adventure_racing = TRUE
          AND r.status = 'CONFIRMED'
          AND ras.abbreviation IN ('BA', 'PE');

/* Corredores por cidade de residência */

  SELECT aux2.cidade, sum (qtd) AS qtd
    FROM (  SELECT CASE
                      WHEN count (*) < 20
                      THEN
                         'Outras'
                      ELSE
                            aux1.user_city_name
                         || '/'
                         || aux1.user_state_abbreviation
                   END
                      AS cidade,
                   count (*) AS qtd
              FROM (SELECT DISTINCT
                           v.user_id, v.user_city_name, v.user_state_abbreviation
                      FROM vw_user_registration v) aux1
          GROUP BY aux1.user_city_name, aux1.user_state_abbreviation) aux2
GROUP BY aux2.cidade
ORDER BY qtd DESC;


/* Corredores por faixa etária */

  SELECT aux2.faixa_etaria, count (*) AS qtd
    FROM (SELECT CASE
                    WHEN aux1.user_age <= 20
                    THEN
                       ' < 21'
                    WHEN (aux1.user_age > 20 AND aux1.user_age <= 30)
                    THEN
                       '21 - 30'
                    WHEN (aux1.user_age > 30 AND aux1.user_age <= 40)
                    THEN
                       '31 - 40'
                    WHEN (aux1.user_age > 40 AND aux1.user_age <= 50)
                    THEN
                       '41 - 50'
                    WHEN (aux1.user_age > 50 AND aux1.user_age <= 60)
                    THEN
                       '51 - 60'
                    ELSE
                       '> 60'
                 END
                    AS faixa_etaria
            FROM (SELECT DISTINCT v.user_id, v.user_age
                    FROM vw_user_registration v) aux1) aux2
GROUP BY aux2.faixa_etaria
ORDER BY aux2.faixa_etaria;

/* Corredores por faixa etária */

  SELECT aux1.user_gender, count (*) AS qtd
    FROM (SELECT DISTINCT v.user_id, v.user_gender
            FROM vw_user_registration v) aux1
GROUP BY aux1.user_gender
ORDER BY qtd DESC;

/* Inscrições por cidade da provas */

  SELECT v.race_city_name || '/' || v.race_state_abbreviation AS cidade,
         count (*) AS qtd
    FROM vw_user_registration v
GROUP BY v.race_city_name, v.race_state_abbreviation
ORDER BY qtd DESC;


/* Valor arrecadado com inscrições por cidade da provas */

  SELECT v.race_city_name || '/' || v.race_state_abbreviation AS cidade,
         sum (v.race_price) AS arrecadado
    FROM vw_user_registration v
GROUP BY v.race_city_name, v.race_state_abbreviation
ORDER BY arrecadado DESC;


/* Pessoas inscrit por categoria por cidade */

  SELECT v.race_city_name || '/' || v.race_state_abbreviation,
         sum ( (CASE WHEN v.category_team_size = 4 THEN 1 ELSE 0 END))
            AS quartetos,
         sum ( (CASE WHEN v.category_team_size = 2 THEN 1 ELSE 0 END))
            AS duplas
    FROM vw_user_registration v
GROUP BY v.race_city_name, v.race_state_abbreviation
ORDER BY quartetos DESC;


/* Inscrições por categoria  */

SELECT sum ( (CASE WHEN v.category_team_size = 4 THEN 1 ELSE 0 END))
          AS quartetos,
       sum ( (CASE WHEN v.category_team_size = 2 THEN 1 ELSE 0 END))
          AS duplas
  FROM vw_user_registration v;


/* Quantidade de corredores ativos  */

SELECT count(DISTINCT v.user_id)
  FROM vw_user_registration v;

/* Inscrições com desconto  */

SELECT sum (aux1.desconto) AS desconto,
       sum (aux1.Isencao) AS Isencao,
       sum (aux1.integral) AS integral
  FROM (SELECT CASE
                  WHEN v.race_price > 0 AND v.race_price <> v.period_price
                  THEN
                     1
                  ELSE
                     0
               END
                  AS Desconto,
               CASE WHEN v.race_price = 0 THEN 1 ELSE 0 END AS Isencao,
               CASE WHEN v.race_price = v.period_price THEN 1 ELSE 0 END
                  AS integral
          FROM vw_user_registration v) aux1;


/* Frequentcia dos atletas  */

SELECT sum (aux2."1") AS "1",
       sum (aux2."2 - 3") AS "2 - 3",
       sum (aux2."4 - 5") AS "4 - 5",
       sum (aux2."6 - 8") AS "6 - 8",
       sum (aux2."9") AS "9"
  FROM (SELECT CASE WHEN aux1.qtd = 1 THEN 1 ELSE 0 END AS "1",
               CASE WHEN aux1.qtd > 1 AND aux1.qtd <= 3 THEN 1 ELSE 0 END
                  AS "2 - 3",
               CASE WHEN aux1.qtd > 3 AND aux1.qtd <= 5 THEN 1 ELSE 0 END
                  AS "4 - 5",
               CASE WHEN aux1.qtd > 5 AND aux1.qtd <= 8 THEN 1 ELSE 0 END
                  AS "6 - 8",
               CASE WHEN aux1.qtd = 9 THEN 1 ELSE 0 END AS "9"
          FROM (  SELECT v.user_id, count (*) AS qtd
                    FROM vw_user_registration v
                --WHERE race_id <> 11
                GROUP BY v.user_id) aux1) aux2;