Please follow these instructions :-

1. Run the command:
$aws configure 
for ubuntu and su users

AWS Access Key ID : 
AWS Secret Access Key : 
Default region name : us-west-2
Default output format : json

2. Copy PiProgramServer.pem and .sh files present in the scripts folder in the same location of your machine

3. Run  sudo ./start.sh  (Run this script with the super user)

4. After establishing the SSH connection to web tier, run ./startserver.sh

5. Once server starts, you can hit the url   http://52.35.116.19/cloudPi.php?input=x  where x is an integer

For example we can give sample input : http://52.35.116.19/cloudPi.php?input=500


