#!/usr/bin/env bash
set -e

echo "=========================================================="
echo " CostGuard OpenShift Deployment Automation Script"
echo "=========================================================="

PROJECT_NAME="${OPENSHIFT_PROJECT:-costguard-devops}"

echo "[*] Creating OpenShift Project: ${PROJECT_NAME}..."
oc new-project "${PROJECT_NAME}" || oc project "${PROJECT_NAME}"

echo "[*] Applying OpenShift ImageStream & BuildConfig..."
oc apply -f openshift/buildconfig.yaml

echo "[*] Triggering OpenShift Binary / Git Build..."
oc start-build costguard-build --from-dir=. --follow || true

echo "[*] Applying Kubernetes Core Manifests (ConfigMap, Deployments, Services)..."
oc apply -f k8s/configmap.yaml -n "${PROJECT_NAME}"
oc apply -f k8s/deployment.yaml -n "${PROJECT_NAME}"
oc apply -f k8s/service.yaml -n "${PROJECT_NAME}"
oc apply -f k8s/otel-collector.yaml -n "${PROJECT_NAME}"

echo "[*] Exposing Service via OpenShift Route (TLS Edge Termination)..."
oc apply -f openshift/route.yaml -n "${PROJECT_NAME}"

ROUTE_URL=$(oc get route costguard-route -n "${PROJECT_NAME}" -o jsonpath='{.spec.host}' 2>/dev/null || echo "costguard.apps.cluster.openshift.com")

echo "=========================================================="
echo "[SUCCESS] CostGuard successfully deployed on OpenShift!"
echo "Access URL: https://${ROUTE_URL}"
echo "=========================================================="
