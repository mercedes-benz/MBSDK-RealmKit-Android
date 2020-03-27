package com.daimler.mbrealmkit.sample

import com.daimler.mbloggerkit.MBLoggerKit
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class RealmUserCache(private val realm: Realm) : UserCache {

    override fun saveUser(user: User) {
        realm.executeTransaction {
            var realmUser = queryUserById(user.id)
            if (realmUser == null) {
                MBLoggerKit.d("Create user $user.")
                realmUser = realm.createObject(user.id)
            } else {
                MBLoggerKit.d("Update user $user.")
            }
            realmUser.name = user.name
            realm.copyToRealmOrUpdate(realmUser)
        }
    }

    override fun loadAll(): List<User> {
        val realmUsers = realm.where<RealmUser>().findAll()
        return realmUsers.map { mapRealmUserToUser(it) }
    }

    override fun loadUserById(id: String): User? {
        val realmUser = queryUserById(id)
        return realmUser?.let { mapRealmUserToUser(it) }
    }

    private fun queryUserById(id: String): RealmUser? =
            realm.where<RealmUser>().equalTo(RealmUser.FIELD_ID, id).findFirst()

    private fun mapRealmUserToUser(realmUser: RealmUser)=
            User(realmUser.id, realmUser.name ?: "")
}