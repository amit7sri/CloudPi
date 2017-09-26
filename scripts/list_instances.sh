for i in $( aws ec2 describe-instances --output text --query 'Reservations[*].Instances[*].[InstanceId]')
do
aws cloudwatch get-metric-statistics --namespace AWS/EC2 --metric-name CPUUtilization --dimensions Name=InstanceId,Value=$i --statistics Maximum --start-time 2017-03-10T23:18:00 --end-time 2017-03-13T23:18:00 --period 3600 --output table 
done
