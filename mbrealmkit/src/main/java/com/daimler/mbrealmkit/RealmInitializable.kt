package com.daimler.mbrealmkit

import io.realm.Realm

interface RealmInitializable {
    fun initRealm(): Realm
}