SELECT    '--'
       || race_name
       || ', '
       || course_name
       || '
update registration set race_id = xx, course_id = 0 where race_id = '
       || x.race_id
       || ' and course_id = '
       || x.course_id
       || ';'
          AS sql
  FROM (  SELECT DISTINCT a.id AS race_id,
                          a.name AS race_name,
                          c.id AS course_id,
                          c.name AS course_name
            FROM registration r, race a, course c
           WHERE r.race_id = a.id AND r.course_id = c.id AND a.id <= 11
        ORDER BY a.id, c.id) x;

----

UPDATE registration
   SET race_id_old = race_id, course_id_old = course_id;

--create MATERIALIZED view mv_race_category as select * from race_category;

--drop MATERIALIZED view mv_race_category;

--select * from mv_race_category;

--Noite do Perrengue 3, 50Km

UPDATE registration
   SET race_id = 12, course_id = 0
 WHERE race_id = 1 AND course_id = 1;

--Sol do Salitre, 50Km

UPDATE registration
   SET race_id = 13, course_id = 0
 WHERE race_id = 2 AND course_id = 3;

--Mandacaru, 60Km

UPDATE registration
   SET race_id = 14, course_id = 0
 WHERE race_id = 3 AND course_id = 2;

--Peleja, 59Km

UPDATE registration
   SET race_id = 15, course_id = 0
 WHERE race_id = 4 AND course_id = 4;

--Casco de Peba, Emotion 40Km

UPDATE registration
   SET race_id = 16, course_id = 0
 WHERE race_id = 5 AND course_id = 5;

--Casco de Peba, Turbinado 90Km

UPDATE registration
   SET race_id = 17, course_id = 0
 WHERE race_id = 5 AND course_id = 6;

--Casco de Peba, Cross Country 12Km

UPDATE registration
   SET race_id = 18, course_id = 0
 WHERE race_id = 5 AND course_id = 7;

--Casco de Peba, Off-Road Aventura 4x4

UPDATE registration
   SET race_id = 19, course_id = 0
 WHERE race_id = 5 AND course_id = 10;

--Corrida do CT Gantuá, Aventura

UPDATE registration
   SET race_id = 20, course_id = 0
 WHERE race_id = 6 AND course_id = 8;

--Corrida do CT Gantuá, Trail Run

UPDATE registration
   SET race_id = 21, course_id = 0
 WHERE race_id = 6 AND course_id = 9;

--Laskpé, SPORT ou PRO

UPDATE registration
   SET race_id = 22, course_id = 0
 WHERE race_id = 7 AND course_id = 12;

--Cangaço, 60km

UPDATE registration
   SET race_id = 23, course_id = 0
 WHERE race_id = 8 AND course_id = 15;

--Cangaço, 30km

UPDATE registration
   SET race_id = 24, course_id = 0
 WHERE race_id = 8 AND course_id = 16;

--Desafio dos Sertões, SPORT ou PRO

UPDATE registration
   SET race_id = 25, course_id = 0
 WHERE race_id = 9 AND course_id = 17;

--2ª Etapa do Circuito Native, Extreme 42km

UPDATE registration
   SET race_id = 26, course_id = 0
 WHERE race_id = 10 AND course_id = 11;

--2ª Etapa do Circuito Native, Expedição 16km

UPDATE registration
   SET race_id = 27, course_id = 0
 WHERE race_id = 10 AND course_id = 13;

--2ª Etapa do Circuito Native, Aventura 10km

UPDATE registration
   SET race_id = 28, course_id = 0
 WHERE race_id = 10 AND course_id = 14;

--Integração Aventura Urbana, Race 50km

UPDATE registration
   SET race_id = 29, course_id = 0
 WHERE race_id = 11 AND course_id = 18;

UPDATE registration
   SET category_id = 14
 WHERE race_id = 19 AND category_id = 9;

DELETE FROM race_category
      WHERE course_id <> 0;