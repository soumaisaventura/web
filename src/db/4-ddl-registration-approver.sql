delete from public.team_formation;

delete from public.annual_fee_payment;

delete from public.registration;

ALTER TABLE public.registration ADD COLUMN approver_id integer;

ALTER TABLE public.registration ADD COLUMN status_date timestamp NOT NULL;

ALTER TABLE public.registration ADD CONSTRAINT fk_registration_approver FOREIGN KEY (approver_id)
REFERENCES public.user_account (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;

