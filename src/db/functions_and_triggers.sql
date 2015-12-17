DROP FUNCTION IF EXISTS event_status (integer);

CREATE OR REPLACE FUNCTION event_status (_event_id integer)
   RETURNS integer
AS
$func$
   # VARIABLE_CONFLICT use_variable
DECLARE
   l_race      race%ROWTYPE;
   status_id   status.id%TYPE;
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

DROP FUNCTION IF EXISTS race_status (integer, DATE);

CREATE OR REPLACE FUNCTION race_status (_race_id integer, _race_ending DATE)
   RETURNS integer
AS
$func$
   # VARIABLE_CONFLICT use_variable
DECLARE
   period      period%ROWTYPE;
   today       DATE;
   status_id   status.id%TYPE;
BEGIN
   today = CURRENT_DATE;

   IF _race_id IS NULL
   THEN
      RETURN NULL;
   END IF;

   SELECT min (p.beginning), max (p.ending)
     INTO period.beginning, period.ending
     FROM period p
    WHERE p.race_id = _race_id;

   IF period.beginning IS NULL
   THEN
      RETURN 1;
   END IF;

   IF today < period.beginning
   THEN
      status_id = 1;
   ELSEIF today >= period.beginning AND today <= period.ending
   THEN
      status_id = 2;
   ELSEIF today > period.ending AND today <= _race_ending
   THEN
      status_id = 3;
   ELSEIF today > _race_ending
   THEN
      status_id = 4;
   END IF;

   RETURN status_id;
END
$func$
   LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trg_period_after_all ()
   RETURNS trigger
AS
$func$
   /*
   # VARIABLE_CONFLICT use_variable
DECLARE
   event_id   event.id%TYPE;
   */
BEGIN
   IF NEW.race_id <> OLD.race_id
   THEN
      UPDATE race
         SET _status_id = race_status (OLD.race_id, OLD.ending)
       WHERE id = OLD.race_id;
   /*
         SELECT r.event_id
           INTO event_id
           FROM race r
          WHERE r.id = OLD.race_id;

         UPDATE event
            SET _beginning =
                   (SELECT min (r.beginning)
                      FROM race r
                     WHERE r.id = OLD.race_id),
                _ending =
                   (SELECT max (r.ending)
                      FROM race r
                     WHERE r.id = OLD.race_id)
          WHERE id = event_id;
         event_id = NULL;
   */



   END IF;

   IF NEW.race_id IS NOT NULL
   THEN
      UPDATE race
         SET _status_id = race_status (NEW.race_id, NEW.ending)
       WHERE id = NEW.race_id;
   /*
       SELECT r.event_id
         INTO event_id
         FROM race r
        WHERE r.id = NEW.race_id;

       UPDATE event
          SET _beginning =
                 (SELECT min (r.beginning)
                    FROM race r
                   WHERE r.id = NEW.race_id),
              _ending =
                 (SELECT max (r.ending)
                    FROM race r
                   WHERE r.id = NEW.race_id)
        WHERE id = event_id;
     */



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
EXECUTE PROCEDURE trg_period_after_all ();

CREATE OR REPLACE FUNCTION trg_race_after_all ()
   RETURNS trigger
AS
$func$
   # VARIABLE_CONFLICT use_variable
DECLARE
   l_race         race%ROWTYPE;
   status_id      event._status_id%TYPE;
   new_event_id   event.id%TYPE;
   old_event_id   event.id%TYPE;
BEGIN
   IF TG_OP IN ('INSERT', 'UPDATE')
   THEN
      UPDATE event
         SET _status_id = event_status (NEW.event_id),
             _beginning =
                (SELECT min (r.beginning)
                   FROM race r
                  WHERE r.id = NEW.id),
             _ending =
                (SELECT max (r.ending)
                   FROM race r
                  WHERE r.id = NEW.id)
       WHERE id = NEW.event_id;
   END IF;

   IF TG_OP IN ('DELETE', 'UPDATE')
   THEN
      UPDATE event
         SET _status_id = event_status (OLD.event_id),
             _beginning =
                (SELECT min (r.beginning)
                   FROM race r
                  WHERE r.id = OLD.id),
             _ending =
                (SELECT max (r.ending)
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
EXECUTE PROCEDURE trg_race_after_all ();

CREATE OR REPLACE FUNCTION trg_race_before_update ()
   RETURNS trigger
AS
$func$
   # VARIABLE_CONFLICT use_variable
BEGIN
   NEW._status_id = race_status (NEW.id, NEW.ending);
   RETURN NEW;
END;
$func$
   LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_race_before_update ON race;

CREATE TRIGGER trg_race_before_update
   BEFORE UPDATE
   ON race
   FOR EACH ROW
EXECUTE PROCEDURE trg_race_before_update ();
