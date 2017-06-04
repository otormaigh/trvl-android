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
package ie.elliot.api.Extensions

import android.content.Context
import android.support.annotation.RawRes
import java.nio.charset.StandardCharsets

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */

fun Context.rawJsonToString(@RawRes resourceId: Int): String {
    resources.openRawResource(resourceId).use {
        val size = it.available()
        val buffer = ByteArray(size)
        it.read(buffer)
        it.close()

        return String(buffer, StandardCharsets.UTF_8)
    }
}