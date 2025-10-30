import boto3
# Create an S3 client
s3 = boto3.client('s3')

# Define bucket and folder names
bucket_name = 'new-bucket-boto3-1234567890'
folder_name = 'folder_inside_bucket/'

# Create the S3 bucket
s3.create_bucket(
    Bucket=bucket_name,
    CreateBucketConfiguration={'LocationConstraint': 'ap-southeast-1'}
)

# Create a folder by uploading an empty object with a trailing slash
s3.put_object(Bucket=bucket_name, Key=folder_name)

# Define a lifecycle configuration
lifecycle_configuration = {
    'Rules': [
        {
            'ID': 'DeleteOldObjects',
            'Prefix': folder_name,
            'Status': 'Enabled',
            'Expiration': {'Days': 30}
        }
    ]
}

# Apply the lifecycle configuration to the bucket
s3.put_bucket_lifecycle_configuration(
    Bucket=bucket_name,
    LifecycleConfiguration=lifecycle_configuration
)