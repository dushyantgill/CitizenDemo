az aks get-credentials --resource-group cdjtest --name cdjtestaks
kubectl create namespace citizendemo
kubectl apply -f appmonitoring-autoinstrumentation.yaml
sleep 60
kubectl apply -f mongodb.yaml
kubectl apply -f kafka-zookeeper.yaml
kubectl apply -f kafka.yaml
kubectl apply -f resourceapi.yaml
kubectl apply -f citizenapi.yaml
kubectl apply -f provisionworker.yaml
kubectl apply -f loadgenerator.yaml
kubectl apply -f prometheus-scrapeconfig.yaml
