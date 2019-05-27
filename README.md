# Android RxContacts
Rx Contacts Library

[![](https://jitpack.io/v/nurjan84/RxContacts.svg)](https://jitpack.io/#nurjan84/RxContacts)

Reading almost all fields of the contacts. You can set limit and offset to make pagination. 

don't forget to ask for permission

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
	implementation 'com.github.nurjan84:RxContacts:0.2.5'
}
```


Example:
```
  private fun getRxContacts(){
        disposables.add(
            RxContacts(contentResolver)
                .sortByDisplayName()
                //.getContacts(limit, offset, "search by phone number or display name")
                .getAllContacts("search by phone number or display name")  //set null to skip searching
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {result -> run {
                        println("getRxContacts ok $result")
                    }},
                    { e -> run{
                        println("getRxContacts error $e")
                    }}
                )
        )
    }
```


Available data :
```
var displayName:String? 
var firstName:String? 
var lastName:String? 
var middleName:String?
var namePrefix:String? 
var nameSuffix:String? 
var phoneticMiddleName:String? 
var phoneticFirstName:String? 
var phoneticLastName:String?
var nickName:String? 
var eventDates: ArrayList<EventDate>? 
var phones:ArrayList<Phone>? 
var emails:ArrayList<Email>? 
var messengers:ArrayList<IM>? 
var company: Company?
var sip:String?
var note:String?
var isStarred:Boolean?
var photoUri:String?
var thumbnailUri:String?
var websites:ArrayList<String>?
```

Photo of the contact stored as blob, so you can get it as byte array or file:
```
//blob will be saved in cache directory
private fun getPhotoFile(){
        val newImagePath = "contact_photo.jpg"
        val file = RxContactsUtils(contentResolver).getContactPhotoFile(CONTACT_ID,  File( cacheDir, newImagePath))
        println("getPhotoFile = ${file?.absolutePath}")
    }

private fun getPhotoByteArray(){
	val bytes = RxContactsUtils(contentResolver).getContactPhotoByteArray(CONTACT_ID)
	println("getPhotoBytes = ${bytes?.size}")
}
```
