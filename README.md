# RxContacts
Rx Contacts Library

[![](https://jitpack.io/v/nurjan84/RxContacts.svg)](https://jitpack.io/#nurjan84/RxContacts)

Reading almost all fields of the contacts. You can set limit and offset to make pagination. 

don't forget ask for permission

```
<uses-permission android:name="android.permission.READ_CONTACTS"/>
```


```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```
dependencies {
	implementation 'com.github.nurjan84:RxContacts:0.1.0'
}
```
