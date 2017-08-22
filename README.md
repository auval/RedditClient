Reddit Client
-

Created by Amir Uval as a home test for Wix..


#### Missing features

- in endless list: make the last row show loading... animation
- filtering
- Testing coverage is low

#### Bugs

- when changing fav state in webview, going back to the list view doesn't refresh to show that (the data is ok). Need to refresh.
- WebView not scaling down to show everything
- Rows width not filling the total width if title is short.
- 

#### NTH

- fancy wait animation
- replace manual onClickListener with the databinding way

#### debug db reference:

- adb shell
- run-as com.mta.redditclient
- cd databases/
- sqlite3 fav.db
- .headers on
- .mode column
- .width 20 20 20 20 20 20 20 20 20 20 20 
- .tables 
- select * from Favorite;



