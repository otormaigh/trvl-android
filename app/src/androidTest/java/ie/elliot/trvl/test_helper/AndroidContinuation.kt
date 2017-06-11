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

package ie.elliot.trvl.test_helper

import android.os.Handler
import android.os.Looper
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor

/**
 * https://stackoverflow.com/questions/42561217/how-to-use-kotlin-coroutines-await-on-main-thread
 *
 * @author Elliot Tormey
 * @since 11/06/2017
 */
internal class AndroidContinuation<T>(val cont: Continuation<T>) : Continuation<T> by cont {
    override fun resume(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) cont.resume(value)
        else Handler(Looper.getMainLooper()).post { cont.resume(value) }
    }

    override fun resumeWithException(exception: Throwable) {
        if (Looper.myLooper() == Looper.getMainLooper()) cont.resumeWithException(exception)
        else Handler(Looper.getMainLooper()).post { cont.resumeWithException(exception) }
    }
}

object Android : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> = AndroidContinuation(continuation)
}