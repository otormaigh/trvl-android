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

package ie.elliot.api

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import ie.elliot.api.Extensions.rawJsonToString
import ie.elliot.api.model.Airport
import io.realm.Realm

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
object ApiClient {
    private val moshi = Moshi.Builder().build()

    fun loadTestData(context: Context) {
        val json = context.rawJsonToString(R.raw.test_airports)
        val type = Types.newParameterizedType(List::class.java, Airport::class.java)
        val adapter = moshi.adapter<List<Airport>>(type)
        val airports = adapter.fromJson(json)

        Realm.getDefaultInstance().run {
            executeTransaction {
                copyToRealmOrUpdate(airports)
            }
        }
    }
}