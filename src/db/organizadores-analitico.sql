-- quantidade de inscrições pode sexo
SELECT
  CASE WHEN pr.gender = 'MALE'
    THEN 'Homens'
  ELSE 'Mulheres' END AS sexo,
  count(*)            AS quantidade
FROM
  registration re,
  race ra,
  event ev,
  city rae,
  state ste,
  user_registration ur,
  user_account ua,
  profile pr
WHERE re.race_id = ra.id
      AND ra.sport_id = 1
      AND ra.event_id = ev.id
      AND ev.city_id = rae.id
      AND rae.state_id = ste.id
      AND ste.abbreviation = 'BA'
      AND re.status = 'CONFIRMED'
      AND ur.registration_id = re.id
      AND ur.user_id = ua.id
      AND pr.id = ua.id
      AND extract(YEAR FROM ra.beginning) = 2015
GROUP BY sexo
ORDER BY quantidade DESC;

-- quantidade de atletas pode sexo
SELECT
  CASE WHEN pr.gender = 'MALE'
    THEN 'Homens'
  ELSE 'Mulheres' END   AS sexo,
  count(DISTINCT ua.id) AS quantidade
FROM
  registration re,
  race ra,
  event ev,
  city rae,
  state ste,
  user_registration ur,
  user_account ua,
  profile pr
WHERE re.race_id = ra.id
      AND ra.sport_id = 1
      AND ra.event_id = ev.id
      AND ev.city_id = rae.id
      AND rae.state_id = ste.id
      AND ste.abbreviation = 'BA'
      AND re.status = 'CONFIRMED'
      AND ur.registration_id = re.id
      AND ur.user_id = ua.id
      AND pr.id = ua.id
      AND extract(YEAR FROM ra.beginning) = 2016
GROUP BY sexo
ORDER BY quantidade DESC;

-- quantidade de inscricoes por cidade sede da prova
CREATE OR REPLACE VIEW vw_inscricoes_cidade_sede AS
  SELECT
    rae.name                        AS cidade,
    ste.abbreviation                AS estado,
    extract(YEAR FROM ra.beginning) AS ano,
    count(*)                        AS quantidade
  FROM
    registration re,
    race ra,
    event ev,
    city rae,
    state ste,
    user_registration ur,
    user_account ua,
    profile pr
  WHERE re.race_id = ra.id
        AND ra.sport_id = 1
        AND ra.event_id = ev.id
        AND ev.city_id = rae.id
        AND rae.state_id = ste.id
        AND ste.abbreviation = 'BA'
        AND re.status = 'CONFIRMED'
        AND ur.registration_id = re.id
        AND ur.user_id = ua.id
        AND pr.id = ua.id
  GROUP BY cidade, estado, ano
  ORDER BY quantidade DESC;

SELECT
  aux.cidade,
  aux.estado,
  sum(aux.qtd_2015) AS qtd_2015,
  sum(aux.qtd_2016) AS qtd_2016
FROM (
       SELECT
         v1.cidade,
         v1.estado,
         v1.quantidade AS qtd_2015,
         0             AS qtd_2016
       FROM vw_inscricoes_cidade_sede v1
       WHERE v1.ano = 2015
       UNION
       SELECT
         v2.cidade,
         v2.estado,
         0             AS qtd_2015,
         v2.quantidade AS qtd_2016
       FROM vw_inscricoes_cidade_sede v2
       WHERE v2.ano = 2016) aux
GROUP BY aux.cidade,
  aux.estado
ORDER BY sum(aux.qtd_2015 + aux.qtd_2016) DESC;

-- quantidade de inscricoes por cidade de residencia
CREATE OR REPLACE VIEW vw_inscricoes_cidade_residencia AS
  SELECT
    rau.name                        AS cidade,
    stu.abbreviation                AS estado,
    EXTRACT(YEAR FROM ra.beginning) AS ano,
    count(*)                        AS quantidade
  FROM
    registration re,
    race ra,
    EVENT ev,
    city rae,
    state ste,
    city rau,
    state stu,
    user_registration ur,
    user_account ua,
    profile pr
  WHERE re.race_id = ra.id
        AND ra.sport_id = 1
        AND ra.event_id = ev.id
        AND ev.city_id = rae.id
        AND rae.state_id = ste.id
        AND ste.abbreviation = 'BA'
        AND re.status = 'CONFIRMED'
        AND ur.registration_id = re.id
        AND ur.user_id = ua.id
        AND pr.id = ua.id
        AND pr.city_id = rau.id
        AND rau.state_id = stu.id
  GROUP BY cidade,
    estado, ano
  ORDER BY quantidade DESC;

