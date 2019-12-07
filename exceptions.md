# Exception:
### 1.Session Must Not be Null
**Reason:** API GATEWAY cannot set cookie
### 2.Uploaded File corrupted
    Reason: unknown
### 3.Read-Only System
    **Reason**: only /tmp is writable in lambda function
**solution** : add prefix "/tmp/" before the filename, when temp file is needed to create
### 4.TransferManager.uploadFileList() String Index out of Bound
    Reason: unknown
    Solution: replace by putObject()
### 5. Cognito: "Error: Unable to verify secret hash for client 4b*******fd".
https://stackoverflow.com/questions/37438879/unable-to-verify-secret-hash-for-client-in-amazon-cognito-userpools