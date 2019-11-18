# Project: ITC6480_final_project
class(AWS architect) project
A demo to upload files into S3 bucket through restful api
## Object 1: Develop Restful APIs to CRUD S3 bucket
### Configuration
#### Step 1: Create a Bucket
![create buckets](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/step1.png)
The default access property of the bucket would be private, which means public traffic would be denied.
#### Step 2: Register an IAM user with "AmazonS3FullAccess"
![register](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/step2.png)
#### Step 3: Create a SpringBoot Project and use Maven to add amazon dependency
![project start](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/step3.png)
#### Step 4: Add your bucket properties into the "application.yml" file
![ymi files](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/step4.png)
Currently I am adding the folder propertiy in the .yml file, and it will be removed and saved into database in the future.
### Restful APIs
#### Post APIs
##### Upload Single File
```java
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,file)
                .withCannedAcl(CannedAccessControlList.Private));
```
***
Postman Test
![single upload](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/postApi.png)
 (The returned value is the etag of the uploaded object)
##### Upload Multiple Files
```java
TransferManager transfer = TransferManagerBuilder.standard().withS3Client(s3Client).build();
            /*
             * transfer.uploadFileList
             * parameter 1:String bucketName
             * parameter 2:String virtualDirecotryKeyPrefix : the virtual directory of files to upload, use null or empty String to upload into the root of bucket
             * parameter 3:File direcotry: the common parent directory of files to upload, use new File(".") to upload without a common directory
             * parameter 4:List<File> files: a list of files to upload
             */
MultipleFileUpload upload = transfer.uploadFileList(bucketName,folderName,new File("."),files);
```
Postman Test
![mulitple upload](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/mulitiplePost.png)
#### Delete API
```Java 
    s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
```
Postman test
![delete](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/deleteApi.png)
#### GET APIs
##### List All Objects in a Folder
```java
      ObjectListing list = s3Client.listObjects(bucketName);
```
 Postman Test
![listFiles](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/getApi1.png)
##### Download a File
```java
      S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucketName,fileName));
```
Postman test
![download](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/getApi2.png)
### Async

&nbsp;&nbsp;&nbsp;&nbsp; In order to improve the efficiency of the whole system, we could use "Async" feature on some time-costing tasks, and in the current task, the most time-costing task would be uploading files task. When we were using "Sync" to execute uploading tasks, the main thread would wait it finished to continue. Then the user would need to wait last upload request finished to submit the new request, and it is not efficient. Then we would want a concurrent system, the "Async" feature would allocate a new sub-thread to the time-costing task, and the task would be done by the sub-thread. Then the main thread would not need to wait the task finished, and it could be released and be receive the next request.
>#### Try 1: Annotation @Async
>	```
>	//sample
>	 @Async
>    public Future<String> testAsyncUploads(List<File> files) {
>        String response = "";
>        try{
>            response = batchUploadToS3Bucket(files);
>        } catch (Exception e){
>            response = e.getMessage();
>            e.printStackTrace();
>        }
>        return new AsyncResult<>(response);
>   }
>   ```
>&nbsp;&nbsp;&nbsp;&nbsp; The @Async annotation is oriented to function, and it would allocate a new sub-thread to the function. The @Async must work on public solution, and it cannot work on static functions. 
>
>![@async](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/%40async.png)
>#### Try 2: Return Callable
>```
>	\\sample
>	Callable<String> result = ()->{
>   logger.info("task Thread start");
>   String status = this.amazonClient.batchUploadFiles(files);
>   logger.info("task Thread return");
>   return status;
>   };
>   ```
> &nbsp;&nbsp;&nbsp;&nbsp; Returning to Callable means that Spring MVC will call the task defined in a different thread. Spring will use the TaskExecutor to manage threads. The servlet thread will be released before waiting for the long-term task to complete.
>![callable](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/callable.png)
>
>&nbsp;&nbsp;&nbsp;&nbsp;We can see that we have returned from the servlet before the long-running task is completed. This does not mean that the client received a response. The communication with the client is still open waiting for the result, but the thread of the received request has been released and can serve the request of another client.
>#### Try 3: Return DeferredResult(using)
>```
> logger.info("Main Thread start");
> String orderNum = "12345678";
> myQueue.setUploadOrder(orderNum,files);
> DeferredResult<String> result = new DeferredResult<>();
> resultHolder.getMap().put(orderNum,result);
> ```
> ![deferredResult](https://github.com/BagaJing/s3-springBoot-itc6480/blob/master/sc/deferredResult.png)
> 
> &nbsp;&nbsp;&nbsp;&nbsp; What is the difference between returning DeferredResult and returning Callable? The difference is that this time the thread is managed by us. Creating a thread and setting the result to DeferredResult is done by ourselves.
### Next Steps:
>#### 1. Sync Download
>#### 2. Upload as a Directory
>#### 3. Rename Files/Directories 
### References:
#### AWS Tutorial
https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646

http://www.runoob.com/http/http-content-type.html
### SDK DOC
https://github.com/awsdocs/aws-doc-sdk-examples/tree/master/java/example_code/s3/src/main/java/aws/example/s3

https://sdk.amazonaws.com/java/api/latest/overview-summary.html



### Spring Tutorial
https://www.cnblogs.com/aheizi/p/5659030.html

https://blog.csdn.net/lory_li/article/details/80838843

### Debug
https://stackoverflow.com/questions/35680932/download-a-file-from-spring-boot-rest-service
