helm repo add istio https://istio-release.storage.googleapis.com/charts
helm repo update
kubectl create namespace istio-system
helm install istio-base istio/base -n istio-system --set defaultRevision=default
helm ls -n istio-system
helm install istiod istio/istiod -n istio-system --wait
helm ls -n istio-system
helm status istiod -n istio-system
kubectl get deployments -n istio-system --output wide
kubectl create namespace istio-ingress
helm install istio-ingress istio/gateway -n istio-ingress --wait
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.19/samples/addons/prometheus.yaml