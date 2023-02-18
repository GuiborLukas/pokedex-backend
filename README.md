Criar conteiner docker para o BD<br>
<br>
docker run --name pokemon-bd -e LANG="en_US.UTF-8" -e "POSTGRES_PASSWORD=postgres" -p 5432:5432 -d postgres
