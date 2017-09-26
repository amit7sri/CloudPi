Group Member Names : Amit Kumar , Harsha Sharma
Public IP : 52.35.116.19

Please follow these instructions :-

1. Run the command:
$aws configure 
for ubuntu and su users

AWS Access Key ID : AKIAIVQQRJZLFAEEBC3Q
AWS Secret Access Key : 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
Default region name : us-west-2
Default output format : json

2. Copy PiProgramServer.pem and .sh files present in the scripts folder in the same location of your machine

3. Run  sudo ./start.sh  (Run this script with the super user)

4. After establishing the SSH connection to web tier, run ./startserver.sh

5. Once server starts, you can hit the url   http://52.35.116.19/cloudPi.php?input=x  where x is an integer

For example we can give sample input : http://52.35.116.19/cloudPi.php?input=500

6. Note :
   a. Run  sudo ./list_data.sh  (list input-output pairs in S3)
   b. After establishing the SSH connection , run the command ./s3datainfo.sh

7. Run ./list_instances.sh and ./stop.sh for listing all ec2 instances and stopping running ec2 instances respectively.


Note: Please contact us, if facing any issue in running.
 

References : Rabbitmq - https://www.rabbitmq.com/tutorials/tutorial-six-python.html 

