package com.daimler.mbrealmkit.sample

import io.realm.annotations.RealmModule

@RealmModule(library = true, classes = [RealmUser::class])
class RealmModule