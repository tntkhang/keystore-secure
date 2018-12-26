# Keystore Secure

This is an lightweight library for save and get data secure into the Keystore which encrypted and decrypted.

![screenshot](screenshot.png)


[ ![Download](https://api.bintray.com/packages/tntkhang/maven/keystore-secure/images/download.svg?version=1.1.1) ](https://bintray.com/tntkhang/maven/keystore-secure/1.1.1/link)

[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-keystore--secure-green.svg?style=flat )]( https://android-arsenal.com/details/1/7404 )

### Add implementation into build.gradle of app level

```
implementation 'com.github.tntkhang:keystore-secure:1.1.1'
```
### How to use it

Call `init` before using

```
KeystoreSecure.init(context);
```

Encrypt string by:

```
KeystoreSecure.encrypt(context, STORE_KEY_1, valueToEncrypt);
```

Decrypt string by:

```
KeystoreSecure.decrypt(STORE_KEY_1);
```


### Checkout example for more detail.
