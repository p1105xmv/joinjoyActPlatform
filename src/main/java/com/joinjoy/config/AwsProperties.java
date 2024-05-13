package com.joinjoy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="aws")
public class AwsProperties {
	 private String accessKeyId;
	    private String secretAccessKey;
	    private S3 s3;

	    
	    public String getAccessKeyId() {
			return accessKeyId;
		}


		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}


		public String getSecretAccessKey() {
			return secretAccessKey;
		}


		public void setSecretAccessKey(String secretAccessKey) {
			this.secretAccessKey = secretAccessKey;
		}


		public S3 getS3() {
			return s3;
		}


		public void setS3(S3 s3) {
			this.s3 = s3;
		}


		public static class S3 {
	        private String bucketName;
	        private String region;
			public String getBucketName() {
				return bucketName;
			}
			public void setBucketName(String bucketName) {
				this.bucketName = bucketName;
			}
			public String getRegion() {
				return region;
			}
			public void setRegion(String region) {
				this.region = region;
			}

	        
	    }
}