SELECT
  aux.cidade,
  aux.estado,
  sum(aux.qtd_2015) AS qtd_2015,
  sum(aux.qtd_2016) AS qtd_2016
FROM (
       SELECT
         v1.cidade,
         v1.estado,
         v1.quantidade AS qtd_2015,
         0             AS qtd_2016
       FROM vw_inscricoes_cidade_residencia v1
       WHERE v1.ano = 2015
       UNION
       SELECT
         v2.cidade,
         v2.estado,
         0             AS qtd_2015,
         v2.quantidade AS qtd_2016
       FROM vw_inscricoes_cidade_residencia v2
       WHERE v2.ano = 2016) aux
GROUP BY aux.cidade,
  aux.estado
ORDER BY sum(aux.qtd_2015 + aux.qtd_2016) DESC;

-- quantidade de atletas por cidade de residencia
CREATE OR REPLACE VIEW vw_atletas_cidade_residencia AS
  SELECT
    rau.name                        AS cidade,
    stu.abbreviation                AS estado,
    extract(YEAR FROM ra.beginning) AS ano,
    count(DISTINCT ua.id)           AS quantidade
  FROM
    registration re,
    race ra,
    event ev,
    city rae,
    state ste,
    city rau,
    state stu,
    user_registration ur,
    user_account ua,
    profile pr
  WHERE re.race_id = ra.id
        AND ra.sport_id = 1
        AND ra.event_id = ev.id
        AND ev.city_id = rae.id
        AND rae.state_id = ste.id
        AND ste.abbreviation = 'BA'
        AND re.status = 'CONFIRMED'
        AND ur.registration_id = re.id
        AND ur.user_id = ua.id
        AND pr.id = ua.id
        AND pr.city_id = rau.id
        AND rau.state_id = stu.id
  GROUP BY cidade,
    estado, ano
  ORDER BY quantidade DESC;

SELECT
  aux.cidade,
  aux.estado,
  sum(aux.qtd_2015) AS qtd_2015,
  sum(aux.qtd_2016) AS qtd_2016
FROM (
       SELECT
         v1.cidade,
         v1.estado,
         v1.quantidade AS qtd_2015,
         0             AS qtd_2016
       FROM vw_atletas_cidade_residencia v1
       WHERE v1.ano = 2015
       UNION
       SELECT
         v2.cidade,
         v2.estado,
         0             AS qtd_2015,
         v2.quantidade AS qtd_2016
       FROM vw_atletas_cidade_residencia v2
       WHERE v2.ano = 2016) aux
GROUP BY aux.cidade,
  aux.estado
ORDER BY sum(aux.qtd_2015 + aux.qtd_2016) DESC;

-- Novos atletas em 2016 em relação aos anos anteriores.
SELECT count(*)
FROM (
       SELECT DISTINCT ua.id AS atleta
       FROM
         registration re,
         race ra,
         event ev,
         city rae,
         state ste,
         city rau,
         state stu,
         user_registration ur,
         user_account ua,
         profile pr
       WHERE re.race_id = ra.id
             AND ra.sport_id = 1
             AND ra.event_id = ev.id
             AND ev.city_id = rae.id
             AND rae.state_id = ste.id
             AND ste.abbreviation = 'BA'
             AND re.status = 'CONFIRMED'
             AND ur.registration_id = re.id
             AND ur.user_id = ua.id
             AND pr.id = ua.id
             AND extract(YEAR FROM ra.beginning) = 2016
             AND pr.city_id = rau.id
             AND rau.state_id = stu.id
       INTERSECT
       SELECT DISTINCT ua.id AS atleta
       FROM
         registration re,
         race ra,
         event ev,
         city rae,
         state ste,
         city rau,
         state stu,
         user_registration ur,
         user_account ua,
         profile pr
       WHERE re.race_id = ra.id
             AND ra.sport_id = 1
             AND ra.event_id = ev.id
             AND ev.city_id = rae.id
             AND rae.state_id = ste.id
             AND ste.abbreviation = 'BA'
             AND re.status = 'CONFIRMED'
             AND ur.registration_id = re.id
             AND ur.user_id = ua.id
             AND pr.id = ua.id
             AND extract(YEAR FROM ra.beginning) = 2015
             AND pr.city_id = rau.id
             AND rau.state_id = stu.id) aux;