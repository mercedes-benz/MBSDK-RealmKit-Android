package com.daimler.mbrealmkit

import com.daimler.mbloggerkit.MBLoggerKit
import io.realm.Realm

/**
 * Service singleton that provides accessors to created realm instances.
 */
object MBRealmKit {

    private val realmProviders = HashMap<String, RealmProvider>()

    /**
     * Creates a new realm instance. The given id must be unique.
     *
     * @throws IllegalArgumentException if there is already a realm instance with the given id
     */
    fun createRealmInstance(id: String, realmServiceConfig: RealmServiceConfig) {
        if (realmProviders.containsKey(id).not()) {
            MBLoggerKit.d("Create provider with id $id.")
            realmProviders[id] = RealmProvider(realmServiceConfig)
        } else {
            MBLoggerKit.d("Skipped registration of provider $id - $realmServiceConfig because already registered")
        }
    }

    /**
     * Returns the non-encrypted realm instance for the given id.
     *
     * @throws IllegalArgumentException if there is no non-encrypted realm instance for the given id
     */
    fun realm(id: String): Realm = getRealm(id)

    private fun getRealm(id: String): Realm =
            realmProviders[id]?.let {
                getRealmInstance(it)
                    ?: throw IllegalArgumentException("Realm was not initialized.")
            } ?: throw IllegalArgumentException("Provider with id $id was not created.")

    private fun getRealmInstance(provider: RealmProvider): Realm? = provider.realm
}