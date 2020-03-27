package com.daimler.mbrealmkit.sample

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmUser : RealmObject() {

    @PrimaryKey
    var id: String = ""

    var name: String? = null

    companion object {
        const val FIELD_ID = "id"
    }
}