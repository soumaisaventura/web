ALTER TABLE public.sport RENAME COLUMN acronym TO alias;

ALTER TABLE public.modality RENAME COLUMN acronym TO alias;

ALTER TABLE public.championship RENAME COLUMN slug TO alias;

ALTER TABLE public.race RENAME COLUMN slug TO alias;

ALTER TABLE public.event RENAME COLUMN slug TO alias;

ALTER TABLE public.kit RENAME COLUMN slug TO alias;

ALTER TABLE public.user_registration RENAME COLUMN race_price TO amount;

ALTER TABLE public.registration RENAME COLUMN payment_code TO payment_checkout_code;

ALTER TABLE public.registration RENAME COLUMN payment_transaction TO payment_transaction_code;

UPDATE period
SET beginning = '2016-04-01'
WHERE id = 110;

COMMIT;