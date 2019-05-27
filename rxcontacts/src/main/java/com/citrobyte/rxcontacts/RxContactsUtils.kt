package com.citrobyte.rxcontacts

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.ContactsContract
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RxContactsUtils(private val contentResolver : ContentResolver){

    fun getContactPhotoFile(contactId: Long, file: File): File? {
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        return try {
            val photoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, contactUri)?: return null
            val buffer = ByteArray(photoInputStream.available())
            photoInputStream.read(buffer)
            val outputStream = FileOutputStream(file)
            outputStream.write(buffer)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    fun getContactPhotoByteArray(contactId: Long): ByteArray? {
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        return try {
            val photoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, contactUri)?: return null
             ByteArray(photoInputStream.available())
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}