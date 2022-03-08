package io.paketo;

    import java.sql.Timestamp;
    import java.util.List;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class CosExample
    {

        private AmazonS3 _cosClient;

	public CosExample(){
	}

	public String test(){
		return "hanamaru";
	}

        /**
         * @param args
         */
        public void run ()
        {

            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";

            String bucketName = "<BUCKET_NAME>";  // eg my-unique-bucket-name
            String newBucketName = "<NEW_BUCKET_NAME>"; // eg my-other-unique-bucket-name
            String api_key = "<API_KEY>"; // eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
            String service_instance_id = "<SERVICE_INSTANCE_ID"; // eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud"; // this could be any service endpoint

            String storageClass = "us-south-standard";
            String location = "us"; 

            System.out.println("Current time: " + new Timestamp(System.currentTimeMillis()).toString());
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            listObjects(bucketName, _cosClient);
            createBucket(newBucketName, _cosClient, storageClass);
            listBuckets(_cosClient);
        }

        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

        /**
         * @param bucketName
         * @param cosClient
         */
        public void listObjects(String bucketName, AmazonS3 cosClient)
        {
            System.out.println("Listing objects in bucket " + bucketName);
            ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println();
        }

        /**
         * @param bucketName
         * @param cosClient
         * @param storageClass
         */
        public void createBucket(String bucketName, AmazonS3 cosClient, String storageClass)
        {
            cosClient.createBucket(bucketName, storageClass);
        }

        /**
         * @param cosClient
         */
        public void listBuckets(AmazonS3 cosClient)
        {
            System.out.println("Listing buckets");
            final List<Bucket> bucketList = _cosClient.listBuckets();
            for (final Bucket bucket : bucketList) {
                System.out.println(bucket.getName());
            }
            System.out.println();
        }

    }
