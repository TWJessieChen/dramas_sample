package com.example.dramas_sample.database.module

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmFileException

object JCRealmConfiguration {

    private val CURRENT_DRAMAS_SCHEMA_VERSION: Long = 1

    private val dramasConfig = RealmConfiguration.Builder()
        .name("dramas.realm")
        .schemaVersion(CURRENT_DRAMAS_SCHEMA_VERSION)
        .deleteRealmIfMigrationNeeded()
        .build()


    fun getDramasInstance(): Realm? {
        return try {
            Realm.getInstance(dramasConfig)
        } catch (ex: RealmFileException) {
            deleteDramasRealm()
            Realm.getInstance(dramasConfig)
        }
    }

    fun deleteDramasRealm() {
        Realm.deleteRealm(dramasConfig)
    }
}