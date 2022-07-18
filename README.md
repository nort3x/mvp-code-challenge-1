[![Publish Docker image](https://github.com/nort3x/mvp-code-challenge-1/actions/workflows/docker-push.yml/badge.svg)](https://github.com/nort3x/mvp-code-challenge-1/actions/workflows/docker-push.yml)

## MVP code challenge #1 - CRUD


### how to launch?
use docker-compose with `deploy.yaml`

```bash
# (postgres-admin will fail due file permission problem but ignore it)
docker-compose -f deploy.yaml up
# fix permission problems
cd /usr/mvp && sudo chmod 777 *
```

### configure database:
* go to `http://localhost:5557` 
* login with `humanardaki@gmail.com:postgres`
* add server with connection `mvp-postgres-database` and `postgres` as password
* create database `mvp-code-challenge-1`

| service name          | binding port |
|-----------------------|--------------|
| mvp-cc1-server        | 5555         |
| mvp-postgres-database | 5556         |
| mvp-postgres-admin    | 5557         |

all containers are configured to `restart-always` so no problems should arise
