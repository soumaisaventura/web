SELECT *
FROM event
ORDER BY id DESC;

SELECT *
FROM race
ORDER BY id DESC;

SELECT *
FROM sport;

SELECT max(id) + 1
FROM race;

INSERT INTO event (id,
                   slug,
                   name,
                   site,
                   city_id,
                   payment_type,
                   payment_info,
                   payment_account,
                   payment_token,
                   _status_id,
                   description)
VALUES (
  (SELECT max(id) + 1
   FROM event),
  'copaamerica/2016',
  'Copa América de Corrida de Aventura 2016',
  'http://cbcaventura.org.br',
  756,
  'MANUAL',
  NULL,
  NULL,
  NULL,
  1,
  'O Circuito Terra Brasilis de CA tem como objetivo estimular o desenvolvimento da Corrida de Aventura no Estado do CE propiciando aos participantes vivenciar os verdadeiros valores do esporte: trabalho em equipe, amizades e contato com a natureza. Circuito válido pelo Campeonato Cearense de Corrida de Aventura com a chancela da FECECA – Federação Cearense de Corrida de Aventura e da CBCA – Confederação Brasileira de Corrida de Aventura. Após o final das 4 etapas, o campeão do percurso a partir de 50km terá direito à vaga para representar o Ceará na grande Final do Campeonato Brasileiro de CA 2016.');

INSERT INTO race (id,
                  slug,
                  name,
                  event_id,
                  sport_id,
                  beginning,
                  ending,
                  distance,
                  _status_id,
                  description)
VALUES (
  (SELECT max(id) + 1
   FROM race),
  'expedicao',
  'Expedição',
  30,
  1,
  '2016-08-27',
  '2016-08-28',
  100,
  1,
  'Percurso com pelo menos 100 km que tem como objetivo estimular a participação de atletas experientes de outros Estados e propiciar o contato dos atletas cearenses com tais. Será realizada a classificação no mesmo modelo dos demais percursos. As equipes que competirem neste percurso e que estiverem competindo pelo Campeonato Cearense terão seus pontos dobrados caso consigam concluir a prova ou atingir 80% dos PCs.');

INSERT INTO event_organizer (event_id,
                             organizer_id,
                             alternate_name,
                             alternate_email)
VALUES (31,
        805,
        'Péricles Rodrigues',
        NULL);

INSERT INTO race_category (race_id, category_id)
  SELECT
    r.id AS race_id,
    c.id AS category_id
  FROM race r, category c
  WHERE r.id IN (34)
        AND c.id IN (1, 8);

INSERT INTO race_modality (race_id, modality_id)
  SELECT
    r.id AS race_id,
    m.id AS modality_id
  FROM race r, modality m
  WHERE r.id IN (34)
        AND m.id IN (1, 2, 3, 4, 5, 6);

-- 54, 58

INSERT INTO period (race_id,
                    price,
                    beginning,
                    ending,
                    id)
VALUES (34,
        175.00,
        TO_DATE('16/10/2016', 'DD/MM/YYYY'),
        TO_DATE('25/10/2016', 'DD/MM/YYYY'),
        (SELECT max(id) + 1
         FROM period));

INSERT INTO kit (id,
                 race_id,
                 alias,
                 name,
                 description,
                 price)
VALUES ((SELECT max(id) + 1
         FROM kit),
        64,
        'mapaextra',
        'Kit Mapa Extra',
        'Em breve mais informações',
        20);


UPDATE user_registration x
SET kit_id = 5
WHERE exists(
    SELECT *
    FROM user_registration ur, registration r
    WHERE ur.registration_id = r.id
          AND kit_id IS NULL
          AND r.race_id = 63
          AND ur.user_id = x.user_id
          AND ur.registration_id = x.registration_id);



