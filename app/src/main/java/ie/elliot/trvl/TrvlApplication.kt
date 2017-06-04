/*
 * Copyright (C) 2017 Elliot Tormey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.elliot.trvl

import android.app.Application
import ie.elliot.trvl.model.TrvlRealmMigration
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class TrvlApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
                .name("trvl.realm")
                .schemaVersion(0)
                .migration(TrvlRealmMigration())

        if (BuildConfig.DEBUG) {
            configuration.deleteRealmIfMigrationNeeded()
        }
        configuration.build()
    }
}