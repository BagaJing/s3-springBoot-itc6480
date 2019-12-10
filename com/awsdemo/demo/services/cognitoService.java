
        package com.awsdemo.demo.services;

        import com.amazonaws.auth.AWSCredentials;
        import com.amazonaws.auth.AWSCredentialsProvider;
        import com.amazonaws.auth.BasicAWSCredentials;
        import com.amazonaws.regions.Regions;
        import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
        import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
        import com.amazonaws.services.cognitoidp.model.*;
        import com.auth0.jwt.JWT;
        import com.auth0.jwt.algorithms.Algorithm;
        import com.auth0.jwt.JWTVerifier;
        import com.auth0.jwt.interfaces.RSAKeyProvider;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import vo.userBasic;
        import java.util.HashMap;
        import java.util.List;

public class cognitoService implements cognitoInterface {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //@Value("${CognitoProperties.aKey}")
    private String cognitoAccessKey = "";
    //@Value("${CognitoProperties.sKey}")
    private String cognitoSecretKey = "";
    //@Value("${CognitoProperties.userPool}")
    private String poolId = "us-east-1_Y2PgefGKO";
    private String clientId = "1a6tk1v013uc0vus57gg9ghoji";
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
    public  loginSession userLogin(String username,String password){
        loginSession loginSession = null;
        SessionInfo sessionInfo = sessionLogin(username,password);
        // The process of sessionLogin should either return a session ID (if the account has not been verified) or a
        // token ID (if the account has been verified).
        if (sessionInfo != null) {
            logger.info("SessionInfo Content");
            logger.info(sessionInfo.getSession());
            logger.info(sessionInfo.getAccessToken());
            logger.info(sessionInfo.getChallengeResult());
             userBasic info = getUserBasic(username);
             loginSession = new loginSession(info.getId(),sessionInfo.getAccessToken());

        }
        return loginSession;
    }

    /**
     *
     * @param username
     * @param password
     * @return SessionInfo
     * @throws AWSCognitoIdentityProviderException
     */
    private SessionInfo sessionLogin(String username,String password) throws AWSCognitoIdentityProviderException{
            SessionInfo info = null;
            AdminInitiateAuthRequest authRequest;
            AdminInitiateAuthResult authResult;
            try {
                HashMap<String, String> authParams = new HashMap<>();
                authParams.put("USERNAME", username);
                authParams.put("PASSWORD", password);
                 authRequest = new AdminInitiateAuthRequest()
                        .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                        .withUserPoolId(poolId)
                        .withClientId(clientId)
                        .withAuthParameters(authParams);
                authResult = identityProvider.adminInitiateAuth(authRequest);
            } catch (Exception e){
                authResult = null;
            }
            if (authResult != null) {
                final String session = authResult.getSession();
                String accessToken = null;
                AuthenticationResultType resultType = authResult.getAuthenticationResult();
                if (resultType != null) {
                    logger.info("access Token");
                    logger.info(resultType.getAccessToken());
                    logger.info("Id Token");
                    logger.info(resultType.getIdToken());
                    logger.info("Refresh Token");
                    logger.info(resultType.getRefreshToken());
                    logger.info("Token Type");
                    logger.info(resultType.getTokenType());
                    accessToken = resultType.getAccessToken();
                } else
                    logger.info("authResult is null");
                final String challengeResult = authResult.getChallengeName();
                info = new SessionInfo(session, accessToken, challengeResult );
            }
            return info;
    }
    /**
     *
     * @param userName
     * @return userBasic
     */

    public userBasic getUserBasic(String userName){
        AdminGetUserRequest userRequest = new AdminGetUserRequest()
                .withUsername(userName)
                .withUserPoolId(poolId);
        AdminGetUserResult userResult = identityProvider.adminGetUser(userRequest);
        List<AttributeType> userAttributes = userResult.getUserAttributes();
        final String rsltUserName = userResult.getUsername();
        String email = null;
        String Id = null;
        String nickname = null;
        for (AttributeType attr : userAttributes) {
            if (attr.getName().equals("nickname"))
                nickname = attr.getValue();
            if (attr.getName().equals("email"))
                email = attr.getValue();
            if (attr.getName().equals("custom:id"))
                Id = attr.getValue();
        }
        userBasic info = null;
        if (rsltUserName != null && email != null && Id != null&& nickname!=null) {
            info = new userBasic();
            info.setEmail(email);
            info.setUsername(rsltUserName);
            info.setId(Long.valueOf(Id));
            info.setNickname(nickname);
        }
        return info;
    }

    /**
     *
     * @param username
     * @param resetcode
     * @param newPassword
     * @return
     * @throws AWSCognitoIdentityProviderException
     */
    public boolean resetPassword(String username,String resetcode,String newPassword) throws AWSCognitoIdentityProviderException{
        try {
            ConfirmForgotPasswordRequest passwordRequest = new ConfirmForgotPasswordRequest()
                                                                .withUsername(username)
                                                                .withConfirmationCode(resetcode)
                                                                .withClientId(clientId)
                                                                .withPassword(newPassword);
            ConfirmForgotPasswordResult rslt = identityProvider.confirmForgotPassword(passwordRequest);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws AWSCognitoIdentityProviderException
     */
    public boolean changeFromTempPassword(String username,String oldPassword,String newPassword) throws AWSCognitoIdentityProviderException{
        final SessionInfo sessionInfo = sessionLogin(username,oldPassword);
        final String sessionString = sessionInfo.getSession();
        if (sessionString!=null&&sessionString.length()>0){
            HashMap<String,String> challengeResponses = new HashMap<>();
            challengeResponses.put("USERNAME",username);
            challengeResponses.put("PASSWORD",oldPassword);
            challengeResponses.put("NEW_PASSWORD",newPassword);
             try {
                 AdminRespondToAuthChallengeRequest changeRequest = new AdminRespondToAuthChallengeRequest()
                                                                    .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                                                                    .withChallengeResponses(challengeResponses)
                                                                    .withClientId(clientId)
                                                                    .withUserPoolId(poolId)
                                                                    .withSession(sessionString);
                 AdminRespondToAuthChallengeResult challengeResult = identityProvider.adminRespondToAuthChallenge(changeRequest);
                 return true;
             } catch (AWSCognitoIdentityProviderException e){
                logger.error(e.getErrorMessage());
                return false;
             }
        }
        return false;
    }
    public boolean isValidToken(String token){
        String aws_cognito_region = "us-east-1";
        RSAKeyProvider keyProvider = new AwsCognitoRsaKeyProvider(aws_cognito_region,poolId);
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            jwtVerifier.verify(token);
            logger.info("Verfiy Succeed");
            return true;
        } catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

    }

}


