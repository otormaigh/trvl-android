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

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Elliot Tormey
 * @since 15/06/2017
 */

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class DateTime

internal class DateTimeAdapter {
    private val serverDateTime = "yyyy-MM-dd'T'HH:mm:ssZ"

    @FromJson @DateTime
    fun fromJson(dateTime: String): Long {
        return dateTime.toDateLong(serverDateTime)
    }

    @ToJson
    fun toJson(@DateTime dateTime: Long): String {
        return dateTime.toDateStr(serverDateTime)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class Date

internal class DateAdapter {
    private val serverDate = "yyyy-MM-dd"

    @FromJson @Date
    fun fromJson(date: String): Long {
        return date.toDateLong(serverDate)
    }

    @ToJson
    fun toJson(@Date date: Long): String {
        return date.toDateStr(serverDate)
    }
}

private fun String.toDateLong(format: String): Long {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(this).time
}

private fun Long.toDateStr(format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(this)
}