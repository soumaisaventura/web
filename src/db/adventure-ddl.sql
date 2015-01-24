-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.8.0-beta1
-- PostgreSQL version: 9.4
-- Project Site: pgmodeler.com.br
-- Model Author: ---


-- Database creation must be done outside an multicommand file.
-- These commands were put in this file only for convenience.
-- -- object: fbca | type: DATABASE --
-- -- DROP DATABASE IF EXISTS fbca;
-- CREATE DATABASE fbca
-- 	ENCODING = 'UTF8'
-- 	LC_COLLATE = 'en_US.UTF8'
-- 	LC_CTYPE = 'en_US.UTF8'
-- 	TABLESPACE = pg_default
-- 	OWNER = postgres
-- ;
-- -- ddl-end --
-- 

-- object: public.category | type: TABLE --
-- DROP TABLE IF EXISTS public.category;
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
-- DROP TABLE IF EXISTS public.city;
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
-- DROP TABLE IF EXISTS public.country;
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
-- DROP TABLE IF EXISTS public.course;
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
-- DROP TABLE IF EXISTS public.health;
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
-- DROP TABLE IF EXISTS public.period;
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
-- DROP TABLE IF EXISTS public.profile;
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
-- DROP TABLE IF EXISTS public.race;
CREATE TABLE public.race(
	id integer NOT NULL,
	name character varying(50) NOT NULL,
	description character varying(500),
	date date NOT NULL,
	city_id integer,
	banner oid,
	CONSTRAINT race_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.race OWNER TO postgres;
-- ddl-end --

-- object: public.race_category | type: TABLE --
-- DROP TABLE IF EXISTS public.race_category;
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
-- DROP TABLE IF EXISTS public.race_organizer;
CREATE TABLE public.race_organizer(
	race_id integer NOT NULL,
	organizer_id integer NOT NULL,
	CONSTRAINT race_organizer_pkey PRIMARY KEY (organizer_id,race_id)

);
-- ddl-end --
ALTER TABLE public.race_organizer OWNER TO postgres;
-- ddl-end --

-- object: public.registration | type: TABLE --
-- DROP TABLE IF EXISTS public.registration;
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
	CONSTRAINT registration_pkey PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.registration OWNER TO postgres;
-- ddl-end --

-- object: public.state | type: TABLE --
-- DROP TABLE IF EXISTS public.state;
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
-- DROP TABLE IF EXISTS public.team_formation;
CREATE TABLE public.team_formation(
	registration_id bigint NOT NULL,
	user_id integer NOT NULL,
	CONSTRAINT team_formation_pkey PRIMARY KEY (registration_id,user_id)

);
-- ddl-end --
ALTER TABLE public.team_formation OWNER TO postgres;
-- ddl-end --

-- object: public.user_account | type: TABLE --
-- DROP TABLE IF EXISTS public.user_account;
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
	CONSTRAINT pk_user_account PRIMARY KEY (id),
	CONSTRAINT uk_user_account_email UNIQUE (email)

);
-- ddl-end --
ALTER TABLE public.user_account OWNER TO postgres;
-- ddl-end --

