package com.citrobyte.rxcontacts

import androidx.annotation.Keep

@Keep
data class Contact (
    val contactId: Long
){
    var displayName:String? = null
    var firstName:String? = null
    var lastName:String? = null
    var middleName:String? = null
    var namePrefix:String? = null
    var nameSuffix:String? = null
    var phoneticMiddleName:String? = null
    var phoneticFirstName:String? = null
    var phoneticLastName:String? = null
    var nickName:String? = null
    var eventDates: ArrayList<EventDate>? = null
    var phones:ArrayList<Phone>? = null
    var emails:ArrayList<Email>? = null
    var messengers:ArrayList<IM>? = null
    var company: Company? = null
    var sip:String? = null
    var note:String? = null
    var isStarred:Boolean? = null
    var photoUri:String? = null
    var thumbnailUri:String? = null
    var websites:ArrayList<String>? = null

    override fun toString(): String {
        return "\n-------------------- contactId = $contactId -----------------------\n" +
                "displayName = $displayName\n" +
                "firstName = $firstName\n" +
                "lastName = $lastName\n" +
                "middleName = $middleName\n" +
                "namePrefix = $namePrefix\n"+
                "nameSuffix = $nameSuffix\n" +
                "phoneticFirstName = $phoneticFirstName\n" +
                "phoneticLastName = $phoneticLastName\n" +
                "phoneticMiddleName = $phoneticMiddleName\n" +
                "nickName = $nickName\n" +
                "phones = $phones\n" +
                "emails = $emails\n" +
                "eventDates = $eventDates\n" +
                "messengers = $messengers\n" +
                "company = $company\n" +
                "sip = $sip\n" +
                "note = $note\n" +
                "isStarred = $isStarred\n" +
                "photoUri = $photoUri\n" +
                "thumbnailUri = $thumbnailUri\n" +
                "websites = $websites\n"

    }
}

@Keep
data class Phone(
    val phoneNumber:String?,
    val phoneType:Int?,
    val customTypeName:String? = null
)

@Keep
data class Email(
    val emailAddress:String?,
    val emailType:Int?,
    val customTypeName:String? = null
)

@Keep
data class EventDate(
    val date:String?,
    val dateType:Int?,
    val customTypeName:String? = null
)

@Keep
data class IM(
    val im:String?,
    val imProtocol:Int?,
    val customProtocolName:String? = null
)

@Keep
data class Company(
    val company:String? = null,
    val companyTitle:String? = null
)