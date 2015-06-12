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