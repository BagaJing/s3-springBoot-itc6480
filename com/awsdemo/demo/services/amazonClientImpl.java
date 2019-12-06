package com.awsdemo.demo.services;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.*;
import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.utils.amazonUtils;
import com.awsdemo.demo.utils.contentTypeUtils;
import com.awsdemo.demo.utils.zipUtils;
import org.hibernate.SessionException;
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
import java.util.*;
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
    private Logger logger = LoggerFactory.getLogger(getClass());
    /*
     * innitializeAmazon(): set amazon credentials to amazon client
     * PostCOnstruct run the method after the construcor is called
     */
    @PostConstruct
    public void initializeAmazon() throws Exception {
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

    private  String batchUploadToS3Bucket(List<File> files, String dir, String folderName) {
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
            ObjectCannedAclProvider aclProvider = new ObjectCannedAclProvider() {
                @Override
                public CannedAccessControlList provideObjectCannedAcl(File file) {
                    return CannedAccessControlList.Private;
                }
            };
            String path = folderName;
            // path: rootFolder Name dir: relative path
            path = path + (dir.equals("/")? "":dir);
            // path: rootFolder Name dir: relative path
            logger.info("final Path:" +path);
            for (File file : files)
                uploadFileToS3Bucket(file.getName(),path,file);
            for (File file : files)
                file.delete();

            /*
            MultipleFileUpload upload = transfer.uploadFileList(bucketName,"bagajing",new File("."),files,null,null,aclProvider);
            do{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    break;
                }
                TransferProgress progress = upload.getProgress();
                double pct = progress.getPercentTransferred();
                amazonUtils.eraseProgressBar();
                amazonUtils.printProgressBar(pct);
            }while (!upload.isDone());
            response = upload.getState().toString();
            /*upload succeed, delete local files*/



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
    public String uploadFile(MultipartFile multipartFile,String folderName){
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
   // @Transactional
    public String batchUploadFiles(MultipartFile[] files,String dir,String folderName){
      //  System.out.println("Hi Controller. THis is S3cilent. I am using Thread "+Thread.currentThread().getName()+" I will upload the files");
        String response = "";
        //   System.out.println("length: "+files.length);
        try{
            List<File> convertedFiles = new ArrayList<>();
            for(int i = 0 ; i < files.length ; i++){
                convertedFiles.add(amazonUtils.convertMultiPartFile(files[i]));
              //  convertedFiles.add(files[i]);
            }
            //  System.out.println("size: "+convertedFiles.size());
           response = batchUploadToS3Bucket(convertedFiles,dir,folderName);
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
    public List<String> getKeyFromS3Bucket(String subDir,String folderName){
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
    public ResponseEntity<byte[]> download(String fileName) throws IOException {
        S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucketName,fileName));
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //System.out.println(suffix);
        InputStream in = fullObject.getObjectContent();
        return toResponseEntity(in,getFileName(fileName),suffix);
    }
    private String getFileName(String fileName){
        int index = fileName.lastIndexOf("/");
        if (index == -1) index = fileName.indexOf("/");
        if (index == -1) return "default";
        String res = fileName.substring(index+1);
        res = res.substring(0,res.indexOf("."));
        return  res;
    }
    /*
     * Delete the file from the bucket
     * Parameter 1 : bucketName, Parameter 2: fileName
     * */
    @Override
    public ResponseEntity<byte[]> downloadFolder(String path) throws IOException {
        TransferManager transfer = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        if (path.endsWith("/")) path = path.substring(0,path.length()-1);
        path = "/tmp/"+path;
        String dirName = path.substring(path.indexOf("/")+1);
        File parent = new File(dirName);
        //logger.info("downloadFolder test: dir "+dirName);
        try {
            MultipleFileDownload  multipleFiles=transfer.downloadDirectory(bucketName,path,parent,true);
            while (true){
                if (multipleFiles.isDone()) break;
            }
            logger.info("Folder Download Completed");
        } catch (AmazonS3Exception e){
            logger.error("download Folder Exception from AmazonClientImpl",e);
        }
        String sourceDir = path;
        logger.info("download Folder exists or not :"+ new File(sourceDir).exists());
        zipUtils.folderToZip(sourceDir,new FileOutputStream(new File(dirName+".zip")));

        //logger.info("zip outcome exists or not: "+new File(sourceDir+".zip").exists());
        File zipFile = new File(sourceDir+".zip");
        if (zipFile.exists()){
          logger.info("zip process finished, start to delete source folders");
          File file = new File(sourceDir);
          if(file.exists()){
              logger.info("start to delete");
              while (true){
                  if (deleteDir(file)) break;
              }
          }
          InputStream in = new FileInputStream(zipFile);
          ResponseEntity<byte[]> res = toResponseEntity(in,sourceDir,".zip");
          zipFile.delete();
          return res;
        }
        return null;
    }
    private static ResponseEntity<byte[]> toResponseEntity(InputStream in,String name,String suffix) throws IOException{
        InputStreamResource resource = new InputStreamResource(in);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = in.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, len);
        }
        byte[] body = swapStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        String content_type = contentTypeUtils.toCotentType(suffix);
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+name+suffix);
        headers.add("Cache-Control","no-cahce,no-store,must-revalidate");
        headers.add("Pargma","no-cache");
        headers.add("Expires","0");
        return  ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(content_type))
                .body(body);
    }
    private static boolean deleteDir(File dir){
        if (dir.isDirectory()){
            String[] children = dir.list();
            for (int i = 0 ; i < children.length ; i++){
                boolean success = deleteDir(new File(dir,children[i]));
                if (!success) return false;
            }
        }
        return dir.delete();
    }
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
    public void renameFolder(String oldPrefix,String newFolder,int level){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                                                .withBucketName(bucketName)
                                                .withPrefix(oldPrefix);
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
        String newPrefix = getNewPath(oldPrefix,newFolder,level);
        try {
            while (true){
                for (S3ObjectSummary summary : objectListing.getObjectSummaries()){
                    String oldPath = summary.getKey();
                    //copy
                    String newPath = oldPath.replace(oldPrefix,newPrefix);
                    CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName,oldPath,bucketName,newPath);
                    s3Client.copyObject(copyObjectRequest);
                    //delete
                    s3Client.deleteObject(bucketName,oldPath);

                }
                if (objectListing.isTruncated()) {
                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
                }
                else break;
            }
        } catch (Exception e){
            logger.error("Rename Folder Exception",e.getMessage());
        }
    }
    private String getNewPath(String oldPath,String newName,int level){
        String[] strs= oldPath.split("/");
        String newPath = "";
        for (int i = 0 ; i < strs.length ; i++){
            if (i != level-1)
                newPath += strs[i]+"/";
            else
                newPath += newName+"/";
        }
        return  newPath.substring(0,newPath.length()-1);
    }

    @Override
    public void deleteFolder(String prefix,String folderName) {
        prefix = folderName+"/"+prefix+"/"; //add a slash at last to prevent same level folders have same prefix be delted
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                                                .withBucketName(bucketName)
                                                .withPrefix(prefix);

        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
        try {
            while (true){
                for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                    logger.info(summary.getKey());
                    s3Client.deleteObject(bucketName, summary.getKey());
                }
                if (objectListing.isTruncated())
                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
                else break;
            }
        } catch (Exception e){
            logger.error("deleteFolder Exception",e.getMessage());
        }
    }

    @Override
    public boolean createCustomer(String nickname) {
        File init = new File("readme.txt");
        if (init.exists()) {
            try {
               String response = uploadFileToS3Bucket(init.getName(),nickname,init);
               if (!response.equals("")) return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        else
            logger.error("init file not found");
        return false;
    }
}

