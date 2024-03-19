#!/bin/bash

echo "------------Update CentOs 8-------------"
sudo dnf update -y
echo "------------CentOs 8 Updated-------------"

echo "------------Upgrade CentOs 8 Packages-------------"
sudo dnf upgrade -y
echo "------------Upgraded CentOs 8 Packages-------------"

echo "------------Move webapp jar-------------------------"
sudo mv /tmp/project-0.0.1-SNAPSHOT.jar /opt/project-0.0.1-SNAPSHOT.jar
echo "------------Move webapp jar complete----------------"

echo "------------Setup Logs--------------------------------"
sudo mkdir /var/log/csye6225
sudo touch /var/log/csye6225/app.log
sudo chown csye6225:csye6225 /var/log/csye6225/app.log

sudo touch /var/log/csye6225/errors.log
sudo chown csye6225:csye6225 /var/log/csye6225/errors.log
echo "------------Logs ready--------------------------------"


echo "-------------------Install Java 17---------------------"
sudo dnf -y install java-17-openjdk java-17-openjdk-devel
echo "-------------------Installed Java 17---------------------"

echo "------------Move service file-------------------------"
sudo mv /tmp/appstart.service /etc/systemd/system/appstart.service
echo "------------Move service file complete----------------"

echo "-------------------Install Google Ops Agent---------------------"
curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh
sudo bash add-google-cloud-ops-agent-repo.sh --also-install \
  --version=latest
echo "-------------------Installed Google Ops Agent---------------------"


# Check if user exists, then update ownership
if id -u csye6225 >/dev/null 2>&1; then
  echo "User 'csye6225' exists, updating application permissions...."
  sudo chown -R csye6225:csye6225 /opt/project-0.0.1-SNAPSHOT.jar
else
  echo "User doesn't exists, ownership cannot be updated...exiting"
  exit 1
fi