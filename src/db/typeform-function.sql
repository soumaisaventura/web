CREATE OR REPLACE FUNCTION migrar()
  RETURNS VOID
AS
$func$
DECLARE
  reg_attrs  pg_attribute%ROWTYPE;
  t_reg      tabelao%ROWTYPE;
  q_id       INTEGER;
  q_old_name TEXT;
  q_attr     TEXT;
  q_titulo   TEXT;
  r_id       INTEGER;
  r_name     TEXT;
  aux_count  INTEGER;
BEGIN
  -- preparando a base
  DROP VIEW IF EXISTS respostas_count;

  DROP VIEW IF EXISTS respostas;

  DROP TABLE IF EXISTS questao;

  DROP TABLE IF EXISTS lista_negra;

  CREATE TABLE questao
  (
    id        INTEGER,
    titulo    TEXT,
    descricao TEXT
  );

  DROP TABLE IF EXISTS resposta;

  CREATE TABLE resposta
  (
    id         INTEGER,
    descricao  TEXT,
    questao_id INTEGER
  );

  DROP TABLE IF EXISTS resposta_usuario;

  CREATE TABLE resposta_usuario
  (
    questao_id      INTEGER,
    resposta_id     INTEGER,
    new_resposta_id INTEGER,
    usuario_id      TEXT
  );

  FOR t_reg IN SELECT *
               FROM tabelao
  LOOP
    -- navegando as colunas q_*, colunas de questão

    FOR reg_attrs
    IN SELECT *
       FROM pg_attribute
       WHERE attrelid = 'tabelao' :: REGCLASS AND attname LIKE 'q_%'
    LOOP
      q_old_name = reg_attrs.attname;

      -- cadastra a questao se nao existir

      q_id = replace(q_old_name, 'q_', '') :: INTEGER;

      SELECT count(*)
      INTO aux_count
      FROM questao q
      WHERE q.id = q_id;

      IF aux_count = 0
      THEN
        CASE
          WHEN q_id = 1
          THEN
            q_titulo = 'Informação sobre Demoiselle';
          WHEN q_id = 2
          THEN
            q_titulo = 'Informações sobre outros projetos';
          WHEN q_id = 3
          THEN
            q_titulo = 'Site';
          WHEN q_id = 4
          THEN
            q_titulo = 'Documentação';
          WHEN q_id = 5
          THEN
            q_titulo = 'Tempo de resposta';
          WHEN q_id = 6
          THEN
            q_titulo = 'Mais adequado';
          WHEN q_id = 7
          THEN
            q_titulo = 'Suas respostas';
          WHEN q_id = 8
          THEN
            q_titulo = 'Bug';
          WHEN q_id = 9
          THEN
            q_titulo = 'Contribuição';
          WHEN q_id = 10
          THEN
            q_titulo = 'Funcionalidades';
          WHEN q_id = 11
          THEN
            q_titulo = 'Componentes';
          WHEN q_id = 12
          THEN
            q_titulo = 'Aprendizado';
          WHEN q_id = 13
          THEN
            q_titulo = 'Modelo';
          WHEN q_id = 14
          THEN
            q_titulo = 'JSF';
          WHEN q_id = 15
          THEN
            q_titulo = 'HTML+REST';
          WHEN q_id = 16
          THEN
            q_titulo = 'Atuação';
          WHEN q_id = 17
          THEN
            q_titulo = 'Uso';
          WHEN q_id = 18
          THEN
            q_titulo = 'Demoiselle';
          WHEN q_id = 19
          THEN
            q_titulo = 'Versão 2.5';
          WHEN q_id = 20
          THEN
            q_titulo = 'Pesquisa';
        END CASE;

        INSERT INTO questao (id, titulo)
        VALUES (q_id, q_titulo);
      END IF;

      -- navegando pelas respostas do usuário à questão corrente

      EXECUTE
      'select t.'
      || q_old_name
      || ' from tabelao t where t.usuario = '''
      || t_reg.usuario
      || ''';'
      INTO q_attr;

      FOREACH r_name IN ARRAY string_to_array(q_attr, ';')
      LOOP
        -- cadastra a resposta na tabela de respostas se nao existir

        SELECT r.id
        INTO r_id
        FROM resposta r
        WHERE r.descricao = r_name AND r.questao_id = q_id;

        IF r_id IS NULL
        THEN
          SELECT max(r.id) + 1
          INTO r_id
          FROM resposta r
          WHERE r.questao_id = q_id;

          IF r_id IS NULL
          THEN
            r_id = 1;
          END IF;

          INSERT INTO resposta (id, descricao, questao_id)
          VALUES (r_id, r_name, q_id);
        END IF;

        INSERT INTO resposta_usuario (questao_id, resposta_id, usuario_id)
        VALUES (q_id, r_id, t_reg.usuario);

      END LOOP;
    END LOOP;
  END LOOP;


  UPDATE resposta_usuario
  SET new_resposta_id = resposta_id;

  -- Questão 1

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (0, 'Colegas', 1);

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (-1, 'Outros', 1);

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (-2, 'Código-fonte', 1);

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 15;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 17;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 10;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 21;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 18;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 20;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 8;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 11;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 1 AND resposta_id = 29;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 19;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 34;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 27;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 28;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 1 AND resposta_id = 25;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 33;

  UPDATE resposta_usuario
  SET new_resposta_id = 44
  WHERE questao_id = 1 AND resposta_id = 13;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 1 AND resposta_id = 32;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 9;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 5;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 24;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 6;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 1 AND resposta_id = 14;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 26;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 22;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 1 AND resposta_id = 31;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 16;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 1 AND resposta_id = 30;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 1 AND resposta_id = 12;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 1 AND resposta_id = 23;

  -- Questão 2

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (0, 'Colegas', 2);

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (-1, 'Outros', 2);

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 2 AND resposta_id = 6;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 2 AND resposta_id = 18;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 2 AND resposta_id = 17;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 2 AND resposta_id = 15;

  UPDATE resposta_usuario
  SET new_resposta_id = 3
  WHERE questao_id = 2 AND resposta_id = 2;

  UPDATE resposta_usuario
  SET new_resposta_id = 3
  WHERE questao_id = 2 AND resposta_id = 14;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 2 AND resposta_id = 7;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 2 AND resposta_id = 16;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 2 AND resposta_id = 12;

  UPDATE resposta_usuario
  SET new_resposta_id = 3
  WHERE questao_id = 2 AND resposta_id = 9;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 2 AND resposta_id = 11;

  UPDATE resposta_usuario
  SET new_resposta_id = 3
  WHERE questao_id = 2 AND resposta_id = 13;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 2 AND resposta_id = 10;

  -- Questão 8

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (0, 'Não achei bugs', 8);

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (-1, 'Informo ao colega', 8);

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (-2, 'Outros', 8);

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 14;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 10;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 8 AND resposta_id = 12;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 8 AND resposta_id = 18;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 41;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 6;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 23;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 8 AND resposta_id = 32;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 8 AND resposta_id = 48;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 15;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 50;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 8 AND resposta_id = 44;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 47;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 31;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 36;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 34;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 8 AND resposta_id = 39;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 49;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 8 AND resposta_id = 37;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 45;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 43;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 38;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 9;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 5;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 7;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 27;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 13;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 25;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 33;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 8 AND resposta_id = 26;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 20;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 8 AND resposta_id = 46;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 24;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 17;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 8 AND resposta_id = 21;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 8 AND resposta_id = 35;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 19;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 40;

  UPDATE resposta_usuario
  SET new_resposta_id = -2
  WHERE questao_id = 8 AND resposta_id = 28;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 30;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 11;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 8 AND resposta_id = 42;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 22;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 8 AND resposta_id = 16;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 8 AND resposta_id = 29;

  -- Questão 10

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (0, 'Anti-Demoiselle', 10);

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (-1, 'Outros', 10);

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 10 AND resposta_id = 11;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 16;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 10 AND resposta_id = 12;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 26;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 18;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 17;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 29;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 10 AND resposta_id = 7;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 13;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 4;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 23;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 27;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 25;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 8;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 19;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 10 AND resposta_id = 21;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 30;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 28;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 10;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 10 AND resposta_id = 14;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 9;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 10 AND resposta_id = 15;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 24;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 20;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 10 AND resposta_id = 22;

  -- Questão 12

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (0, 'Colega', 12);

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (-1, 'Outros', 12);

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 12 AND resposta_id = 14;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 12 AND resposta_id = 11;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 12 AND resposta_id = 15;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 12 AND resposta_id = 5;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 12 AND resposta_id = 10;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 12 AND resposta_id = 12;

  UPDATE resposta_usuario
  SET new_resposta_id = -1
  WHERE questao_id = 12 AND resposta_id = 13;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 12 AND resposta_id = 7;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 12 AND resposta_id = 9;

  -- Questão 16

  INSERT INTO resposta (id, descricao, questao_id)
  VALUES (0, 'Outros', 16);

  UPDATE resposta_usuario
  SET new_resposta_id = 7
  WHERE questao_id = 16 AND resposta_id = 10;

  UPDATE resposta_usuario
  SET new_resposta_id = 7
  WHERE questao_id = 16 AND resposta_id = 25;

  UPDATE resposta_usuario
  SET new_resposta_id = 2
  WHERE questao_id = 16 AND resposta_id = 22;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 16 AND resposta_id = 11;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 16 AND resposta_id = 16;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 16 AND resposta_id = 21;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 16 AND resposta_id = 28;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 16 AND resposta_id = 9;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 16 AND resposta_id = 23;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 16 AND resposta_id = 20;

  UPDATE resposta_usuario
  SET new_resposta_id = 7
  WHERE questao_id = 16 AND resposta_id = 17;

  UPDATE resposta_usuario
  SET new_resposta_id = 6
  WHERE questao_id = 16 AND resposta_id = 15;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 16 AND resposta_id = 12;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 16 AND resposta_id = 4;

  UPDATE resposta_usuario
  SET new_resposta_id = 6
  WHERE questao_id = 16 AND resposta_id = 18;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 16 AND resposta_id = 13;

  UPDATE resposta_usuario
  SET new_resposta_id = 7
  WHERE questao_id = 16 AND resposta_id = 8;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 16 AND resposta_id = 19;

  UPDATE resposta_usuario
  SET new_resposta_id = 3
  WHERE questao_id = 16 AND resposta_id = 5;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 16 AND resposta_id = 26;

  UPDATE resposta_usuario
  SET new_resposta_id = 0
  WHERE questao_id = 16 AND resposta_id = 27;

  UPDATE resposta_usuario
  SET new_resposta_id = 7
  WHERE questao_id = 16 AND resposta_id = 14;

  UPDATE resposta_usuario
  SET new_resposta_id = 7
  WHERE questao_id = 16 AND resposta_id = 24;

  -- Questão 20

  UPDATE resposta_usuario
  SET new_resposta_id = 5
  WHERE questao_id = 20 AND resposta_id = 13;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 20 AND resposta_id = 3;

  UPDATE resposta_usuario
  SET new_resposta_id = 4
  WHERE questao_id = 20 AND resposta_id = 15;

  UPDATE resposta_usuario
  SET new_resposta_id = 5
  WHERE questao_id = 20 AND resposta_id = 12;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 20 AND resposta_id = 2;

  UPDATE resposta_usuario
  SET new_resposta_id = 8
  WHERE questao_id = 20 AND resposta_id = 6;

  UPDATE resposta_usuario
  SET new_resposta_id = 8
  WHERE questao_id = 20 AND resposta_id = 17;

  UPDATE resposta_usuario
  SET new_resposta_id = 5
  WHERE questao_id = 20 AND resposta_id = 16;

  UPDATE resposta_usuario
  SET new_resposta_id = 8
  WHERE questao_id = 20 AND resposta_id = 10;

  UPDATE resposta_usuario
  SET new_resposta_id = 4
  WHERE questao_id = 20 AND resposta_id = 14;

  UPDATE resposta_usuario
  SET new_resposta_id = 1
  WHERE questao_id = 20 AND resposta_id = 7;

  UPDATE resposta_usuario
  SET new_resposta_id = 5
  WHERE questao_id = 20 AND resposta_id = 11;

  CREATE TABLE lista_negra
  AS
    SELECT DISTINCT _ru2.usuario_id
    FROM resposta_usuario _ru2
    WHERE _ru2.usuario_id IN
          (SELECT _ru.usuario_id
           FROM resposta_usuario _ru
           WHERE _ru.questao_id = 1 AND _ru.new_resposta_id = 1)
          AND _ru2.questao_id = 3
          AND _ru2.new_resposta_id = 2
    UNION
    SELECT _ru.usuario_id
    FROM resposta_usuario _ru
    WHERE _ru.questao_id = 18 AND _ru.new_resposta_id = 6
    UNION
    SELECT _ru.usuario_id
    FROM resposta_usuario _ru
    WHERE _ru.questao_id = 17 AND _ru.new_resposta_id = 5
    UNION
    SELECT _ru2.usuario_id
    FROM resposta_usuario _ru2
    WHERE _ru2.usuario_id IN
          (SELECT _ru.usuario_id
           FROM resposta_usuario _ru
           WHERE _ru.questao_id = 4 AND _ru.new_resposta_id = 2)
          AND _ru2.questao_id = 4
          AND _ru2.new_resposta_id <> 2;

  CREATE VIEW respostas
  AS
    SELECT
      ru.usuario_id,
      ru.questao_id,
      q.titulo    AS questao,
      ru.resposta_id,
      ru.new_resposta_id,
      r.descricao AS resposta,
      (SELECT count(*)
       FROM lista_negra _ln
       WHERE _ln.usuario_id = ru.usuario_id) > 0
                  AS lista_negra
    FROM resposta_usuario ru, questao q, resposta r
    WHERE ru.questao_id = q.id
          AND ru.questao_id = r.questao_id
          AND ru.new_resposta_id = r.id
    ORDER BY ru.usuario_id, ru.questao_id, ru.new_resposta_id;

  CREATE VIEW respostas_count
  AS
    SELECT
      questao_id,
      questao,
      new_resposta_id,
      resposta,
      sum(CASE WHEN lista_negra = TRUE
        THEN 0
          ELSE 1 END) AS qtd,
      count(*)        AS qtd_total,
      count(*) - sum(CASE WHEN lista_negra = TRUE
        THEN 0
                     ELSE 1 END)
                      AS diff
    FROM respostas
    GROUP BY questao_id,
      questao,
      new_resposta_id,
      resposta
    ORDER BY questao_id, qtd DESC;
  -- RAISE EXCEPTION '%', q_attr;
END
$func$
LANGUAGE plpgsql;