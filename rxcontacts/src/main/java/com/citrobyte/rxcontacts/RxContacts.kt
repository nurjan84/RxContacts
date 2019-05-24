package com.citrobyte.rxcontacts

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract

import io.reactivex.Observable

class RxContacts (private val contentResolver : ContentResolver){

    private var contactsSortingOrder:String? = null

    fun sortById(): RxContacts {
        contactsSortingOrder = ContactsContract.Contacts._ID
        return this
    }

    fun sortByDisplayName(): RxContacts {
        contactsSortingOrder = ContactsContract.Contacts.DISPLAY_NAME
        return this
    }

    fun getAllContacts(like: String?): Observable<ArrayList<Contact>> {
        val contactCursor = createContactsCursor(null, null, like) ?: return Observable.empty()
        return readContacts(contactCursor, like)
    }

    fun getContacts(limit:Int, offset:Int, like: String?): Observable<ArrayList<Contact>> {
        val contactCursor = createContactsCursor(limit, offset, like) ?: return Observable.empty()
        return readContacts(contactCursor, like)
    }

    private fun readContacts(contactCursor:Cursor, like: String?): Observable<ArrayList<Contact>>{

        val listOfContacts = ArrayList<Contact>()

        val idxId = contactCursor.getColumnIndex(ContactsContract.Contacts._ID)
        val idxDisplayName = contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val idxPhotoUri = contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
        val idxThumbnailUri = contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)


        while (contactCursor.moveToNext()){

            val contact = Contact(contactCursor.getLong(idxId))
            contact.displayName = contactCursor.getString(idxDisplayName)
            contact.photoUri = contactCursor.getString(idxPhotoUri)
            contact.thumbnailUri = contactCursor.getString(idxThumbnailUri)

            var dataCursor : Cursor? = null
            if(like != null && isNumber(like)){
                val searchCursor = createSearchContactsDataCursor(contact.contactId, like)
                if(searchCursor != null && searchCursor.count > 0){
                    dataCursor = createContactsDataCursor(contact.contactId)
                }
                searchCursor?.close()
            }else{
                dataCursor = createContactsDataCursor(contact.contactId)
            }

            if(dataCursor != null){
                val idxMimeType = dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
                val idxFirstName = dataCursor.getColumnIndex(ContactsContract.Data.DATA2)
                val idxLastName = dataCursor.getColumnIndex(ContactsContract.Data.DATA3)
                val idxMiddleName = dataCursor.getColumnIndex(ContactsContract.Data.DATA5)
                val idxNamePrefix = dataCursor.getColumnIndex(ContactsContract.Data.DATA4)
                val idxNameSuffix = dataCursor.getColumnIndex(ContactsContract.Data.DATA6)
                val idxPhoneticFirstName = dataCursor.getColumnIndex(ContactsContract.Data.DATA7)
                val idxPhoneticLastName = dataCursor.getColumnIndex(ContactsContract.Data.DATA9)
                val idxPhoneticMiddleName = dataCursor.getColumnIndex(ContactsContract.Data.DATA8)
                val idxNickName = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxNote = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxPhoneNumber = dataCursor.getColumnIndex(ContactsContract.Data.DATA4)
                val idxPhoneNumber2 = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxPhoneNumberType = dataCursor.getColumnIndex(ContactsContract.Data.DATA2)
                val idxPhoneNumberCustomType = dataCursor.getColumnIndex(ContactsContract.Data.DATA3)
                val idxEmail = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxEmailType = dataCursor.getColumnIndex(ContactsContract.Data.DATA2)
                val idxEmailCustomType = dataCursor.getColumnIndex(ContactsContract.Data.DATA3)
                val idxEventDate = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxEventType = dataCursor.getColumnIndex(ContactsContract.Data.DATA2)
                val idxEventCustomType = dataCursor.getColumnIndex(ContactsContract.Data.DATA3)
                val idxIm = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxImProtocol = dataCursor.getColumnIndex(ContactsContract.Data.DATA2)
                val idxImCustomProtocol = dataCursor.getColumnIndex(ContactsContract.Data.DATA3)
                val idxCompany = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxCompanyTitle = dataCursor.getColumnIndex(ContactsContract.Data.DATA4)
                val idxSIP = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxWebsite = dataCursor.getColumnIndex(ContactsContract.Data.DATA1)
                val idxStarred = dataCursor.getColumnIndex(ContactsContract.Data.STARRED)

                while(dataCursor.moveToNext()){
                    contact.isStarred = dataCursor.getInt(idxStarred) != 0
                    when(dataCursor.getString(idxMimeType)){
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE -> {
                            contact.firstName = dataCursor.getString(idxFirstName)
                            contact.lastName = dataCursor.getString(idxLastName)
                            contact.middleName = dataCursor.getString(idxMiddleName)
                            contact.namePrefix = dataCursor.getString(idxNamePrefix)
                            contact.nameSuffix = dataCursor.getString(idxNameSuffix)
                            contact.phoneticFirstName = dataCursor.getString(idxPhoneticFirstName)
                            contact.phoneticLastName = dataCursor.getString(idxPhoneticLastName)
                            contact.phoneticMiddleName = dataCursor.getString(idxPhoneticMiddleName)
                        }
                        ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE ->{
                            contact.nickName = dataCursor.getString(idxNickName)
                        }
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE ->{
                            contact.note = dataCursor.getString(idxNote)
                        }
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE ->{
                            if(contact.phones == null){contact.phones = ArrayList()}
                            var phoneNumber = dataCursor.getString(idxPhoneNumber)
                            if(phoneNumber.isNullOrEmpty()){phoneNumber = dataCursor.getString(idxPhoneNumber2).replace(" ","")}

                            val phone = Phone(
                                phoneNumber,
                                dataCursor.getInt(idxPhoneNumberType),
                                dataCursor.getString(idxPhoneNumberCustomType)
                            )
                            contact.phones?.add(phone)
                        }
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ->{
                            if(contact.emails == null){contact.emails = ArrayList()}
                            val email = Email(
                                dataCursor.getString(idxEmail),
                                dataCursor.getInt(idxEmailType),
                                dataCursor.getString(idxEmailCustomType)
                            )
                            contact.emails?.add(email)
                        }
                        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE ->{
                            if(contact.eventDates == null){contact.eventDates = ArrayList()}
                            val event = EventDate(
                                dataCursor.getString(idxEventDate),
                                dataCursor.getInt(idxEventType),
                                dataCursor.getString(idxEventCustomType)
                            )
                            contact.eventDates?.add(event)
                        }
                        ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE ->{
                            if(contact.messengers == null){contact.messengers = ArrayList()}
                            val im = IM(
                                dataCursor.getString(idxIm),
                                dataCursor.getInt(idxImProtocol),
                                dataCursor.getString(idxImCustomProtocol)
                            )
                            contact.messengers?.add(im)
                        }
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE ->{
                            val company = Company(
                                dataCursor.getString(idxCompany),
                                dataCursor.getString(idxCompanyTitle)
                            )
                            contact.company = company
                        }
                        ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE ->{
                            contact.sip = dataCursor.getString(idxSIP)
                        }
                        ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE ->{
                            if(contact.websites == null){contact.websites = ArrayList()}
                            contact.websites?.add(dataCursor.getString(idxWebsite))
                        }
                    }
                }
                listOfContacts.add(contact)
            }

        }

