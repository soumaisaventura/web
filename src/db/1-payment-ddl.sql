ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS registration_pkey CASCADE;

ALTER TABLE public.registration ADD CONSTRAINT pk_registration PRIMARY KEY (id);

ALTER TABLE public.registration ADD COLUMN payment_account character varying(255);

ALTER TABLE public.registration ADD COLUMN payment_token character varying(64);

ALTER TABLE public.registration ADD COLUMN payment_transaction character varying(64);

ALTER TABLE public.registration ADD CONSTRAINT uk_registration_payment_transaction UNIQUE (payment_transaction);

update public.registration set payment_account = 'arnaldo_maciel@hotmail.com', payment_token = 'F5320349987D4692BD5E599695E7CF5D';
