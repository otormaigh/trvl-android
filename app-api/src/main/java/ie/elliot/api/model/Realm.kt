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
package ie.elliot.api.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmModule

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
@RealmModule(library = true, allClasses = true)
class ApiRealmModule

open class Airport(@PrimaryKey
                   var name: String = "",
                   var location: String = "",
                   var latitude: Double = 0.0,
                   var longitude: Double = 0.0,
        // TODO : Elliot -> Test data
                   var price: String = "€321.30",
        // TODO : Elliot -> Test data
                   var flightTime: String = "18h 13m") : RealmObject()