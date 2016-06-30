SELECT migrar();


SELECT DISTINCT
  questao_id,
  questao
FROM respostas_count
ORDER BY questao_id ASC;

--SELECT _ru2.usuario_id, _ru2.new_resposta_id AS qtd


SELECT count(DISTINCT _ru.usuario_id)
FROM resposta_usuario _ru
WHERE _ru.questao_id = 16 AND _ru.new_resposta_id IN (6, 2);


SELECT count(DISTINCT _ru2.usuario_id)
FROM resposta_usuario _ru2
WHERE _ru2.usuario_id IN
      (SELECT _ru.usuario_id
       FROM resposta_usuario _ru
       WHERE _ru.questao_id = 16 AND _ru.new_resposta_id = 1)
      AND _ru2.questao_id = 17
      AND _ru2.new_resposta_id = 1
      AND NOT EXISTS
(SELECT *
 FROM lista_negra _ln
 WHERE _ln.usuario_id = _ru2.usuario_id);


SELECT DISTINCT _ru.usuario_id
FROM resposta_usuario _ru
WHERE _ru.questao_id = 17
      AND _ru.new_resposta_id = 2
      AND NOT EXISTS
(SELECT *
 FROM lista_negra _ln
 WHERE _ln.usuario_id = _ru.usuario_id);


SELECT _ru2.usuario_id
FROM resposta_usuario _ru2
WHERE _ru2.usuario_id IN
      (SELECT _ru.usuario_id
       FROM resposta_usuario _ru
       WHERE _ru.questao_id = 17 AND _ru.new_resposta_id = 2)
      AND _ru2.questao_id = 17
      AND _ru2.new_resposta_id = 1
INTERSECT
SELECT usuario_id
FROM lista_negra;


SELECT _ru.usuario_id
FROM resposta_usuario _ru
WHERE _ru.questao_id = 17 AND _ru.new_resposta_id = 2;


SELECT count(*)
FROM lista_negra;


SELECT *
FROM respostas_count;


SELECT count(DISTINCT usuario_id)
FROM resposta_usuario
WHERE questao_id = 16 AND new_resposta_id IN (0);

SELECT count(DISTINCT usuario_id)
FROM resposta_usuario;


SELECT count(2)
FROM (SELECT DISTINCT usuario_id
      FROM resposta_usuario
      WHERE questao_id = 17 AND new_resposta_id IN (1)
INTERSECT
SELECT DISTINCT usuario_id
FROM resposta_usuario
WHERE questao_id = 16 AND new_resposta_id IN (1, 3)) x;


SELECT usuario_id
FROM resposta_usuario
WHERE questao_id = 17 AND new_resposta_id = 2;

SELECT
  min(data),
  max(data)
FROM tabelao;