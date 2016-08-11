yum install -y docker maven java-1.8.0-openjdk-devel.x86_64
sudo sed -i "s/# INSECURE_REGISTRY='--insecure-registry '/INSECURE_REGISTRY='--insecure-registry 0.0.0.0\/0'/" /etc/sysconfig/docker
echo "JAVA_HOME=/usr/lib/jvm/java-1.8.0" >> /home/vagrant/.bashrc
