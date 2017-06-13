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
package ie.elliot.trvl.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal open class TrvlActivity<P: TrvlPresenter>(protected val presenter: P?) : AppCompatActivity() {
    constructor(): this(null)

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    protected fun addFrag(fragment: Fragment, addToStack: Boolean = true) {
        val tag = fragment.javaClass.simpleName
        val transaction: FragmentTransaction = supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, tag)

        if (addToStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    protected fun replaceFrag(fragment: Fragment, addToStack: Boolean = true) {
        val tag = fragment.javaClass.simpleName
        val transaction: FragmentTransaction = supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, fragment, tag)

        if (addToStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }
}