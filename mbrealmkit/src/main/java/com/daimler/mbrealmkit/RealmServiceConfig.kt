package com.daimler.mbrealmkit

import android.content.Context
import com.daimler.mbcommonkit.security.Crypto
import io.realm.RealmMigration
import io.realm.annotations.RealmModule

abstract class RealmServiceConfig internal constructor(
    val context: Context,
    internal val realmSchema: SchemaConfig
) : RealmInitializable {

    /**
     * This is used to created a config, required to instantiate a [RealmProvider].
     *
     * @param schemaVersion the schema version
     * @param module the realm module, needs to be annotated with [RealmModule]
     */
    class Builder(private val context: Context, private val schemaVersion: Long, private val module: Any) {

        private var keyAlias = "${context.packageName}.realmcache.alias"

        private var encrypt = false

        private var dbName: String? = null

        private var migration: RealmMigration? = null

        /**
         * Use a different name than [DEFAULT_DB_NAME]
         *
         * @param dbName name of the database
         */
        fun useDbName(dbName: String): Builder {
            this.dbName = dbName
            return this
        }

        /**
         * @param migration a custom migration; the database will be deleted and re-created
         */
        fun migrate(migration: RealmMigration): Builder {
            this.migration = migration
            return this
        }

        /**
         * Sets a custom key alias.
         *
         * @param keyAlias the alias for the key created and stored in the Android key store
         */
        fun encrypt(keyAlias: String? = null): Builder {
            this.encrypt = true
            keyAlias?.let {
                this.keyAlias = it
            }
            return this
        }

        fun build(): RealmServiceConfig =
                if (encrypt) {
                    EncryptedRealmServiceConfig(context, encryptionConfig(), schemaConfig())
                } else {
                    PlainRealmServiceConfig(context, schemaConfig())
                }

        private fun schemaConfig(): SchemaConfig {
            val databaseName = dbName?.let {
                it
            } ?: defaultDbName()
            return SchemaConfig(schemaVersion, databaseName, module, migration)
        }

        private fun defaultDbName(): String = if (encrypt) DEFAULT_DB_NAME_ENCRYPTED else DEFAULT_DB_NAME

        private fun encryptionConfig(): EncryptionConfig {

            return EncryptionConfig(keyAlias, Crypto(context, false))
        }

        private companion object {
            private const val DEFAULT_DB_NAME = "realm_table"
            private const val DEFAULT_DB_NAME_ENCRYPTED = "realm_table_encrypted"
        }
    }

    internal data class SchemaConfig(
        val schemaVersion: Long,
        val dbName: String,
        val module: Any,
        val migration: RealmMigration? = null
    )

    internal data class EncryptionConfig(
        val keyAlias: String,
        val crypto: Crypto
    )
}