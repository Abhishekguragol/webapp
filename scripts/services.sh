#!/bin/bash

echo "------------Daemon service reload-------------"
sudo systemctl daemon-reload
echo "----------------------------------------------"

echo "------------Enable mysql service-------------"
sudo systemctl enable mysqld.service
echo "----------------------------------------------"

echo "------------Enable app service-------------"
sudo systemctl enable appstart.service
echo "----------------------------------------------"