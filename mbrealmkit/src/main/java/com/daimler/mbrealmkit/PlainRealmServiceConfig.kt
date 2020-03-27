package com.daimler.mbrealmkit

import android.content.Context
import com.daimler.mbloggerkit.MBLoggerKit
import io.realm.Realm
import io.realm.RealmConfiguration

class PlainRealmServiceConfig internal constructor(
    context: Context,
    realmSchema: SchemaConfig
) : RealmServiceConfig(context, realmSchema) {

    override fun initRealm(): Realm {
        MBLoggerKit.d("Realm with schema $realmSchema")
        Realm.init(context)
        return Realm.getInstance(
                RealmConfiguration.Builder().apply {
                    name(realmSchema.dbName)
                    schemaVersion(realmSchema.schemaVersion)
                    realmSchema.migration?.let { migration(it) } ?: deleteRealmIfMigrationNeeded()
                    modules(realmSchema.module)
                }.build()
        )
    }
}