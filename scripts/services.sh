#!/bin/bash

echo "------------Daemon service reload-------------"
sudo systemctl daemon-reload
echo "----------------------------------------------"

echo "------------Daemon service reload-------------"
sudo systemctl enable appstart
echo "----------------------------------------------"