terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  required_version = ">= 1.3.0"
}

provider "aws" {
  region = var.aws_region
}

variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "ap-southeast-1"
}

variable "bucket_name" {
  description = "Name of the S3"
  type        = string
  default     = "test-pipeline-898203562451-bucket"
}

# Create S3 bucket
resource "aws_s3_bucket" "demo_bucket" {
  bucket = var.bucket_name

  tags = {
    Name        = var.bucket_name
    Environment = "dev"
  }
}

# resource "aws_s3_bucket_lifecycle_configuration" "demo_lifecycle" {
#   bucket = aws_s3_bucket.demo_bucket.id

#   rule {
#     id     = "abort-incomplete-multipart-uploads"
#     status = "Enabled"

#     abort_incomplete_multipart_upload {
#       days_after_initiation = 7  # Delete incomplete parts after 7 days
#     }
#   }
# }