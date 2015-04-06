--LPAD (row_number () OVER (ORDER BY _re.id)::text, 2, '0')


SELECT * FROM
(SELECT re.id AS registration_id,
'#' ||LPAD (re.id::text, 4, '0') AS registration_number,
         re.date AS registration_date,
         re.team_number AS team_number,
      re.team_name AS team_name,
         ca.name AS team_category_name,
         co.length AS team_course_length,
         ra.id AS race_id,
         ra.name AS race_name,
         ra.date AS race_date,
         raci.name AS race_city_name,
         rast.abbreviation AS race_state_name,
   us.email AS user_email,
         pr.name AS profile_name,
   substring (pr.gender FOR 1) AS profile_gender,
         pr.birthday AS profile_birthday,
         pr.rg AS profile_rg,
   pr.mobile AS profile_mobile,
    replace(to_char(pr.cpf::numeric, '000=000=000-00'), '=' , '.') AS  profile_cpf,
         prc.name AS profile_city_name,
         prs.abbreviation AS profile_state_name,
         he.allergy AS health_allergy,
         substring (he.blood_type FOR 1) ||  CASE WHEN position('_P' IN he.blood_type) > 0 THEN '+' ELSE '-' END AS health_blood_type,
     he.health_care_name AS health_care_name,
     he.health_care_number AS health_care_number,
     he.emergency_contact_name AS health_emergency_contact_name,
     he.emergency_contact_phone_number AS health_emergency_contact_phone
    FROM (  SELECT '#'  || LPAD (_re.id::text, 3, '0')
                      AS team_number,
                   _re.*
              FROM registration _re
             WHERE _re.race_id = 1 AND _re.status = 'CONFIRMED'
          ORDER BY _re.id) re,
         race_category rc,
            race ra,
            category ca,
            course co,
            city raci,
           state rast,
            team_formation tf,
           user_account us,
           profile pr,
           city prc,
           state prs,
           health he
   WHERE     re.race_id = rc.race_id
         AND re.category_id = rc.category_id
         AND re.course_id = rc.course_id
        AND rc.race_id = ra.id
        AND rc.category_id = ca.id
        AND rc.course_id = co.id
        AND ra.city_id = raci.id
       AND raci.state_id = rast.id
        AND re.id = tf.registration_id
         AND tf.user_id = us.id
         AND us.id = pr.id
         AND pr.city_id = prc.id
         AND prc.state_id = prs.id
         AND us.id = he.id
UNION ALL
SELECT NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       r.id AS race_id,
       r.name AS race_name,
       r.date AS race_date,
       rc.name AS race_city_name,
       rs.abbreviation AS race_state_name,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL
  FROM race r, city rc, state rs, (SELECT 1 UNION SELECT 2) _2
 WHERE r.id = 1 AND r.city_id = rc.id AND rc.state_id = rs.id) f
 ORDER BY f.registration_id;

SELECT *
  FROM (SELECT 1
        UNION
        SELECT 2) a,
       (SELECT 3) b;


SELECT pr.*
  FROM team_formation tf, profile pr
 WHERE tf.registration_id = 1 AND tf.user_id = pr.id;



SELECT * FROM registration_forms;