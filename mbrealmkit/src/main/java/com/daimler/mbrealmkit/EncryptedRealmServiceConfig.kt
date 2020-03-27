package com.daimler.mbrealmkit

import android.content.Context
import android.content.SharedPreferences
import com.daimler.mbcommonkit.extensions.edit
import com.daimler.mbcommonkit.security.Crypto
import com.daimler.mbcommonkit.security.RandomStringGenerator
import com.daimler.mbloggerkit.MBLoggerKit
import io.realm.Realm
import io.realm.RealmConfiguration
import java.nio.charset.Charset

class EncryptedRealmServiceConfig internal constructor(
    context: Context,
    private val encryptionConfig: RealmServiceConfig.EncryptionConfig,
    realmSchemaConfig: SchemaConfig
) : RealmServiceConfig(context, realmSchemaConfig) {

    private val settingsName = "${context.packageName}$REALM_SETTINGS_SUFFIX"

    private val settingsKey = REALM_SETTINGS_KEY_ALIAS

    override fun initRealm(): Realm {
        MBLoggerKit.d("Encrypted realm with schema $realmSchema")
        Realm.init(context)
        val keyAlias = encryptionConfig.keyAlias
        val crypto = encryptionConfig.crypto
        if (crypto.keyExists(keyAlias).not()) {
            crypto.generateKey(keyAlias)
        }
        if (existsRealmEncryptionKeyInSettings(context).not()) {
            createRandomRealmEncryptionKeyInSettings(context, crypto, keyAlias)
        }
        val decryptedRealmKey = crypto.decrypt(keyAlias, getRealmEncryptionKeyFromSettings(context))
        return Realm.getInstance(
                RealmConfiguration.Builder().apply {
                    name(realmSchema.dbName)
                    schemaVersion(realmSchema.schemaVersion)
                    realmSchema.migration?.let { migration(it) } ?: deleteRealmIfMigrationNeeded()
                    encryptionKey(decryptedRealmKey.toByteArray(Charset.defaultCharset()))
                    modules(realmSchema.module)
                }.build()
        )
    }

    private fun createRandomRealmEncryptionKeyInSettings(
        context: Context,
        crypto: Crypto,
        keyAlias: String
    ) {
        val randomKey = RandomStringGenerator().generateString(KEY_LENGTH)
        val encryptedKey = crypto.encrypt(keyAlias, randomKey)
        realmSettings(context).edit(commit = true) {
            putString(settingsKey, encryptedKey)
        }
    }

    private fun existsRealmEncryptionKeyInSettings(context: Context): Boolean {
        return realmSettings(context).contains(settingsKey)
    }

    private fun getRealmEncryptionKeyFromSettings(context: Context): String {
        return realmSettings(context).getString(settingsKey, "")
    }

    private fun realmSettings(context: Context): SharedPreferences {
        return context.getSharedPreferences(settingsName, Context.MODE_PRIVATE)
    }

    private companion object {

        /**
         * A string with a length of 64 chars has a related length of 64 bytes which is required
         * by Realm as key for encryption
         */
        private const val KEY_LENGTH = 64

        private const val REALM_SETTINGS_SUFFIX = ".realmcache.settings"

        private const val REALM_SETTINGS_KEY_ALIAS = "realm.settings.key_alias"
    }
}