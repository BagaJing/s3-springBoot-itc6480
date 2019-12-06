# Exception:
### 1. Session Must Not be Null
Reason:
API GATEWAY cannot set cookie
### 2. Uploaded File corrupted
### 3. Read-Only System
**Reason**: only /tmp is writable in lambda function
**solution** : add prefix "/tmp/" before the filename, when temp file is needed to create
