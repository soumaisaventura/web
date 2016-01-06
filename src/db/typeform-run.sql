SELECT migrar ();

  SELECT questao_id,
         questao,
         resposta_id,
         resposta,
         count (*) AS qtd
    FROM respostas
GROUP BY questao_id,
         questao,
         resposta_id,
         resposta
ORDER BY questao_id, qtd DESC;