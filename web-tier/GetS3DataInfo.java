import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.util.Base64;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.lightsail.model.StopInstanceResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DomainMetadataRequest;
import com.amazonaws.services.simpledb.model.DomainMetadataResult;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;

public class GetS3DataInfo {

	private static String bucketName = "cloudpiamit";
	static String ACCESS = "AKIAIVQQRJZLFAEEBC3Q";
	static String SECRET = "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";


	public static void main(String[] args) throws IOException {
		BasicAWSCredentials cred = new BasicAWSCredentials(ACCESS, SECRET);
		AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(cred);
		AmazonS3 s3client = new AmazonS3Client(credentialsProvider);
		try {
			System.out.println("Listing objects");
			final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
			ListObjectsV2Result result;
			do {
				result = s3client.listObjectsV2(req);

				for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
					System.out.println(
							" key " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")"  + "  value " + s3client.getObjectAsString("cloudpiamit", objectSummary.getKey()));
					System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");
				}
				//System.out.println("Next Continuation Token : " + result.getNextContinuationToken());
				req.setContinuationToken(result.getNextContinuationToken());
			} while (result.isTruncated() == true);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, " + "which means your request made it "
					+ "to Amazon S3, but was rejected with an error response " + "for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, " + "which means the client encountered "
					+ "an internal error while trying to communicate" + " with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
}