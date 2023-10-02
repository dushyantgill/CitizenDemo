cd ../../resourceapi
./mvnw clean package
docker build --platform linux/amd64 -t resourceapi:latest -f dockerfile .
cd ../citizenapi
./mvnw clean package
docker build --platform linux/amd64 -t citizenapi:latest -f dockerfile .
cd ../provisionworker
./mvnw clean package
docker build --platform linux/amd64 -t provisionworker:latest -f dockerfile .
cd ../loadgenerator
./mvnw clean package
docker build --platform linux/amd64 -t loadgenerator:latest -f dockerfile .
cd ../deploy
docker tag resourceapi:latest cdjtestacr.azurecr.io/resourceapi
docker tag citizenapi:latest cdjtestacr.azurecr.io/citizenapi
docker tag provisionworker:latest cdjtestacr.azurecr.io/provisionworker
docker tag loadgenerator:latest cdjtestacr.azurecr.io/loadgenerator
az acr login --name cdjtestacr
docker push cdjtestacr.azurecr.io/resourceapi
docker push cdjtestacr.azurecr.io/citizenapi
docker push cdjtestacr.azurecr.io/provisionworker
docker push cdjtestacr.azurecr.io/loadgenerator