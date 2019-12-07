package com.awsdemo.demo.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.awsdemo.demo.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import vo.userBasic;

import java.util.List;

public class cognitoService implements cognitoInterface {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //@Value("${CognitoProperties.aKey}")
    private String cognitoAccessKey = "AKIAR4AZAJJLTVVJSTPA";
    //@Value("${CognitoProperties.sKey}")
    private String cognitoSecretKey = "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";
    //@Value("${CognitoProperties.userPool}")
    private String poolId = "us-east-1_Y2PgefGKO";
    private AWSCognitoIdentityProvider identityProvider = null;
    public cognitoService() {
        //logger.info(cognitoAccessKey);
       // logger.info(cognitoSecretKey);
        if(identityProvider == null){
            identityProvider = getCognitoIdentityProvider();
        }
    }

    private AWSCognitoIdentityProvider getCognitoIdentityProvider(){
        AWSCognitoIdentityProvider cognitoIdentityProvider = null;
        try {
            AWSCredentials credentials = getCredentials();
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
             cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion(clientRegion)
                    .build();

        } catch (Exception e){
            logger.error(e.getMessage());
        }
        return cognitoIdentityProvider;
    }
    private AWSCredentials getCredentials(){
        AWSCredentials credentials = new BasicAWSCredentials(this.cognitoAccessKey,this.cognitoSecretKey);
        return credentials;
    }
    @Override
    public void createNewUser(userBasic customer) {
        String name = customer.getUsername();
        try {
            AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
                    .withUserPoolId(poolId)
                    .withUsername(name)
                    .withUserAttributes(
                            new AttributeType().withName("email").withValue(customer.getEmail()),
                            new AttributeType().withName("nickname").withValue(customer.getNickname()),
                            new AttributeType().withName("custom:id").withValue(String.valueOf(customer.getId()))
                    );
            identityProvider.adminCreateUser(cognitoRequest);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }
    //create new user
    public userBasic findUserByEmail(String email) throws Exception {
        userBasic user = null;
        if (email != null && email.length()>0){
            final String emailQuery = "email=\""+email+"\"";
            ListUsersRequest usersRequest = new ListUsersRequest()
                                            .withUserPoolId(poolId)
                                            .withAttributesToGet("nickname","custom:id")
                                            .withFilter(emailQuery);
            ListUsersResult usersResult = identityProvider.listUsers(usersRequest);
            List<UserType> users = usersResult.getUsers();
            if (users!=null&&users.size()>0){
                if (users.size() == 1){
                    user = new userBasic();
                    UserType userType = users.get(0);
                    final  String userName =userType.getUsername();
                    List<AttributeType> attributes = userType.getAttributes();
                    if (attributes!=null){
                        for (AttributeType attr : attributes){
                            if (attr.getName().equals("nickname"))
                                user.setNickname(attr.getValue());
                            if (attr.getName().equals("email"))
                                user.setNickname(attr.getValue());
                            if (attr.getName().equals("custom:id"))
                                user.setId(Long.valueOf(attr.getValue()));
                        }
                    }
                } else throw  new Exception("More than one user have the email addr");
            }
        }
        return user;
    }
    //session login
    
}
