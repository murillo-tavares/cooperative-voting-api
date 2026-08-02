# Postman

- [cooperative-voting-api.postman_collection.json](cooperative-voting-api.postman_collection.json)
- [cooperative-voting-api.local.postman_environment.json](cooperative-voting-api.local.postman_environment.json) — `baseUrl` apontando pra API local (`localhost:8080`)
- [cooperative-voting-api.render.postman_environment.json](cooperative-voting-api.render.postman_environment.json) — `baseUrl` apontando pra API hospedada no Render

Troca de ambiente é só selecionar o environment correspondente no Postman; o resto da collection não muda.

As requisições de criação (criar pauta, abrir sessão, registrar voto) já salvam o id retornado na variável do
environment automaticamente, então dá pra rodar o fluxo criar pauta → abrir sessão → votar → apurar resultado em
sequência sem copiar e colar id em nenhum momento.

> **Importando:** no Postman, **Import** → arraste a collection e o(s) environment(s) → selecione o environment
> desejado no seletor do canto superior direito.
