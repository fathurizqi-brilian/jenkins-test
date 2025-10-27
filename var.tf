variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "ap-southeast-1"
}

variable "bucket_name" {
  description = "Name of the S3"
  type        = string
  default     = "testing-pipeline-898203562451-bucket"
}