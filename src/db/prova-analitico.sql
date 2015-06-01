  SELECT ca.name || ' ' || co.name,
         (SELECT count (re.*)
            FROM registration re
           WHERE     re.category_id = rc.category_id
                 AND re.course_id = rc.course_id
                 AND re.status = 'CONFIRMED')
            AS count
    FROM race_category rc, category ca, course co
   WHERE rc.race_id = 5 AND rc.category_id = ca.id AND rc.course_id = co.id
ORDER BY co.id, count DESC;

  SELECT co.name,
         (SELECT count (re.*)
            FROM registration re
           WHERE re.course_id = co.id AND re.status = 'CONFIRMED')
            AS count
    FROM course co
   WHERE co.race_id = 5
ORDER BY count DESC;

  SELECT re.status, count (re.*)
    FROM registration re
   WHERE re.race_id = 5
GROUP BY re.status
ORDER BY count (re.*) DESC;

  SELECT ci.name || '/' || st.abbreviation, count (re.*)
    FROM registration re,
         team_formation tf,
         profile pr,
         city ci,
         state st
   WHERE     re.race_id = 5
         AND re.status = 'CONFIRMED'
         AND re.id = tf.registration_id
         AND tf.user_id = pr.id
         AND pr.city_id = ci.id
         AND ci.state_id = st.id
GROUP BY ci.id, st.id
ORDER BY count (re.*) DESC, ci.name;

  SELECT CASE WHEN pr.tshirt IS NULL THEN 'Indefinido' ELSE pr.tshirt END,
         count (re.*)
    FROM registration re, team_formation tf, profile pr
   WHERE     re.race_id = 6
         AND re.status = 'CONFIRMED'
         AND re.id = tf.registration_id
         AND tf.user_id = pr.id
GROUP BY pr.tshirt
ORDER BY count (re.*) DESC;