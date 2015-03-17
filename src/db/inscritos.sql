  SELECT u.email
    FROM registration r, team_formation tf, user_account u
   WHERE r.race_id = 1 AND tf.registration_id = r.id AND tf.user_id = u.id
ORDER BY u.email