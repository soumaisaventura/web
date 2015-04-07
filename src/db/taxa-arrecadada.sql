  SELECT pr.name AS nome,
         w.email,
         prc."name" AS cidade,
         prs.abbreviation AS estado
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
--   AND w.id NOT IN (SELECT _tf.user_id
--       FROM registration _r, team_formation _tf
--         WHERE     _r.race_id = r.race_id
--         AND _tf.registration_id = _r.id
--         AND _r.status = 'CONFIRMED')
ORDER BY pr.name;

  SELECT lpad (r.id::text, 3, '0') AS "Inscrição",
         r.team_name AS "Equipe",
         r.date AS "Data da confirmação",
         sum (tf.race_price) AS "Valor da inscrição",
         sum (tf.annual_fee) AS "Taxa anual",
         sum (tf.race_price) + sum (tf.annual_fee) AS "Total"
    FROM registration r, team_formation tf
   WHERE r.race_id = 1 AND tf.registration_id = r.id AND r.status = 'CONFIRMED'
GROUP BY r.id
ORDER BY r.id;