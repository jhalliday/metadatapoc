sudo docker   build  --rm -t  metadata-registry-rest  .
sudo docker tag  metadata-registry-rest $REGISTRY/redhatanalytics/metadata-registry-rest
sudo docker push $REGISTRY/redhatanalytics/metadata-registry-rest