        contactCursor.close()

        return Observable.fromArray(listOfContacts)
    }

    private fun createContactsCursor(limit:Int?, offset:Int?, like:String?): Cursor? {
        val projection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.PHOTO_URI,
        ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )

        var selection :String? = null
        var selectionArg : Array<String>? = null
        if(like != null && !isNumber(like)){
            selection =  ContactsContract.Contacts.DISPLAY_NAME +" LIKE ?"
            selectionArg = arrayOf("%$like%")
        }


        var limitAndOffset = ""
        if(limit != null && offset != null){
            limitAndOffset =  " LIMIT $limit OFFSET $offset"
        }

        return contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            selection,
            selectionArg,
            contactsSortingOrder + limitAndOffset
        )
    }

    private fun createSearchContactsDataCursor(contactId:Long, like:String?):Cursor?{
        val projection = arrayOf(
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DATA4,
            ContactsContract.Data.MIMETYPE
        )

        val selection :String? = ContactsContract.Data.CONTACT_ID +" = ?" + " AND " + ContactsContract.Data.MIMETYPE +" = ?"+ " AND " + ContactsContract.Data.DATA4 + " LIKE ?"
        val selectionArg : Array<String>? = arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, "%$like%")

        return contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArg,
            null
        )
    }

    private fun createContactsDataCursor(contactId:Long):Cursor?{
        val projection = arrayOf(
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.STARRED,
            ContactsContract.Data.DATA1,
            ContactsContract.Data.DATA2,
            ContactsContract.Data.DATA3,
            ContactsContract.Data.DATA4,
            ContactsContract.Data.DATA5,
            ContactsContract.Data.DATA6,
            ContactsContract.Data.DATA7,
            ContactsContract.Data.DATA8,
            ContactsContract.Data.DATA9,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.IN_VISIBLE_GROUP
        )

        val selection :String? = ContactsContract.Data.CONTACT_ID +" = ?"
        val selectionArg : Array<String>? = arrayOf(contactId.toString())

        return contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArg,
            null
        )
    }

    private fun isNumber(c:CharSequence) = run { c.matches("-?\\d+(\\.\\d+)?".toRegex()) }

}