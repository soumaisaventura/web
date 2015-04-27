SELECT count (*)
  FROM (  SELECT            --'#' || lpad (r.id::text, 4, '0') AS "Inscrição",
                pr.name AS "Nome",
                 w.email AS "E-mail",
                 prc."name" AS "Cidade",
                 prs.abbreviation AS "Estado",
                 ra."name" AS "Prova"
            FROM annual_fee_payment a,
                 registration r,
                 race ra,
                 user_account w,
                 profile pr,
                 city prc,
                 state prs
           WHERE     a.registration_id = r.id
                 AND r.race_id = ra.id
                 AND a.user_id = w.id
                 AND w.id = pr.id
                 AND r.race_id = 1
                 AND pr.city_id = prc.id
                 AND prc.state_id = prs.id
        --   and r.status = 'CANCELLED'
        --   AND w.id NOT IN (SELECT _tf.user_id
        --       FROM registration _r, team_formation _tf
        --         WHERE     _r.race_id = r.race_id
        --         AND _tf.registration_id = _r.id
        --         AND _r.status = 'CONFIRMED')
        ORDER BY pr.name) AS x;


SELECT DISTINCT status
  FROM registration;

  SELECT '#' || lpad (r.id::text, 3, '0') AS "Inscrição",
         r.team_name AS "Equipe",
         ca."name" || ' ' || co.length || ' km' AS "Categoria",
         r.date AS "Data da confirmação",
         sum (tf.race_price) AS "Valor da inscrição",
         sum (tf.annual_fee) AS "Taxa anual",
         sum (tf.race_price) + sum (tf.annual_fee) AS "Total"
    FROM registration r,
         team_formation tf,
         race_category rc,
         category ca,
         course co
   WHERE     r.race_id = 3
         AND tf.registration_id = r.id
         AND r.status = 'CONFIRMED'
         AND rc.race_id = r.race_id
         AND rc.category_id = r.category_id
         AND rc.course_id = r.course_id
         AND rc.course_id = co.id
         AND rc.category_id = ca.id
GROUP BY r.id, ca.id, co.id
ORDER BY r.id;

SELECT * FROM annual_fee;


-- Na NP3 tem q colocar 1 taxa a mais
  SELECT ra."name" AS prova,
         count (tf.user_id) AS "Inscritos",
         count (a.user_id) AS "Quantidade de taxas",
         sum (CASE WHEN af.fee IS NULL THEN 0 ELSE af.fee END)
            AS "Total arrecadado com taxas"
    FROM race ra,
         registration re,
         team_formation tf
         LEFT JOIN annual_fee_payment a
            ON (    tf.registration_id = a.registration_id
                AND tf.user_id = a.user_id)
         LEFT JOIN annual_fee af ON (a.year = af.year)
   WHERE     tf.registration_id = re.id
         AND re.status = 'CONFIRMED'
         AND re.race_id = ra.id
GROUP BY ra.id;