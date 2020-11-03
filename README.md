# sms_retriever_neo

This is a fork and rewrite of the sms_retriever plugin, adding androidx compatibility and modifications to the codebase.


This plugin allows the user retrieve the SMS on Android using SMS Retrieval API

## Getting Started
To retrieve a app signature. The app signature generated should be in the body of the sms sent to the device.
```dart
String appSignature = await SmsRetrieverNeo.getAppSignature();
```
To start listening for an incoming SMS
```dart
await SmsRetrieverNeo.startListening(
    onSmsReceived: (sms) {
        // this is fired when the sms is received
        print(sms)
    },
    onTimeout: () {
        // this is fired in 5 minutes after we start listening for sms events
        // you may restart ths listener here
        SmsRetrieverNeo.startListening();
    }
);
```
Stop listening after getting the SMS
```dart
SmsRetrieverNeo.stopListening();
```

Generate appSignature for keystore file
````dart in html
keytool -storepass storepass -alias alias -exportcert -keystore file | xxd -p | tr -d "[:space:]" | xxd -r -p | base64 | cut -c1-11

````

Example SMS

[#] Your example code is:
123456
appSignature