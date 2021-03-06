Reddit Client
-

Created by Amir Uval as an exercise.


#### Missing features

- Testing coverage is low
- <strike>in endless list: make the last row show loading... animation</strike>
- <strike>filtering</strike>

#### Bugs

- WebView not scaling down to show everything
- Rows width not filling the total width if title is short.
- <strike>when changing fav state in webview, going back to the list view doesn't refresh to show that (the data is ok). Need to refresh.</strike>
- 

#### NTH

- *(done)* favorite toggleable star on every row
- *(done)* when filtering rows, highlight the text matching the filter
- replace manual onClickListener with the databinding way
- when there's no network don't show busy wait. Tell the user.
- swipe up when top-most to retrieve new data from the server
- fancy wait animation

#### Notes on the architecture chosen

I've used a mixture of MVP and MVVM patterns for code separation, to allow Unit testing of the Presenter mostly.

I've also used my own class to allow serialize async and ui thread operations.
But I think that RxJava would work better for that. 
I didn't need RxJava for my past projects, and it's not a good idea to work on RxJava for the first time on a time constrained test project.

I didn't use DI framework library in this project (Dagger2), 
but I did use DI architecture, implemented manually by creating dependencies in the Activity and passing them to consumers.


#### Notes on libraries used

Retrofit: for fetching data from the server
picasso: for the thumbnails
Room: used to store favorites in db
databinding: used for all layouts to better UI performance and less code
mocitto: for unit testing


#### debug db reference 
(just for my copy&paste convenience):
<pre>
 adb shell
 run-as com.mta.redditclient
 sqlite3 databases/fav.db
 .headers on
 .mode column
 .width 20 20 20 20 20 20 20 20 20 20 20 
 .tables 
 select * from Favorite;
</pre>

