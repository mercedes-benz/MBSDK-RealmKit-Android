package com.daimler.mbrealmkit

internal class RealmProvider(realmInitializable: RealmInitializable) {

    internal val realm by lazy { realmInitializable.initRealm() }
}