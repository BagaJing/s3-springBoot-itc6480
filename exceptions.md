# Exception:
### 1.Session Must Not be Null
**Reason:** no cookie found in api gateway
### 2.Uploaded File corrupted
https://stackoverflow.com/questions/52725317/aws-api-gateway-as-a-http-proxy-is-currupting-binary-uploaded-image-files
### 3. Downloaded File corrupted
**Reason:** unknown
### 4.Read-Only System
**Reason**: only /tmp is writable in lambda function
**solution** : add prefix "/tmp/" before the filename, when temp file is needed to create
### 5.TransferManager.uploadFileList() String Index out of Bound
Reason: unknown
Solution: replace by putObject()
### 6. Cognito: "Error: Unable to verify secret hash for client 4b*******fd".
https://stackoverflow.com/questions/37438879/unable-to-verify-secret-hash-for-client-in-amazon-cognito-userpools
### 7. Fail to load static resources:
https://medium.com/@jstubblefield7939/serving-static-files-with-amazon-cloudfront-cdn-ccec91a985cd