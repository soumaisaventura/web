DROP FUNCTION IF EXISTS event_status ( INTEGER );

CREATE OR REPLACE FUNCTION event_status(_event_id INTEGER)
  RETURNS INTEGER
AS
$func$
# VARIABLE_CONFLICT use_variable
DECLARE
  l_race    race%ROWTYPE;
  status_id status.id%TYPE;
BEGIN
  FOR l_race IN SELECT r.*
                FROM race r
                WHERE r.event_id = _event_id
  LOOP
    IF status_id IS NULL OR l_race._status_id < status_id
    THEN
      status_id = l_race._status_id;
    END IF;
  END LOOP;

  IF status_id IS NULL
  THEN
    status_id = 1;
  END IF;

  RETURN status_id;
END
$func$
LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS race_status ( INTEGER );

CREATE OR REPLACE FUNCTION race_status(_race_id INTEGER)
  RETURNS INTEGER
AS
$func$
# VARIABLE_CONFLICT use_variable
DECLARE
  period            period%ROWTYPE;
  today             DATE;
  race_beginning    race.beginning%TYPE;
  race_ending       race.ending%TYPE;
  status_id         status.id%TYPE;
  current_status_id status.id%TYPE;
BEGIN
  today = CURRENT_DATE;

  IF _race_id IS NULL
  THEN
    RETURN NULL;
  END IF;

  SELECT
    min(p.beginning),
    max(p.ending)
  INTO period.beginning, period.ending
  FROM period p
  WHERE p.race_id = _race_id;

  SELECT
    r._status_id,
    r.beginning,
    r.ending
  INTO current_status_id, race_beginning, race_ending
  FROM race r
  WHERE r.id = _race_id;

  IF period.beginning IS NULL
  THEN
    RETURN 1;
  END IF;

  -- Antes do período de inscrições
  IF today < period.beginning
  THEN
    status_id = 1;

    -- Dentro do período de inscrições
  ELSEIF today >= period.beginning AND today <= period.ending
    THEN
      IF current_status_id = 3
      THEN
        status_id = 3;
      ELSE
        status_id = 2;
      END IF;

      -- Após o período de inscrições, mas a prova não aconteceu ainda
  ELSEIF today > period.ending AND today <= race_beginning
    THEN
      status_id = 4;

      -- A prova já aconteceu
  ELSEIF today > race_ending
    THEN
      status_id = 5;
  END IF;

  RETURN status_id;
END
$func$
LANGUAGE plpgsql;


SELECT r.*
FROM period p, race r, status s
WHERE r._status_id = s.id
      AND p.race_id = r.id                              --AND s.name <> 'end'
      AND now() :: DATE BETWEEN p.beginning AND p.ending;

DROP FUNCTION IF EXISTS update_statuses ();

CREATE OR REPLACE FUNCTION update_statuses()
  RETURNS VOID
AS
$func$
# VARIABLE_CONFLICT use_variable
DECLARE
  l_race    race%ROWTYPE;
  status_id status.id%TYPE;
BEGIN
  FOR l_race IN SELECT r.*
                FROM race r
  LOOP
    status_id = race_status(l_race.id);

    IF l_race._status_id <> status_id
    THEN
      UPDATE race
      SET _status_id = status_id
      WHERE id = l_race.id;
    END IF;
  END LOOP;
END
$func$
LANGUAGE plpgsql;

SELECT update_statuses();

CREATE OR REPLACE FUNCTION trg_period_after_all()
  RETURNS TRIGGER
AS
$func$
BEGIN
  IF TG_OP IN ('INSERT', 'UPDATE')
  THEN
    UPDATE race
    SET _status_id = race_status(NEW.race_id)
    WHERE id = NEW.race_id;
  END IF;

  IF TG_OP IN ('DELETE', 'UPDATE')
  THEN
    UPDATE race
    SET _status_id = race_status(OLD.race_id)
    WHERE id = OLD.race_id;
  END IF;

  RETURN NULL;
END;
$func$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_period_after_all ON period;

CREATE TRIGGER trg_period_after_all
AFTER INSERT OR UPDATE OR DELETE
ON period
FOR EACH ROW
EXECUTE PROCEDURE trg_period_after_all();

CREATE OR REPLACE FUNCTION trg_race_after_all()
  RETURNS TRIGGER
AS
$func$
# VARIABLE_CONFLICT use_variable
DECLARE
  l_race       race%ROWTYPE;
  status_id    event._status_id%TYPE;
  new_event_id event.id%TYPE;
  old_event_id event.id%TYPE;
BEGIN
  IF TG_OP IN ('INSERT', 'UPDATE')
  THEN
    UPDATE event
    SET _status_id = event_status(NEW.event_id),
      _beginning   =
      (SELECT min(r.beginning)
       FROM race r
       WHERE r.id = NEW.id),
      _ending      =
      (SELECT max(r.ending)
       FROM race r
       WHERE r.id = NEW.id)
    WHERE id = NEW.event_id;
  END IF;

  IF TG_OP IN ('DELETE', 'UPDATE')
  THEN
    UPDATE event
    SET _status_id = event_status(OLD.event_id),
      _beginning   =
      (SELECT min(r.beginning)
       FROM race r
       WHERE r.id = OLD.id),
      _ending      =
      (SELECT max(r.ending)
       FROM race r
       WHERE r.id = OLD.id)
    WHERE id = OLD.event_id;
  END IF;

  RETURN NULL;
