aws ec2 start-instances --instance-ids i-0e273413d70ab7197
chmod 400 PiProgramServer.pem
echo
echo
echo "About to enter web tier....Remember to run ./s3datainfo.sh to start server"
ssh -i PiProgramServer.pem  ubuntu@52.35.116.19