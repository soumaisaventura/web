ALTER TABLE event
  RENAME COLUMN banner_hash TO _banner_hash;

-- select * from event;
-- select * from profile;

ALTER TABLE profile
  RENAME COLUMN picture_hash TO _picture_hash;

UPDATE event
SET _banner_hash =
md5(CASE WHEN banner IS NOT NULL
  THEN lo_get(banner)
    ELSE '' END);

UPDATE profile
SET _picture_hash =
md5(CASE WHEN picture IS NOT NULL
  THEN lo_get(picture)
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
      THEN lo_get(NEW.banner)
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
