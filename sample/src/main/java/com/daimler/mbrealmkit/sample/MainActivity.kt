package com.daimler.mbrealmkit.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.daimler.mbloggerkit.MBLoggerKit
import com.daimler.mbrealmkit.MBRealmKit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val realmUserCache =
            RealmUserCache(MBRealmKit.realm(RealmProviderManager.PROVIDER_ID))
    private val encryptedRealmUserCache =
            RealmUserCache(MBRealmKit.realm(RealmProviderManager.ENCRYPTED_PROVIDER_ID))

    private var id: String = ""
    private var name: String = ""
    private var encrypted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edit_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                id = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })
        edit_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                name = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })
        check_encrypted.setOnCheckedChangeListener { _, isChecked -> encrypted = isChecked }
        btn_save.setOnClickListener {
            val id = id
            if (id.isNotBlank()) {
                val user = User(id, name)
                if (encrypted) {
                    encryptedRealmUserCache.saveUser(user)
                } else {
                    realmUserCache.saveUser(user)
                }
            }
        }
        btn_load.setOnClickListener {
            val id = id
            if (id.isNotBlank()) {
                val user = if (encrypted) encryptedRealmUserCache.loadUserById(id) else realmUserCache.loadUserById(id)
                MBLoggerKit.d("Loaded user: $user.")
            } else {
                val users = if (encrypted) encryptedRealmUserCache.loadAll() else realmUserCache.loadAll()
                MBLoggerKit.d("Loaded users: $users.")
            }
        }
    }
}
