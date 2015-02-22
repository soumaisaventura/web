-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.8.0-beta2
-- PostgreSQL version: 9.4
-- Project Site: pgmodeler.com.br
-- Model Author: ---

-- object: adminendyfwd | type: ROLE --
-- DROP ROLE IF EXISTS adminendyfwd;
CREATE ROLE adminendyfwd WITH 
	SUPERUSER;
-- ddl-end --


-- Database creation must be done outside an multicommand file.
-- These commands were put in this file only for convenience.
-- -- object: fbca | type: DATABASE --
-- -- DROP DATABASE IF EXISTS fbca;
-- CREATE DATABASE fbca
-- 	ENCODING = 'UTF8'
-- 	LC_COLLATE = 'en_US.UTF8'
-- 	LC_CTYPE = 'en_US.UTF8'
-- 	TABLESPACE = pg_default
-- 	OWNER = adminendyfwd
-- ;
-- -- ddl-end --
-- 

-- object: public.category | type: TABLE --
-- DROP TABLE IF EXISTS public.category CASCADE;
CREATE TABLE public.category(
	id integer NOT NULL,
	name character varying(50) NOT NULL,
	description character varying(100) NOT NULL,
	team_size integer NOT NULL,
	min_male_members integer,
	min_female_members integer,
	CONSTRAINT category_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.category OWNER TO postgres;
-- ddl-end --

-- object: public.city | type: TABLE --
-- DROP TABLE IF EXISTS public.city CASCADE;
CREATE TABLE public.city(
	id integer NOT NULL,
	state_id integer NOT NULL,
	name character varying(50) NOT NULL,
	CONSTRAINT city_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.city OWNER TO postgres;
-- ddl-end --

-- object: public.country | type: TABLE --
-- DROP TABLE IF EXISTS public.country CASCADE;
CREATE TABLE public.country(
	id integer NOT NULL,
	name character varying(50) NOT NULL,
	abbreviation character varying(2) NOT NULL,
	CONSTRAINT country_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.country OWNER TO postgres;
-- ddl-end --

-- object: public.course | type: TABLE --
-- DROP TABLE IF EXISTS public.course CASCADE;
CREATE TABLE public.course(
	id integer NOT NULL,
	race_id integer NOT NULL,
	length integer NOT NULL,
	CONSTRAINT pk_course PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.course OWNER TO postgres;
-- ddl-end --

-- object: public.health | type: TABLE --
-- DROP TABLE IF EXISTS public.health CASCADE;
CREATE TABLE public.health(
	id integer NOT NULL,
	blood_type character varying(20),
	allergy character varying(500),
	health_care_name character varying(50),
	health_care_number character varying(20),
	emergency_contact_name character varying(50),
	emergency_contact_phone_number character varying(15),
	pendencies integer NOT NULL,
	CONSTRAINT health_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.health OWNER TO postgres;
-- ddl-end --

-- object: public.period | type: TABLE --
-- DROP TABLE IF EXISTS public.period CASCADE;
CREATE TABLE public.period(
	id integer NOT NULL,
	race_id integer NOT NULL,
	beginning date NOT NULL,
	ending date NOT NULL,
	price numeric(7,2) NOT NULL,
	CONSTRAINT pk_period PRIMARY KEY (id),
	CONSTRAINT uk_period_beginning UNIQUE (race_id,beginning),
	CONSTRAINT uk_period_ending UNIQUE (race_id,ending)

);
-- ddl-end --
ALTER TABLE public.period OWNER TO postgres;
-- ddl-end --

-- object: public.profile | type: TABLE --
-- DROP TABLE IF EXISTS public.profile CASCADE;
CREATE TABLE public.profile(
	id integer NOT NULL,
	name character varying(50) NOT NULL,
	gender character varying(20) NOT NULL,
	birthday date,
	rg character varying(10),
	cpf character varying(11),
	mobile character varying(15),
	city_id integer,
	pendencies integer NOT NULL,
	CONSTRAINT profile_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.profile OWNER TO postgres;
-- ddl-end --

-- object: public.race | type: TABLE --
-- DROP TABLE IF EXISTS public.race CASCADE;
CREATE TABLE public.race(
	id integer NOT NULL,
	name character varying(50) NOT NULL,
	description character varying(500),
	date date NOT NULL,
	city_id integer,
	banner oid,
	logo oid,
	payment_account character varying(255),
	payment_token character varying(64),
	CONSTRAINT race_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.race OWNER TO postgres;
-- ddl-end --

-- object: public.race_category | type: TABLE --
-- DROP TABLE IF EXISTS public.race_category CASCADE;
CREATE TABLE public.race_category(
	race_id integer NOT NULL,
	course_id integer NOT NULL,
	category_id integer NOT NULL,
	CONSTRAINT race_category_pkey PRIMARY KEY (category_id,course_id,race_id)

);
-- ddl-end --
ALTER TABLE public.race_category OWNER TO postgres;
-- ddl-end --

-- object: public.race_organizer | type: TABLE --
-- DROP TABLE IF EXISTS public.race_organizer CASCADE;
CREATE TABLE public.race_organizer(
	race_id integer NOT NULL,
	organizer_id integer NOT NULL,
	CONSTRAINT race_organizer_pkey PRIMARY KEY (organizer_id,race_id)

);
-- ddl-end --
ALTER TABLE public.race_organizer OWNER TO postgres;
-- ddl-end --

-- object: public.registration | type: TABLE --
-- DROP TABLE IF EXISTS public.registration CASCADE;
CREATE TABLE public.registration(
	id bigint NOT NULL,
	race_id integer NOT NULL,
	course_id integer NOT NULL,
	category_id integer NOT NULL,
	period_id integer NOT NULL,
	team_name character varying(50) NOT NULL,
	date timestamp NOT NULL,
	submitter_id integer NOT NULL,
	status character varying(20) NOT NULL,
	status_date timestamp NOT NULL,
	approver_id integer,
	payment_transaction character varying(64),
	payment_code character varying(64),
	CONSTRAINT pk_registration PRIMARY KEY (id),
	CONSTRAINT uk_registration_payment_code UNIQUE (payment_code),
	CONSTRAINT uk_registration_payment_transaction UNIQUE (payment_transaction)

);
-- ddl-end --
ALTER TABLE public.registration OWNER TO postgres;
-- ddl-end --

-- object: public.state | type: TABLE --
-- DROP TABLE IF EXISTS public.state CASCADE;
CREATE TABLE public.state(
	id integer NOT NULL,
	country_id integer NOT NULL,
	name character varying(50) NOT NULL,
	abbreviation character varying(2) NOT NULL,
	CONSTRAINT state_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.state OWNER TO postgres;
-- ddl-end --

-- object: public.team_formation | type: TABLE --
-- DROP TABLE IF EXISTS public.team_formation CASCADE;
CREATE TABLE public.team_formation(
	registration_id bigint NOT NULL,
	user_id integer NOT NULL,
	race_price numeric(7,2) NOT NULL,
	annual_fee numeric(5,2) NOT NULL,
	CONSTRAINT team_formation_pkey PRIMARY KEY (registration_id,user_id)

);
-- ddl-end --
ALTER TABLE public.team_formation OWNER TO postgres;
-- ddl-end --

-- object: public.user_account | type: TABLE --
-- DROP TABLE IF EXISTS public.user_account CASCADE;
CREATE TABLE public.user_account(
	id integer NOT NULL,
	email character varying(255) NOT NULL,
	password character varying(64),
	activation timestamp,
	activation_token character varying(64),
	password_reset_request timestamp,
	password_reset_token character varying(64),
	creation timestamp NOT NULL,
	deleted timestamp,
	admin boolean NOT NULL DEFAULT false,
	CONSTRAINT pk_user_account PRIMARY KEY (id),
	CONSTRAINT uk_user_account_email UNIQUE (email)

);
-- ddl-end --
ALTER TABLE public.user_account OWNER TO postgres;
-- ddl-end --

-- object: idx_category_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_category_name CASCADE;
CREATE INDEX idx_category_name ON public.category
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_city_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_city_name CASCADE;
CREATE INDEX idx_city_name ON public.city
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_city_state | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_city_state CASCADE;
CREATE INDEX idx_city_state ON public.city
	USING btree
	(
	  state_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_country_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_country_name CASCADE;
CREATE INDEX idx_country_name ON public.country
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_country_abbreviation | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_country_abbreviation CASCADE;
CREATE INDEX idx_country_abbreviation ON public.country
	USING btree
	(
	  abbreviation
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_course_race | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_course_race CASCADE;
CREATE INDEX idx_course_race ON public.course
	USING btree
	(
	  race_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_period_race | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_period_race CASCADE;
CREATE INDEX idx_period_race ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_profile_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_profile_name CASCADE;
CREATE INDEX idx_profile_name ON public.profile
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_user_city | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_user_city CASCADE;
CREATE INDEX idx_user_city ON public.profile
	USING btree
	(
	  city_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_profile_cpf | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_profile_cpf CASCADE;
CREATE INDEX idx_profile_cpf ON public.profile
	USING btree
	(
	  cpf
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_race_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_race_name CASCADE;
CREATE INDEX idx_race_name ON public.race
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_race_date | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_race_date CASCADE;
CREATE INDEX idx_race_date ON public.race
	USING btree
	(
	  date
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_race_city | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_race_city CASCADE;
CREATE INDEX idx_race_city ON public.race
	USING btree
	(
	  city_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_registration_date | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_registration_date CASCADE;
CREATE INDEX idx_registration_date ON public.registration
	USING btree
	(
	  date
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_registration_submitter | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_registration_submitter CASCADE;
CREATE INDEX idx_registration_submitter ON public.registration
	USING btree
	(
	  submitter_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_state_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_state_name CASCADE;
CREATE INDEX idx_state_name ON public.state
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_state_abbreviation | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_state_abbreviation CASCADE;
CREATE INDEX idx_state_abbreviation ON public.state
	USING btree
	(
	  abbreviation
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_state_country | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_state_country CASCADE;
CREATE INDEX idx_state_country ON public.state
	USING btree
	(
	  country_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_user_email | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_user_email CASCADE;
CREATE INDEX idx_user_email ON public.user_account
	USING btree
	(
	  email
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_user_deleted | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_user_deleted CASCADE;
CREATE INDEX idx_user_deleted ON public.user_account
	USING btree
	(
	  deleted
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.seq_category | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_category CASCADE;
CREATE SEQUENCE public.seq_category
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_category OWNER TO postgres;
-- ddl-end --

-- object: public.seq_course | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_course CASCADE;
CREATE SEQUENCE public.seq_course
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_course OWNER TO postgres;
-- ddl-end --

-- object: public.seq_period | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_period CASCADE;
CREATE SEQUENCE public.seq_period
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_period OWNER TO postgres;
-- ddl-end --

-- object: public.seq_race | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_race CASCADE;
CREATE SEQUENCE public.seq_race
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_race OWNER TO postgres;
-- ddl-end --

-- object: public.seq_receipt | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_receipt CASCADE;
CREATE SEQUENCE public.seq_receipt
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_receipt OWNER TO postgres;
-- ddl-end --

-- object: public.seq_registration | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_registration CASCADE;
CREATE SEQUENCE public.seq_registration
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_registration OWNER TO postgres;
-- ddl-end --

-- object: public.seq_user | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_user CASCADE;
CREATE SEQUENCE public.seq_user
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.seq_user OWNER TO postgres;
-- ddl-end --

-- object: idx_registration_status | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_registration_status CASCADE;
CREATE INDEX idx_registration_status ON public.registration
	USING btree
	(
	  status ASC NULLS LAST
	);
-- ddl-end --

-- object: idx_period_ending | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_period_ending CASCADE;
CREATE UNIQUE INDEX idx_period_ending ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST,
	  ending
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_period_beginning | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_period_beginning CASCADE;
CREATE UNIQUE INDEX idx_period_beginning ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST,
	  beginning ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.annual_fee | type: TABLE --
-- DROP TABLE IF EXISTS public.annual_fee CASCADE;
CREATE TABLE public.annual_fee(
	year integer NOT NULL,
	fee numeric(5,2),
	CONSTRAINT pk_annual_fee PRIMARY KEY (year)

);
-- ddl-end --
ALTER TABLE public.annual_fee OWNER TO postgres;
-- ddl-end --

-- object: public.annual_fee_payment | type: TABLE --
-- DROP TABLE IF EXISTS public.annual_fee_payment CASCADE;
CREATE TABLE public.annual_fee_payment(
	year integer NOT NULL,
	user_id integer NOT NULL,
	registration_id bigint NOT NULL,
	CONSTRAINT pk_annual_fee_payment PRIMARY KEY (year,user_id)

);
-- ddl-end --
ALTER TABLE public.annual_fee_payment OWNER TO postgres;
-- ddl-end --

-- object: idx_anual_fee_payment_registration | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_anual_fee_payment_registration CASCADE;
CREATE INDEX idx_anual_fee_payment_registration ON public.annual_fee_payment
	USING btree
	(
	  registration_id ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: adminpack | type: EXTENSION --
-- DROP EXTENSION IF EXISTS adminpack CASCADE;
CREATE EXTENSION adminpack
      WITH SCHEMA pg_catalog
      VERSION '1.0';
-- ddl-end --
COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
-- ddl-end --

-- object: idx_registration_approver | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_registration_approver CASCADE;
CREATE INDEX idx_registration_approver ON public.registration
	USING btree
	(
	  approver_id ASC NULLS LAST
	);
-- ddl-end --

-- object: idx_user_admin | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_user_admin CASCADE;
CREATE INDEX idx_user_admin ON public.user_account
	USING btree
	(
	  admin ASC NULLS LAST
	);
-- ddl-end --

-- object: fk_city_state | type: CONSTRAINT --
-- ALTER TABLE public.city DROP CONSTRAINT IF EXISTS fk_city_state CASCADE;
ALTER TABLE public.city ADD CONSTRAINT fk_city_state FOREIGN KEY (state_id)
REFERENCES public.state (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_course_race | type: CONSTRAINT --
-- ALTER TABLE public.course DROP CONSTRAINT IF EXISTS fk_course_race CASCADE;
ALTER TABLE public.course ADD CONSTRAINT fk_course_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_health_user | type: CONSTRAINT --
-- ALTER TABLE public.health DROP CONSTRAINT IF EXISTS fk_health_user CASCADE;
ALTER TABLE public.health ADD CONSTRAINT fk_health_user FOREIGN KEY (id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_period_race | type: CONSTRAINT --
-- ALTER TABLE public.period DROP CONSTRAINT IF EXISTS fk_period_race CASCADE;
ALTER TABLE public.period ADD CONSTRAINT fk_period_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_profile_user | type: CONSTRAINT --
-- ALTER TABLE public.profile DROP CONSTRAINT IF EXISTS fk_profile_user CASCADE;
ALTER TABLE public.profile ADD CONSTRAINT fk_profile_user FOREIGN KEY (id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_user_city | type: CONSTRAINT --
-- ALTER TABLE public.profile DROP CONSTRAINT IF EXISTS fk_user_city CASCADE;
ALTER TABLE public.profile ADD CONSTRAINT fk_user_city FOREIGN KEY (city_id)
REFERENCES public.city (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_city | type: CONSTRAINT --
-- ALTER TABLE public.race DROP CONSTRAINT IF EXISTS fk_race_city CASCADE;
ALTER TABLE public.race ADD CONSTRAINT fk_race_city FOREIGN KEY (city_id)
REFERENCES public.city (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_category_category | type: CONSTRAINT --
-- ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_category CASCADE;
ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_category FOREIGN KEY (category_id)
REFERENCES public.category (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_category_race | type: CONSTRAINT --
-- ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_race CASCADE;
ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_category_course | type: CONSTRAINT --
-- ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_course CASCADE;
ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_course FOREIGN KEY (course_id)
REFERENCES public.course (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_organizer_race | type: CONSTRAINT --
-- ALTER TABLE public.race_organizer DROP CONSTRAINT IF EXISTS fk_race_organizer_race CASCADE;
ALTER TABLE public.race_organizer ADD CONSTRAINT fk_race_organizer_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_organizer | type: CONSTRAINT --
-- ALTER TABLE public.race_organizer DROP CONSTRAINT IF EXISTS fk_race_organizer CASCADE;
ALTER TABLE public.race_organizer ADD CONSTRAINT fk_race_organizer FOREIGN KEY (organizer_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_registration_submitter | type: CONSTRAINT --
-- ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_submitter CASCADE;
ALTER TABLE public.registration ADD CONSTRAINT fk_registration_submitter FOREIGN KEY (submitter_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_registration_race_category | type: CONSTRAINT --
-- ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_race_category CASCADE;
ALTER TABLE public.registration ADD CONSTRAINT fk_registration_race_category FOREIGN KEY (category_id,course_id,race_id)
REFERENCES public.race_category (category_id,course_id,race_id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_resgistration_period | type: CONSTRAINT --
-- ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_resgistration_period CASCADE;
ALTER TABLE public.registration ADD CONSTRAINT fk_resgistration_period FOREIGN KEY (period_id)
REFERENCES public.period (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_registration_approver | type: CONSTRAINT --
-- ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_approver CASCADE;
ALTER TABLE public.registration ADD CONSTRAINT fk_registration_approver FOREIGN KEY (approver_id)
REFERENCES public.user_account (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_state_country | type: CONSTRAINT --
-- ALTER TABLE public.state DROP CONSTRAINT IF EXISTS fk_state_country CASCADE;
ALTER TABLE public.state ADD CONSTRAINT fk_state_country FOREIGN KEY (country_id)
REFERENCES public.country (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_team_formation_user | type: CONSTRAINT --
-- ALTER TABLE public.team_formation DROP CONSTRAINT IF EXISTS fk_team_formation_user CASCADE;
ALTER TABLE public.team_formation ADD CONSTRAINT fk_team_formation_user FOREIGN KEY (user_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_team_formation_registration | type: CONSTRAINT --
-- ALTER TABLE public.team_formation DROP CONSTRAINT IF EXISTS fk_team_formation_registration CASCADE;
ALTER TABLE public.team_formation ADD CONSTRAINT fk_team_formation_registration FOREIGN KEY (registration_id)
REFERENCES public.registration (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_anual_fee_payment_annual_fee | type: CONSTRAINT --
-- ALTER TABLE public.annual_fee_payment DROP CONSTRAINT IF EXISTS fk_anual_fee_payment_annual_fee CASCADE;
ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_annual_fee FOREIGN KEY (year)
REFERENCES public.annual_fee (year) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_anual_fee_payment_user | type: CONSTRAINT --
-- ALTER TABLE public.annual_fee_payment DROP CONSTRAINT IF EXISTS fk_anual_fee_payment_user CASCADE;
ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_user FOREIGN KEY (user_id)
REFERENCES public.user_account (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_anual_fee_payment_registration | type: CONSTRAINT --
-- ALTER TABLE public.annual_fee_payment DROP CONSTRAINT IF EXISTS fk_anual_fee_payment_registration CASCADE;
ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_registration FOREIGN KEY (registration_id)
REFERENCES public.registration (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --


