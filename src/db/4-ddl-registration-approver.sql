delete from public.team_formation;

delete from public.annual_fee_payment;

delete from public.registration;

ALTER TABLE public.registration ADD COLUMN approver_id integer;

ALTER TABLE public.registration ADD COLUMN status_date timestamp NOT NULL;

ALTER TABLE public.registration ADD CONSTRAINT fk_registration_approver FOREIGN KEY (approver_id)
REFERENCES public.user_account (id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE public.registration DROP CONSTRAINT IF EXISTS fk_registration_user CASCADE;

DROP INDEX IF EXISTS public.idx_registration_user CASCADE;


CREATE INDEX idx_registration_submitter ON public.registration
	USING btree
	(
	  submitter_id
	)	WITH (FILLFACTOR = 90);


CREATE INDEX idx_registration_approver ON public.registration
	USING btree
	(
	  approver_id ASC NULLS LAST
	);

ALTER TABLE public.registration ADD CONSTRAINT fk_registration_submitter FOREIGN KEY (submitter_id)
REFERENCES public.user_account (id) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;

