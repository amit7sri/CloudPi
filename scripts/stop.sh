aws ec2 stop-instances --instance-ids i-0e273413d70ab7197
sleep 15s
for temp in $(aws ec2 describe-instances --filters " Name=instance-state-name, Values= running"  --query 'Reservations[*].Instances[*].InstanceId')
do
aws ec2 terminate-instances --instance-ids $temp
done
aws s3 rm s3://cloudpiamit --recursive
