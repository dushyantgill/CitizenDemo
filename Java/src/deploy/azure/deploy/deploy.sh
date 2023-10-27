az aks get-credentials --resource-group citizendemo861a --name citizendemo861aaks --overwrite-existing
kubectl config use-context citizendemo861aaks
kubectl create namespace citizendemo
kubectl label namespace default istio.io/rev=asm-1-17
kubectl apply -f mongodb.yaml
kubectl apply -f kafka-zookeeper.yaml
kubectl apply -f kafka.yaml
kubectl apply -f resourceapi.yaml
kubectl apply -f citizenapi.yaml
kubectl apply -f provisionworker.yaml
kubectl apply -f loadgenerator.yaml
kubectl apply -f prometheus-scrapeconfig.yaml
