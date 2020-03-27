package com.daimler.mbrealmkit.sample

interface UserCache {

    fun saveUser(user: User)

    fun loadAll(): List<User>

    fun loadUserById(id: String): User?
}