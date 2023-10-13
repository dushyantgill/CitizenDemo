cd ../../../resourceapi
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
cd ../deploy/azure/build
docker tag resourceapi:latest citizendemoacr.azurecr.io/resourceapi
docker tag citizenapi:latest citizendemoacr.azurecr.io/citizenapi
docker tag provisionworker:latest citizendemoacr.azurecr.io/provisionworker
docker tag loadgenerator:latest citizendemoacr.azurecr.io/loadgenerator
az acr login --name citizendemoacr
docker push citizendemoacr.azurecr.io/resourceapi
docker push citizendemoacr.azurecr.io/citizenapi
docker push citizendemoacr.azurecr.io/provisionworker
docker push citizendemoacr.azurecr.io/loadgenerator