# ITC6480 Final Project serverless-springboot-s3-cognito-apiGateway
### Description:
            The project is a practice project that provides users the access to upload standarded jpg file 
    into the registered folder of a private S3 bucket, and the access of interactions with the app is protected by
    the cognito service.
            The app is using the IAM user to get the permssion to do interaction with S3 bucket and Cognito Service.
    Currently, the accesskey of needed IAM user is included in the "demo-0.0.1-SNAPSHOT.jar" for the convenience of
    tempoary deployment on lambda, and it will be expired in the future(maybe after 12.14).
### Deployment Guide:
#### 1. download the "demo-0.0.1-SNAPSHOT.jar"
#### 2. upload to Lambda Function
##### 2.1 Create a new Lambda Function in your AWS Account, choose "Java 11" as the language env
  ![lambda](https://github.com/BagaJing/sc/blob/master/Screenshot%20from%202019-12-09%2019-22-21.png) 
##### 2.2 Upload the "demo-0.0.1-SNAPSHOT.jar" 
	the path of Handler is
	 "com.awsdemo.demo.StreamLambdaHandler::handleRequest"
![upload](https://github.com/BagaJing/sc/blob/master/Screenshot%20from%202019-12-09%2019-32-24.png)
##### 2.3 Cofigure the CPU:
	Since the speed of lambda to run JVM is relatively slow, then we need to make full use of the CPU power to make our application initialize on time.
![CPU](https://github.com/BagaJing/sc/blob/master/Screenshot%20from%202019-12-09%2019-35-38.png)
##### 2.4 Test the lambda function
	After saving the file and the cofiguration, we could do a API Test to check it. Since we would use the API Gateway to trigger the lambda function. We could create a "Amazon API Gateway AWS Proxy" to test the restful api, and just modify the path to "/client/login" and the "httpMethod" to "GET" to get the login page
	
![ApiTest](https://github.com/BagaJing/sc/blob/master/Screenshot%20from%202019-12-09%2019-40-05.png)

	If everything goes well, you would get a 200 response with with page of login page.
	
![login200](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2019-40-55.png)

#### 3. Create a new Restful API in AWS API GATEWAY
	In this Project, we are using AWS API GATEWAT to trigger our lambda function.
![restAPI](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2020-11-00.png)

![createRestAPI](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2020-14-21.png)

	3.1 After Creating a new Restful API, Click "Create a Resource" 
	3.2 Then make the "proxy resource" and "Enable API Gateway CORS" choosen, and Click "Create Resource"
![CreateResource](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2020-14-21.png)

	3.3  Then we will jump to the "Setup" Page, search the Lambda function's name that we just created and choose it, and click "Save".
	
![chooseFunction](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2020-14-21.png)

	3.4 then click "Deploy API", and create a new Stage. In order to make all the api request could be redirect correctly, please make sure the "stage name" is "serverless". 
![deploy](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2020-16-07.png)
#### 4.finally we would get a "Invoke URL", click it to go a new tab, and add "/client/login" to direct to the login page.
![invokeUrl](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2020-16-50.png)
![loginPage](https://raw.githubusercontent.com/BagaJing/sc/master/Screenshot%20from%202019-12-09%2020-16-41.png)
	
