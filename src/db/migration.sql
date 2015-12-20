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

CREATE MATERIALIZED VIEW race_category_old
AS
   SELECT * FROM race_category;

ALTER TABLE public.registration
   ADD COLUMN race_id_old integer;

ALTER TABLE public.registration
   ADD COLUMN course_id_old integer;

UPDATE registration
   SET race_id_old = race_id, course_id_old = course_id;

--Noite do Perrengue 3, 50Km

UPDATE registration
   SET race_id = 12, course_id = 0
 WHERE race_id = 1 AND course_id = 1;

SELECT *
  FROM registration r
 --SET race_id = 12, course_id = 0
 WHERE r.race_id = 1 AND r.course_id = 1;

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

ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_race_category CASCADE;

UPDATE race_category
   SET category_id = 14
 WHERE race_id = 5 AND course_id = 10;

UPDATE registration
   SET category_id = 14
 WHERE race_id = 5 AND course_id = 10;

ALTER TABLE public.registration ADD CONSTRAINT fk_registration_race_category FOREIGN KEY (race_id,course_id,category_id)
REFERENCES public.race_category (race_id,course_id,category_id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;

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


UPDATE period
   SET ending = '2015-12-02'::date
 WHERE id = 59;


CREATE MATERIALIZED VIEW registration_period_old
AS
   SELECT r.id AS registration_id, p_old.id AS p_old_id, p_new.id AS p_new_id
     --SELECT count(r.*)
     FROM registration r, period p_old, period p_new
    WHERE     r.period_id = p_old.id
          AND r.race_id = p_new.race_id
          AND p_old.beginning = p_new.beginning
          AND p_old.ending = p_new.ending;

  SELECT p_old_id, count (*)
    FROM registration_period_old
GROUP BY p_old_id;

UPDATE registration r
   SET period_id =
          (SELECT o.p_new_id
             FROM registration_period_old o
            WHERE o.registration_id = r.id);


DELETE FROM race_category
      WHERE course_id <> 0;

ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_race_category CASCADE;

ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_race CASCADE;

ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_course CASCADE;

ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_category CASCADE;

ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS pk_race_category CASCADE;

ALTER TABLE public.race_category
   ADD CONSTRAINT pk_race_category PRIMARY KEY (race_id, category_id);

ALTER TABLE public.registration ADD CONSTRAINT fk_registration_race_category FOREIGN KEY (race_id,category_id)
REFERENCES public.race_category (race_id,category_id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_category FOREIGN KEY (category_id)
REFERENCES public.category (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_course FOREIGN KEY (course_id)
REFERENCES public.course (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE public.race_category ALTER COLUMN course_id DROP NOT NULL;

ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_course CASCADE;

ALTER TABLE public.course DROP CONSTRAINT IF EXISTS fk_course_race CASCADE;

DROP INDEX IF EXISTS public.idx_course_race CASCADE;



DROP TABLE IF EXISTS public.course CASCADE;

DELETE FROM race_organizer;

DELETE FROM period p
      WHERE p.race_id IN (SELECT r.id
                            FROM race r
                           WHERE r.event_id = 0);

DELETE FROM race
      WHERE event_id = 0;


DROP MATERIALIZED VIEW IF EXISTS mv_race_category;

DROP MATERIALIZED VIEW IF EXISTS registration_period_old;


ALTER TABLE public.race_category
   DROP COLUMN IF EXISTS course_id CASCADE;

ALTER TABLE public.registration
   DROP COLUMN IF EXISTS course_id CASCADE;


CREATE INDEX idx_registration_race_category ON public.registration
 USING btree
 (
   race_id ASC NULLS LAST,
   category_id ASC NULLS LAST
 );

CREATE INDEX idx_registration_period ON public.registration
 USING btree
 (
   period_id ASC NULLS LAST
 );

ALTER TABLE public.race DROP CONSTRAINT IF EXISTS fk_race_city CASCADE;

DROP INDEX IF EXISTS public.idx_race_payment_type CASCADE;

DROP INDEX IF EXISTS public.idx_race_date CASCADE;

DROP INDEX IF EXISTS public.idx_race_city CASCADE;



CREATE INDEX idx_race_ending ON public.race
 USING btree
 (
   ending ASC NULLS LAST
 );


ALTER TABLE public.race_organizer DROP CONSTRAINT IF EXISTS fk_race_organizer_race CASCADE;

ALTER TABLE public.race_organizer DROP CONSTRAINT IF EXISTS fk_race_organizer CASCADE;

DROP TABLE IF EXISTS public.race_organizer CASCADE;

--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS date CASCADE;
--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS city_id CASCADE;
--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS banner CASCADE;
--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS payment_account CASCADE;
--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS payment_token CASCADE;
--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS payment_type CASCADE;
--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS payment_info CASCADE;
--
-- ALTER TABLE public.race
--    DROP COLUMN IF EXISTS site CASCADE;
--
--
-- ALTER TABLE public.event
--    DROP COLUMN IF EXISTS background_color CASCADE;
--
-- ALTER TABLE public.event
--    DROP COLUMN IF EXISTS button_color CASCADE;
--
-- ALTER TABLE public.event
--    DROP COLUMN IF EXISTS text_color CASCADE;
--
-- ALTER TABLE public.event
--    DROP COLUMN IF EXISTS top_color CASCADE;
