
ALTER TABLE public.period DROP CONSTRAINT IF EXISTS pk_period CASCADE;

ALTER TABLE public.period ADD COLUMN id integer;

update period set id=0 where price=799;

update period set id=1 where price=80;

update period set id=2 where price=90;

update period set id=3 where price=100;

SELECT setval('seq_period', 3);

ALTER TABLE public.course DROP CONSTRAINT IF EXISTS course_pkey CASCADE;

ALTER TABLE public.course ADD CONSTRAINT pk_course PRIMARY KEY (id);

ALTER TABLE public.period ADD CONSTRAINT pk_period PRIMARY KEY (id);


CREATE INDEX idx_period_race ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);


CREATE INDEX idx_period_begin ON public.period
	USING btree
	(
	  beginning ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);


ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_course FOREIGN KEY (course_id)
REFERENCES public.course (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.user_account DROP CONSTRAINT IF EXISTS user_account_email_key CASCADE;

ALTER TABLE public.user_account DROP CONSTRAINT IF EXISTS user_account_pkey CASCADE;

DROP INDEX IF EXISTS public.idx_period_begin CASCADE;

ALTER TABLE public.user_account ADD CONSTRAINT pk_user_account PRIMARY KEY (id);

ALTER TABLE public.user_account ADD CONSTRAINT uk_user_account_email UNIQUE (email);

CREATE UNIQUE INDEX idx_period_beginning ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST,
	  beginning ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);


ALTER TABLE public.period ADD CONSTRAINT uk_period_beginning UNIQUE (race_id,beginning);

ALTER TABLE public.period ADD CONSTRAINT uk_period_ending UNIQUE (race_id,ending);

