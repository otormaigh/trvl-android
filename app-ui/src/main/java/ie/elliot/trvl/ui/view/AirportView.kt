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
package ie.elliot.trvl.ui.view

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import ie.elliot.api.model.Airport
import ie.elliot.trvl.ui.R
import kotlinx.android.synthetic.main.airport_item_view.view.*

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
class AirportView : LinearLayout {
    var airport: Airport = Airport()
        get() = airport
        set(airport) {
            field = airport
            showHint = false
            tvLocation.text = airport.name
            tvAirport.text = airport.iata
        }

    @StringRes
    var hint: Int = 0
        get() = hint
        set(hint) {
            field = hint
            showHint = true
            tvHint.setText(hint)
        }

    private var showHint: Boolean = true
        get() = showHint
        set(showHint) {
            field = showHint
            if (showHint) {
                llHint.visibility = View.VISIBLE
                llAirport.visibility = View.GONE
            } else {
                llHint.visibility = View.GONE
                llAirport.visibility = View.VISIBLE
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        inflate(getContext(), R.layout.airport_item_view, this)

        val attributes = context?.theme?.obtainStyledAttributes(attrs, R.styleable.AirportView, defStyleAttr, defStyleRes)
        if (attributes != null) {
            val hintResId = attributes.getResourceId(R.styleable.AirportView_hint, 0)
            if (hintResId != 0) {
                hint = hintResId
            }
        }

        if (isInEditMode) {
            llAirport.visibility = View.VISIBLE
            tvLocation.text = "New York"
            tvAirport.text = "JFK"
        }
    }
}