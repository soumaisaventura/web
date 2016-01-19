-- Database diff generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.8.0
-- PostgreSQL version: 9.4

-- [ Diff summary ]
-- Dropped objects: 0
-- Created objects: 3
-- Changed objects: 0
-- Truncated tables: 0

-- [ Created objects ] --
-- object: public.hotspot | type: TABLE --
-- DROP TABLE IF EXISTS public.hotspot CASCADE;

CREATE TABLE public.hotspot
(
   id                integer NOT NULL,
   event_id          integer NOT NULL,
   name              CHARACTER VARYING (30) NOT NULL,
   coord_latitude    numeric (9, 7) NOT NULL,
   coord_longitude   numeric (10, 7) NOT NULL,
   main              boolean NOT NULL,
   CONSTRAINT pk_hotspot PRIMARY KEY (id)
);

-- ddl-end --

ALTER TABLE public.hotspot
   OWNER TO postgres;

-- ddl-end --

-- object: idx_hotspot_event | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_hotspot_event CASCADE;
CREATE INDEX idx_hotspot_event ON public.hotspot
 USING btree
 (
   event_id ASC NULLS LAST
 );
-- ddl-end --

-- object: idx_hotspot_main | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_hotspot_main CASCADE;
CREATE INDEX idx_hotspot_main ON public.hotspot
 USING btree
 (
   main ASC NULLS LAST
 );
-- ddl-end --

-- object: fk_hotspot_event | type: CONSTRAINT --
-- ALTER TABLE public.hotspot DROP CONSTRAINT IF EXISTS fk_hotspot_event CASCADE;
ALTER TABLE public.hotspot ADD CONSTRAINT fk_hotspot_event FOREIGN KEY (event_id)
REFERENCES public.event (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --


INSERT INTO hotspot (id,
                     event_id,
                     name,
                     coord_latitude,
                     coord_longitude,
                     main)
   SELECT row_number () OVER (ORDER BY e.id) AS id,
          e.id AS event_id,
          'Briefing técnico' AS name,
          e.coord_latitude,
          e.coord_longitude,
          TRUE AS main
     FROM event e
    WHERE e.coord_latitude IS NOT NULL;



-- [ Created objects ] --
-- object: order | type: COLUMN --
-- ALTER TABLE public.hotspot DROP COLUMN IF EXISTS order CASCADE;

ALTER TABLE public.hotspot
   ADD COLUMN sequence integer;

-- ddl-end --

UPDATE hotspot
   SET sequence = 1;

-- [ Changed objects ] --

ALTER TABLE public.hotspot
   ALTER COLUMN sequence SET NOT NULL;

-- ddl-end --


-- [ Dropped objects ] --

ALTER TABLE public.event
   DROP COLUMN IF EXISTS coord_latitude CASCADE;

-- ddl-end --

ALTER TABLE public.event
   DROP COLUMN IF EXISTS coord_longitude CASCADE;

-- ddl-end --



-- [ Dropped objects ] --

ALTER TABLE public.event
   DROP COLUMN IF EXISTS coord_latitude CASCADE;

-- ddl-end --

ALTER TABLE public.event
   DROP COLUMN IF EXISTS coord_longitude CASCADE;

-- ddl-end --

-- [ Created objects ] --
-- object: description | type: COLUMN --
-- ALTER TABLE public.hotspot DROP COLUMN IF EXISTS description CASCADE;

ALTER TABLE public.hotspot
   ADD COLUMN description CHARACTER VARYING (50);

-- ddl-end --

UPDATE hotspot
   SET description = 'Briefing técnico e concentração para a largada.';

ALTER TABLE public.hotspot
   ALTER COLUMN description SET NOT NULL;