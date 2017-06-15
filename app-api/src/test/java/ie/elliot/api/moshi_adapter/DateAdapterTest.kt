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

package ie.elliot.api.moshi_adapter

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Elliot Tormey
 * @since 15/06/2017
 */
internal class DateAdapterTest {
    private val dateTimeAdapter = DateTimeAdapter()
    private val dateAdapter = DateAdapter()

    @Test
    fun testDateTimeToJson() {
        val result = dateTimeAdapter.toJson(1497565435000)
        assertEquals("2017-06-15T22:23:55+0000", result)
    }

    @Test
    fun testDateTimeFromJson() {
        val result = dateTimeAdapter.fromJson("2017-06-15T23:23:55+0100")
        assertEquals(1497565435000, result)
    }

    @Test
    fun testDateTimeToFrom() {
        val toJson = dateTimeAdapter.toJson(1497565435000)
        assertEquals("2017-06-15T22:23:55+0000", toJson)

        val fromJson = dateTimeAdapter.fromJson(toJson)
        assertEquals(fromJson, 1497565435000)
    }

    @Test
    fun testDateTimeFromTo() {
        val fromJson = dateTimeAdapter.fromJson("2017-06-15T23:23:55+0100")
        assertEquals(1497565435000, fromJson)

        val toJson = dateTimeAdapter.toJson(fromJson)
        assertEquals(toJson, "2017-06-15T22:23:55+0000")
    }

    @Test
    fun testDateToJson() {
        val result = dateAdapter.toJson(1497565435000)
        assertEquals("2017-06-15", result)
    }

    @Test
    fun testDateFromJson() {
        val result = dateAdapter.fromJson("2017-06-15")
        assertEquals(1497484800000, result)
    }

    @Test
    fun testDateToFrom() {
        val toJson = dateAdapter.toJson(1497565435000)
        assertEquals("2017-06-15", toJson)

        val fromJson = dateAdapter.fromJson(toJson)
        assertEquals(1497484800000, fromJson)
    }

    @Test
    fun testDateFromTo() {
        val fromJson = dateAdapter.fromJson("2017-06-15")
        assertEquals(1497484800000, fromJson)

        val toJson = dateAdapter.toJson(fromJson)
        assertEquals("2017-06-15", toJson)
    }
}