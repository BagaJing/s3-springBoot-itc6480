package com.awsdemo.demo.services;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.awsdemo.demo.utils.amazonUtils;
import com.awsdemo.demo.utils.contentTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;

@Service
public class amazonClientImpl implements amazonClient {
    private AmazonS3 s3Client;
    /*
     * Value annotation works for bind application properties directly into class
     */
    @Value("${amazonProperties.endpointUrl}")
    private String endPointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Value("${amazonProperties.folderName}")
    private String folderName;
    private Logger logger = LoggerFactory.getLogger(getClass());
    /*
     * innitializeAmazon(): set amazon credentials to amazon client
     * PostCOnstruct run the method after the construcor is called
     */
    @PostConstruct
    private  void initializeAmazon(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                //AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);
                return credentials;
            }

            @Override
            public void refresh() {

            }
        };
        Regions clientRegion = Regions.US_EAST_1;
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(credentialsProvider)
                .build();
    }
    /*
     * upload method
     * use putObject() to upload single file into bucket
     * */
    private String uploadFileToS3Bucket(String fileName,String folderName,File file){
        fileName = folderName+"/"+fileName;
        return s3Client.putObject(new PutObjectRequest(bucketName,fileName,file)
                .withCannedAcl(CannedAccessControlList.Private)).getETag();

        //s3Client.putObject(bucketName,accessKey,"Upload String object");
    }
    /*
     * upload mutiliple files method
     * use TransferManager.uploadFileList to upload multiple files
     * */

    private String batchUploadToS3Bucket(List<File> files,String dir) throws InterruptedException {
        //System.out.println("i am the upload function, i am using Thread: "+Thread.currentThread().getName()+" Current Time "+System.currentTimeMillis());
        if (files.size()==0) return "no files found to upload";
        String response = "";
        TransferManager transfer = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        try {
            /*
             * transfer.uploadFileList
             * parameter 1:String bucketName
             * parameter 2:String virtualDirecotryKeyPrefix : the virtual directory of files to upload, use null or empty String to upload into the root of bucket
             * parameter 3:File direcotry: the common parent directory of files to upload, use new File(".") to upload without a common directory
             * parameter 4:List<File> files: a list of files to upload
             */
            String path = folderName;
            if(dir.length()!=0) path += "/"+dir;
            MultipleFileUpload upload = transfer.uploadFileList(bucketName,path,new File("."),files);
            //amazonUtils.printProgressBar(0.0);
         /*   do{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    break;
                }
                TransferProgress progress = upload.getProgress();
                double pct = progress.getPercentTransferred();
                amazonUtils.eraseProgressBar();
                amazonUtils.printProgressBar(pct);
            }while (!upload.isDone());*/
            while (true){
                if (upload.isDone()){
                    response = upload.getState().toString();
                    break;
                }
            }

            /*upload succeed, delete local files*/

            for (File file : files){
                file.delete();
            }
        } catch (Exception e){
            response = e.getMessage();
        }
        return response;
    }
    private String uploadAsDirToS3(File dir){
        String response = "";
        TransferManager transfer = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        try {
            MultipleFileUpload upload = transfer.uploadDirectory(bucketName,folderName,dir,false);
            while (true){
                if (upload.isDone()){
                    response = upload.getState().toString();
                    break;
                }
            }
        } catch (Exception e){
            response = e.getMessage();
        }
        return response;
    }
    //-------------------------------------------------------public interfaces------------------------------------------
    /*
     * public interface for single upload
     * return the etag
     */
    @Override
    public String uploadFile(MultipartFile multipartFile){
        String response = "";
        try{
            //1.convert multipartFile into File, which the aws requires
            File file = amazonUtils.convertMultiPartFile(multipartFile);
            //2.generate a unique name
            String fileName = file.getName();
            System.out.println("Generate name "+fileName);
            //3.upload file into S3 bucket
            response = uploadFileToS3Bucket(fileName,folderName,file);
            //4.delete the file from ram
            file.delete();
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
    /*
     * public interface for batch upload
     * return a succeed message/exception
     */
    @Override
    public String batchUploadFiles(MultipartFile[] files,String dir){
      //  System.out.println("Hi Controller. THis is S3cilent. I am using Thread "+Thread.currentThread().getName()+" I will upload the files");
        String response = "";
        //   System.out.println("length: "+files.length);
        try{
            List<File> convertedFiles = new ArrayList<>();
            for(int i = 0 ; i < files.length ; i++){
                convertedFiles.add(amazonUtils.convertMultiPartFile(files[i]));
            }
            //  System.out.println("size: "+convertedFiles.size());
           response = batchUploadToS3Bucket(convertedFiles,dir);
        } catch (Exception e){
            response = e.getMessage();
            e.printStackTrace();
        }
        return response;
    }
    /*
     * list the file url from the bucket
     * Parameter 1 : bucketName, Parameter 2: fileName
     * */
    @Override
    public List<String> getKeyFromS3Bucket(String subDir){
        List<String> result = new ArrayList<>();
        String path = folderName;
        if(subDir.length()!=0) path += "/"+subDir+"/";
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                                                .withBucketName(bucketName)
                                                .withPrefix(path);
       // ObjectListing list = s3Client.listObjects(bucketName,path);
        ObjectListing list = s3Client.listObjects(listObjectsRequest);
        List<S3ObjectSummary> list2 = list.getObjectSummaries();
        String regex = folderName+ (subDir.length()==0? "":"/"+subDir)+"/(.*)/(.)*";
        Pattern pattern = Pattern.compile(folderName+ (subDir.length()==0? "":"/"+subDir)+"(/{1}[a-zA-Z1-9]*){1}");
        for(S3ObjectSummary object : list2){
            String fileName = object.getKey();
            if (fileName.matches(regex)){
                Matcher matcher =pattern.matcher(fileName);
                if (matcher.find()) fileName = matcher.group();
            }
            if (!result.contains(fileName))result.add(fileName);
        }
        return result;
    }
    /*
     * return file content to Restful API(GET)
     * Parameter : fileName
     * */
    @Override
    public ResponseEntity<Resource> download(String fileName) throws IOException {
        S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucketName,fileName));
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //System.out.println(suffix);
        InputStream in = fullObject.getObjectContent();
        InputStreamResource resource = new InputStreamResource(in);
        HttpHeaders headers = new HttpHeaders();
        String content_type = contentTypeUtils.toCotentType(suffix);
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=file"+suffix);
        headers.add("Cache-Control","no-cahce,no-store,must-revalidate");
        headers.add("Pargma","no-cache");
        headers.add("Expires","0");
        return  ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(content_type))
                .body(resource);
    }
    /*
     * Delete the file from the bucket
     * Parameter 1 : bucketName, Parameter 2: fileName
     * */
    @Override
    public void deleteFile(String fileName){
        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
    }

    @Override
    public boolean renameFile(String oldName, String newName) {
        logger.info("rename service called");
        boolean res = false;
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName,oldName,bucketName,newName);
        try {
            s3Client.copyObject(copyObjectRequest);
            deleteFile(oldName);
            res = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean reNameFolder(String oldFolder, String newFolder) {
        boolean res = false;
        logger.info("refolder function called");
        try {
            List<String> nameList = getKeyFromS3Bucket(oldFolder);
            //System.out.println(nameList);
            List<CopyObjectRequest> copyRequests = new LinkedList<>();
            for (String name : nameList) {
                String newName = name.replaceFirst(oldFolder,newFolder);
                CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, name, bucketName, newName);
                copyRequests.add(copyObjectRequest);
            }
            for (CopyObjectRequest request : copyRequests) {
                s3Client.copyObject(request);
            }
            for (String oldFile : nameList) {
                deleteFile(oldFile);
            }
            res = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}

