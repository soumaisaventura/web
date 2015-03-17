  SELECT *
    FROM registration r
   WHERE r.race_id = 1
ORDER BY id;

SELECT *
  FROM team_formation tf,
       user_account u,
       profile p,
       city c,
	   state s
 WHERE     tf.registration_id = 1
       AND tf.user_id = u.id
       AND u.id = p.id
       AND p.city_id = c.id
	   and c.state_id s.id