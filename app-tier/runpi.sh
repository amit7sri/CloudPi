#!/bin/bash
cd /pifft
sudo rm -rf ref.in
sudo echo $1 >> ref.in
make clean
make
sudo ./pifft ref.in > "$1"ref.out
aws s3 cp "$1"ref.out s3://cloudpiamit
