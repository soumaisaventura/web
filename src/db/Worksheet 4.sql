SELECT pr.name AS profile_name
  FROM team_formation tf, user_account us, profile pr
 WHERE tf.registration_id = 1 AND tf.user_id = us.id AND us.id = pr.id;