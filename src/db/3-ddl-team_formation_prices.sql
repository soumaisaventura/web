delete from public.team_formation;

delete from public.annual_fee_payment;

delete from public.registration;

ALTER TABLE public.team_formation ADD COLUMN race_price numeric(7,2) NOT NULL;

ALTER TABLE public.team_formation ADD COLUMN annual_fee numeric(5,2) NOT NULL;
