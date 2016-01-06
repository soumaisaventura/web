CREATE OR REPLACE FUNCTION migrar ()
   RETURNS void
AS
$func$
DECLARE
   reg_attrs    pg_attribute%ROWTYPE;
   t_reg        tabelao%ROWTYPE;
   q_id         integer;
   q_old_name   text;
   q_attr       text;
   q_titulo     text;
   r_id         integer;
   r_name       text;
   aux_count    integer;
BEGIN
   -- preparando a base
   DROP VIEW IF EXISTS respostas;

   DROP TABLE IF EXISTS questao;

   CREATE TABLE questao
   (
      id          integer,
      titulo      text,
      descricao   text
   );

   DROP TABLE IF EXISTS resposta;

   CREATE TABLE resposta
   (
      id           integer,
      descricao    text,
      questao_id   integer
   );

   DROP TABLE IF EXISTS resposta_usuario;

   CREATE TABLE resposta_usuario
   (
      questao_id    integer,
      resposta_id   integer,
      usuario_id    text
   );

   FOR t_reg IN SELECT * FROM tabelao
   LOOP
      -- navegando as colunas q_*, colunas de questão

      FOR reg_attrs
         IN SELECT *
              FROM pg_attribute
             WHERE attrelid = 'tabelao'::regclass AND attname LIKE 'q_%'
      LOOP
         q_old_name = reg_attrs.attname;

         -- cadastra a questao se nao existir

         q_id = replace (q_old_name, 'q_', '')::integer;

         SELECT count (*)
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

         FOREACH r_name IN ARRAY string_to_array (q_attr, ';')
         LOOP
            -- cadastra a resposta na tabela de respostas se nao existir

            SELECT r.id
              INTO r_id
              FROM resposta r
             WHERE r.descricao = r_name AND r.questao_id = q_id;

            IF r_id IS NULL
            THEN
               SELECT max (r.id) + 1
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

   CREATE VIEW respostas
   AS
        SELECT ru.usuario_id,
               ru.questao_id,
               q.titulo AS questao,
               ru.resposta_id,
               r.descricao AS resposta
          FROM resposta_usuario ru, questao q, resposta r
         WHERE     ru.questao_id = q.id
               AND ru.questao_id = r.questao_id
               AND ru.resposta_id = r.id
      ORDER BY ru.usuario_id, ru.questao_id, ru.resposta_id;
-- RAISE EXCEPTION '%', q_attr;
END
$func$
   LANGUAGE plpgsql;