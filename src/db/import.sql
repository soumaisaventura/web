SELECT md5(oidsend(banner))
FROM event
WHERE id = 21;


ALTER TABLE event
  RENAME COLUMN _banner_hash TO banner_hash;


ALTER TABLE profile
  RENAME COLUMN _picture_hash TO picture_hash;


ALTER TABLE event
  RENAME COLUMN banner_hash TO _banner_hash;

ALTER TABLE profile
  RENAME COLUMN picture_hash TO _picture_hash;

UPDATE event
SET _banner_hash =
md5(CASE WHEN banner IS NOT NULL
  THEN oidsend(banner)
    ELSE '' END);

UPDATE profile
SET _picture_hash =
md5(CASE WHEN picture IS NOT NULL
  THEN oidsend(picture)
    ELSE '' END);

ALTER TABLE event
  ALTER COLUMN _banner_hash SET NOT NULL;

ALTER TABLE profile
  ALTER COLUMN _picture_hash SET NOT NULL;

CREATE OR REPLACE FUNCTION trg_event_before_insert_update()
  RETURNS TRIGGER
AS
$func$
# VARIABLE_CONFLICT use_variable
BEGIN
  IF TG_OP IN ('INSERT', 'UPDATE')
  THEN
    NEW._banner_hash = md5(CASE WHEN NEW.banner IS NOT NULL
      THEN oidsend(NEW.banner)
                           ELSE '' END);
  END IF;

  RETURN NEW;
END;
$func$
LANGUAGE plpgsql;

CREATE TRIGGER trg_event_before_insert_update
BEFORE INSERT OR UPDATE
ON event
FOR EACH ROW
EXECUTE PROCEDURE trg_event_before_insert_update();

ALTER TABLE public.profile
  ADD COLUMN national_id VARCHAR(20);
ALTER TABLE public.profile
  ADD COLUMN sicard_number VARCHAR(20);
