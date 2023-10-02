cd ../../resourceapi
docker build -t resourceapi:latest -f dockerfile .
cd ../citizenapi
docker build -t citizenapi:latest -f dockerfile .
cd ../provisionworker
docker build -t provisionworker:latest -f dockerfile .
cd ../loadgenerator
docker build -t loadgenerator:latest -f dockerfile .
cd ../deploy
kubectl create namespace citizendemo
kubectl label namespace citizendemo istio-injection=enabled --overwrite
kubectl apply -f k8s/promtail.yaml
kubectl apply -f k8s/mongodb.yaml
kubectl apply -f k8s/kafka-zookeeper.yaml
kubectl apply -f k8s/kafka.yaml
kubectl apply -f k8s/resourceapi.yaml
kubectl apply -f k8s/citizenapi.yaml
kubectl apply -f k8s/provisionworker.yaml
kubectl apply -f k8s/loadgenerator.yaml
kubectl apply -f k8s/loki.yaml
kubectl apply -f k8s/jaeger.yaml
kubectl apply -f k8s/prometheus.yaml
kubectl apply -f k8s/grafana.yaml