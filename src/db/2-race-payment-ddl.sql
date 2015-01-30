ALTER TABLE public.registration DROP COLUMN IF EXISTS payment_token CASCADE;

ALTER TABLE public.registration DROP COLUMN IF EXISTS payment_account CASCADE;

ALTER TABLE public.race ADD COLUMN payment_account character varying(255);

ALTER TABLE public.race ADD COLUMN payment_token character varying(64);

ALTER TABLE public.team_formation ADD CONSTRAINT fk_team_formation_registration FOREIGN KEY (registration_id)
REFERENCES public.registration (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE public.annual_fee_payment ADD CONSTRAINT fk_anual_fee_payment_registration FOREIGN KEY (registration_id)
REFERENCES public.registration (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;

update public.race set payment_account = 'arnaldo_maciel@hotmail.com', payment_token = 'F5320349987D4692BD5E599695E7CF5D' where name like '%errengue%';

commit;