END;
$func$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_race_after_all ON race;

CREATE TRIGGER trg_race_after_all
AFTER INSERT OR UPDATE OR DELETE
ON race
FOR EACH ROW
EXECUTE PROCEDURE trg_race_after_all();

DROP FUNCTION IF EXISTS trg_race_before_update();

CREATE OR REPLACE FUNCTION trg_race_before_update()
  RETURNS TRIGGER
AS
$func$
# VARIABLE_CONFLICT use_variable
BEGIN
  --NEW._status_id = race_status(NEW.id, NEW.ending);
  RETURN NEW;
END;
$func$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_race_before_update ON race;

CREATE TRIGGER trg_race_before_update
BEFORE UPDATE
ON race
FOR EACH ROW
EXECUTE PROCEDURE trg_race_before_update();

---------------


CREATE OR REPLACE FUNCTION trg_event_organizer_after_all()
  RETURNS TRIGGER
AS
$func$
# VARIABLE_CONFLICT use_variable
BEGIN
  IF TG_OP IN ('INSERT', 'UPDATE')
  THEN
    UPDATE user_account
    SET _organizer =
    (SELECT count(*) > 0
     FROM event_organizer eo
     WHERE eo.organizer_id = NEW.organizer_id)
    WHERE id = NEW.organizer_id;
  END IF;

  IF TG_OP IN ('DELETE', 'UPDATE')
  THEN
    UPDATE user_account
    SET _organizer =
    (SELECT count(*) > 0
     FROM event_organizer eo
     WHERE eo.organizer_id = OLD.organizer_id)
    WHERE id = OLD.organizer_id;
  END IF;

  RETURN NULL;
END;
$func$
LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_event_organizer_after_all ON event_organizer;

CREATE TRIGGER trg_event_organizer_after_all
AFTER INSERT OR UPDATE OR DELETE
ON event_organizer
FOR EACH ROW
EXECUTE PROCEDURE trg_event_organizer_after_all();

DROP VIEW IF EXISTS registration_status_by_day;

CREATE OR REPLACE VIEW registration_status_by_day
AS
  SELECT
    da.event_id :: INTEGER,
    da.date :: DATE,
    sum(CASE WHEN re.status = 'PENDENT'
      THEN 1
        ELSE 0 END) :: INTEGER
      AS pendent,
    sum(CASE WHEN re.status = 'CONFIRMED'
      THEN 1
        ELSE 0 END) :: INTEGER
      AS confirmed,
    sum(CASE WHEN re.status = 'CANCELLED'
      THEN 1
        ELSE 0 END) :: INTEGER
      AS cancelled
  FROM (SELECT
          ep.event_id,
          ra.id AS race_id,
          generate_series(ep.beginning, ep.ending, '1 day') :: DATE
                AS date
        FROM (SELECT
                ra.event_id,
                min(pe.beginning) AS beginning,
                GREATEST(max(pe.ending), max(reg.status_date))
                                  AS ending
              FROM period pe, race ra, registration reg
              WHERE pe.race_id = ra.id AND ra.id = reg.race_id
              GROUP BY ra.event_id) ep,
          race ra
        WHERE ep.event_id = ra.event_id) da
    LEFT JOIN registration re
      ON (da.race_id = re.race_id AND da.date = re.status_date :: DATE)
  GROUP BY da.event_id, da.date;

-- NÃO USAR ESTRA TRIGGER. ESTÁ EM FASE EXPERIMENTAL E TALVEZ NÃO SEJA NECESSÁRIA

CREATE OR REPLACE FUNCTION trg_user_account_before_update()
  RETURNS TRIGGER
AS
$func$
# VARIABLE_CONFLICT use_variable
DECLARE
  activation_token     user_account.activation_token%TYPE;
  password_reset_token user_account.password_reset_token%TYPE;
BEGIN
  SELECT
    ua.activation_token,
    ua.password_reset_token
  INTO activation_token, password_reset_token
  FROM user_account ua
  WHERE ua.email = NEW.email;

  IF NEW.activation_token IS NOT NULL AND activation_token IS NOT NULL
  THEN
    NEW.activation_token = activation_token;
  END IF;

  IF NEW.password_reset_token IS NOT NULL AND password_reset_token IS NOT NULL
  THEN
    NEW.password_reset_token = password_reset_token;
  END IF;

  RETURN NEW;
END;
$func$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_user_account_before_update ON user_account;

CREATE TRIGGER trg_user_account_before_update
BEFORE UPDATE
ON user_account
FOR EACH ROW
EXECUTE PROCEDURE trg_user_account_before_update();

UPDATE race
SET _status_id = NULL;
