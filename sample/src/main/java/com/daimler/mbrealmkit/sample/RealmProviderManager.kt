package com.daimler.mbrealmkit.sample

import java.util.*

object RealmProviderManager {

    val PROVIDER_ID: String by lazy { UUID.randomUUID().toString() }

    val ENCRYPTED_PROVIDER_ID: String by lazy { UUID.randomUUID().toString() }
}