-- object: idx_category_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_category_name;
CREATE INDEX idx_category_name ON public.category
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_city_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_city_name;
CREATE INDEX idx_city_name ON public.city
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_city_state | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_city_state;
CREATE INDEX idx_city_state ON public.city
	USING btree
	(
	  state_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_country_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_country_name;
CREATE INDEX idx_country_name ON public.country
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_country_abbreviation | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_country_abbreviation;
CREATE INDEX idx_country_abbreviation ON public.country
	USING btree
	(
	  abbreviation
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_course_race | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_course_race;
CREATE INDEX idx_course_race ON public.course
	USING btree
	(
	  race_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_period_race | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_period_race;
CREATE INDEX idx_period_race ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_profile_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_profile_name;
CREATE INDEX idx_profile_name ON public.profile
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_user_city | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_user_city;
CREATE INDEX idx_user_city ON public.profile
	USING btree
	(
	  city_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_profile_cpf | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_profile_cpf;
CREATE INDEX idx_profile_cpf ON public.profile
	USING btree
	(
	  cpf
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_race_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_race_name;
CREATE INDEX idx_race_name ON public.race
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_race_date | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_race_date;
CREATE INDEX idx_race_date ON public.race
	USING btree
	(
	  date
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_race_city | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_race_city;
CREATE INDEX idx_race_city ON public.race
	USING btree
	(
	  city_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_registration_date | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_registration_date;
CREATE INDEX idx_registration_date ON public.registration
	USING btree
	(
	  date
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_registration_user | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_registration_user;
CREATE INDEX idx_registration_user ON public.registration
	USING btree
	(
	  submitter_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_state_name | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_state_name;
CREATE INDEX idx_state_name ON public.state
	USING btree
	(
	  name
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_state_abbreviation | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_state_abbreviation;
CREATE INDEX idx_state_abbreviation ON public.state
	USING btree
	(
	  abbreviation
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_state_country | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_state_country;
CREATE INDEX idx_state_country ON public.state
	USING btree
	(
	  country_id
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_user_email | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_user_email;
CREATE INDEX idx_user_email ON public.user_account
	USING btree
	(
	  email
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_user_deleted | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_user_deleted;
CREATE INDEX idx_user_deleted ON public.user_account
	USING btree
	(
	  deleted
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.seq_category | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_category;
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
-- DROP SEQUENCE IF EXISTS public.seq_course;
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
-- DROP SEQUENCE IF EXISTS public.seq_period;
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
-- DROP SEQUENCE IF EXISTS public.seq_race;
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
-- DROP SEQUENCE IF EXISTS public.seq_receipt;
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
-- DROP SEQUENCE IF EXISTS public.seq_registration;
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
-- DROP SEQUENCE IF EXISTS public.seq_user;
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
-- DROP INDEX IF EXISTS public.idx_registration_status;
CREATE INDEX idx_registration_status ON public.registration
	USING btree
	(
	  status ASC NULLS LAST
	);
-- ddl-end --

-- object: idx_period_ending | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_period_ending;
CREATE UNIQUE INDEX idx_period_ending ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST,
	  ending
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: idx_period_beginning | type: INDEX --
-- DROP INDEX IF EXISTS public.idx_period_beginning;
CREATE UNIQUE INDEX idx_period_beginning ON public.period
	USING btree
	(
	  race_id ASC NULLS LAST,
	  beginning ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.annual_fee | type: TABLE --
-- DROP TABLE IF EXISTS public.annual_fee;
CREATE TABLE public.annual_fee(
	year integer NOT NULL,
	fee numeric(5,2),
	CONSTRAINT pk_annual_fee PRIMARY KEY (year)

);
-- ddl-end --
ALTER TABLE public.annual_fee OWNER TO postgres;
-- ddl-end --

-- object: public.annual_fee_payment | type: TABLE --
-- DROP TABLE IF EXISTS public.annual_fee_payment;
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
-- DROP INDEX IF EXISTS public.idx_anual_fee_payment_registration;
CREATE INDEX idx_anual_fee_payment_registration ON public.annual_fee_payment
	USING btree
	(
	  registration_id ASC NULLS LAST
	)	WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: adminpack | type: EXTENSION --
-- DROP EXTENSION IF EXISTS pg_catalog.adminpack;
CREATE EXTENSION adminpack
      WITH SCHEMA pg_catalog
      VERSION '1.0';
-- ddl-end --
COMMENT ON EXTENSION pg_catalog.adminpack IS 'administrative functions for PostgreSQL';
-- ddl-end --

-- object: fk_city_state | type: CONSTRAINT --
-- ALTER TABLE public.city DROP CONSTRAINT IF EXISTS fk_city_state;
ALTER TABLE public.city ADD CONSTRAINT fk_city_state FOREIGN KEY (state_id)
REFERENCES public.state (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_course_race | type: CONSTRAINT --
-- ALTER TABLE public.course DROP CONSTRAINT IF EXISTS fk_course_race;
ALTER TABLE public.course ADD CONSTRAINT fk_course_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_health_user | type: CONSTRAINT --
-- ALTER TABLE public.health DROP CONSTRAINT IF EXISTS fk_health_user;
ALTER TABLE public.health ADD CONSTRAINT fk_health_user FOREIGN KEY (id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_period_race | type: CONSTRAINT --
-- ALTER TABLE public.period DROP CONSTRAINT IF EXISTS fk_period_race;
ALTER TABLE public.period ADD CONSTRAINT fk_period_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_profile_user | type: CONSTRAINT --
-- ALTER TABLE public.profile DROP CONSTRAINT IF EXISTS fk_profile_user;
ALTER TABLE public.profile ADD CONSTRAINT fk_profile_user FOREIGN KEY (id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_user_city | type: CONSTRAINT --
-- ALTER TABLE public.profile DROP CONSTRAINT IF EXISTS fk_user_city;
ALTER TABLE public.profile ADD CONSTRAINT fk_user_city FOREIGN KEY (city_id)
REFERENCES public.city (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_city | type: CONSTRAINT --
-- ALTER TABLE public.race DROP CONSTRAINT IF EXISTS fk_race_city;
ALTER TABLE public.race ADD CONSTRAINT fk_race_city FOREIGN KEY (city_id)
REFERENCES public.city (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_category_category | type: CONSTRAINT --
-- ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_category;
ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_category FOREIGN KEY (category_id)
REFERENCES public.category (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_category_race | type: CONSTRAINT --
-- ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_race;
ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_category_course | type: CONSTRAINT --
-- ALTER TABLE public.race_category DROP CONSTRAINT IF EXISTS fk_race_category_course;
ALTER TABLE public.race_category ADD CONSTRAINT fk_race_category_course FOREIGN KEY (course_id)
REFERENCES public.course (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_organizer_race | type: CONSTRAINT --
-- ALTER TABLE public.race_organizer DROP CONSTRAINT IF EXISTS fk_race_organizer_race;
ALTER TABLE public.race_organizer ADD CONSTRAINT fk_race_organizer_race FOREIGN KEY (race_id)
REFERENCES public.race (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_race_organizer | type: CONSTRAINT --
-- ALTER TABLE public.race_organizer DROP CONSTRAINT IF EXISTS fk_race_organizer;
ALTER TABLE public.race_organizer ADD CONSTRAINT fk_race_organizer FOREIGN KEY (organizer_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_registration_user | type: CONSTRAINT --
-- ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_user;
ALTER TABLE public.registration ADD CONSTRAINT fk_registration_user FOREIGN KEY (submitter_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_registration_race_category | type: CONSTRAINT --
-- ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_race_category;
ALTER TABLE public.registration ADD CONSTRAINT fk_registration_race_category FOREIGN KEY (category_id,course_id,race_id)
REFERENCES public.race_category (category_id,course_id,race_id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_resgistration_period | type: CONSTRAINT --
-- ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_resgistration_period;
ALTER TABLE public.registration ADD CONSTRAINT fk_resgistration_period FOREIGN KEY (period_id)
REFERENCES public.period (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_state_country | type: CONSTRAINT --
-- ALTER TABLE public.state DROP CONSTRAINT IF EXISTS fk_state_country;
ALTER TABLE public.state ADD CONSTRAINT fk_state_country FOREIGN KEY (country_id)
REFERENCES public.country (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_team_formation_user | type: CONSTRAINT --
-- ALTER TABLE public.team_formation DROP CONSTRAINT IF EXISTS fk_team_formation_user;
ALTER TABLE public.team_formation ADD CONSTRAINT fk_team_formation_user FOREIGN KEY (user_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_team_formation_registration | type: CONSTRAINT --
-- ALTER TABLE public.team_formation DROP CONSTRAINT IF EXISTS fk_team_formation_registration;
ALTER TABLE public.team_formation ADD CONSTRAINT fk_team_formation_registration FOREIGN KEY (registration_id)
REFERENCES public.registration (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_anual_fee_payment_annual_fee | type: CONSTRAINT --
-- ALTER TABLE public.annual_fee_payment DROP CONSTRAINT IF EXISTS fk_anual_fee_payment_annual_fee;
ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_annual_fee FOREIGN KEY (year)
REFERENCES public.annual_fee (year) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_anual_fee_payment_user | type: CONSTRAINT --
-- ALTER TABLE public.annual_fee_payment DROP CONSTRAINT IF EXISTS fk_anual_fee_payment_user;
ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_user FOREIGN KEY (user_id)
REFERENCES public.user_account (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: fk_anual_fee_payment_registration | type: CONSTRAINT --
-- ALTER TABLE public.annual_fee_payment DROP CONSTRAINT IF EXISTS fk_anual_fee_payment_registration;
ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_registration FOREIGN KEY (registration_id)
REFERENCES public.registration (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --


