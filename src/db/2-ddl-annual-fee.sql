
CREATE TABLE public.annual_fee(
	year smallint NOT NULL,
	fee numeric(3,2),
	CONSTRAINT pk_annual_fee PRIMARY KEY (year)
);


ALTER TABLE public.annual_fee OWNER TO postgres;


CREATE TABLE public.annual_fee_payment(
	year smallint NOT NULL,
	user_id smallint NOT NULL,
	registration_id bigint NOT NULL,
	CONSTRAINT pk_annual_fee_payment PRIMARY KEY (year,user_id)
);


ALTER TABLE public.annual_fee_payment OWNER TO postgres;


CREATE INDEX idx_anual_fee_payment_registration ON public.annual_fee_payment
	USING btree
	(
	  registration_id ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);



ALTER TABLE public.registration ADD COLUMN period_id integer NULL;


update public.registration set period_id = 0;


ALTER TABLE public.registration ALTER COLUMN period_id SET NOT NULL;


ALTER TABLE public.health ADD CONSTRAINT fk_health_user FOREIGN KEY (id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.profile ADD CONSTRAINT fk_profile_user FOREIGN KEY (id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.race_organizer ADD CONSTRAINT fk_race_organizer FOREIGN KEY (organizer_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.registration ADD CONSTRAINT fk_registration_user FOREIGN KEY (submitter_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;




ALTER TABLE public.registration ADD CONSTRAINT fk_resgistration_period FOREIGN KEY (period_id)
REFERENCES public.period (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.team_formation ADD CONSTRAINT fk_team_formation_user FOREIGN KEY (user_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_annual_fee FOREIGN KEY (year)
REFERENCES public.annual_fee (year) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_user FOREIGN KEY (user_id)
REFERENCES public.user_account (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_registration FOREIGN KEY (registration_id)
REFERENCES public.registration (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE public.annual_fee ALTER COLUMN fee TYPE numeric(5,2);

insert into public.annual_fee (year, fee) values (2015, 10);

ALTER TABLE public.period ALTER COLUMN price TYPE numeric(7,2);


DROP INDEX IF EXISTS public.idx_team_formation_registration CASCADE;


DROP INDEX IF EXISTS public.idx_team_formation_user CASCADE;


DROP INDEX IF EXISTS public.idx_race_organizer CASCADE;


DROP INDEX IF EXISTS public.idx_race_organizer_race CASCADE;


DROP INDEX IF EXISTS public.idx_race_category_category CASCADE;


DROP INDEX IF EXISTS public.idx_race_category_course CASCADE;


DROP INDEX IF EXISTS public.idx_race_category_race CASCADE;


ALTER TABLE public.annual_fee ALTER COLUMN year TYPE integer;


ALTER TABLE public.annual_fee_payment ALTER COLUMN year TYPE integer;


ALTER TABLE public.annual_fee_payment ALTER COLUMN user_id TYPE integer;
