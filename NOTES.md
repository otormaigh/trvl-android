* Calculate distance to airport lat/long
* Transition between HomeActivity and AirportSearchFragment
* Filterable Realm RecyclerView in AirportSearchAdapter
* Activity/Fragment transition animation
* Take picture of passport and fill PassengerView

## FlightResults
Activity make call to 'ApiIntentService' with some parameter to make an API call. Data gets returned
and saved to Realm? 'FlightResultAdapter' is on autoUpdate so once a change is made base on the realm query
its listening to it will update. But how do we make sure 'FlightResults' saved in Realm are relative to
the current user/search.

## Booking
Add a Moshi converter to read 'flight' id from API response object and set this field to the saved Flight object if present.