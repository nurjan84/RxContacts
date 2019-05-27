package com.citrobyte.contacts

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.citrobyte.rxcontacts.RxContacts
import com.citrobyte.rxcontacts.RxContactsUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(){

    private val disposables = CompositeDisposable()

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            checkContactsPermission()
        }
    }

    private fun checkContactsPermission(){
        disposables.add(
            RxPermissions(this).
                request(Manifest.permission.READ_CONTACTS)
                .subscribe { granted ->
                    if(granted){
                        getRxContacts()
                    }
                }
        )
    }

    private fun getRxContacts(){
        disposables.add(
            RxContacts(contentResolver)
                .sortByDisplayName()
                //.getContacts(limit, offset, "search by phone number or display name")
                .getAllContacts(null)  //set null to skip searching
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

    private fun getPhotoFile(){
        val newImagePath = "contact_photo.jpg"
        val file = RxContactsUtils(contentResolver).getContactPhotoFile(23,  File( cacheDir, newImagePath))
        println("getPhotoFile = ${file?.absolutePath}")
    }

    private fun getPhotoByteArray(){
        val bytes = RxContactsUtils(contentResolver).getContactPhotoByteArray(23)
        println("getPhotoBytes = ${bytes?.size}")
    }

}
