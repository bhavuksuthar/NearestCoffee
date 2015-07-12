# Nearest Coffee Locator 

NearestCoffee is a simple application to locate coffee store near your location.Application will display list of coffee location near to you in ascending order from your current location.It also provides a feature to see the location on map using Google Maps.

  - Android 2.0
  - Additional lib used google maps

To make this application i used a simple approach for coding.
Homepage will automatically fetch your location and get the data through API about the nearest coffee stores.Displays them in simple list.Heading with coffee store name location next to it and distance in meters.The list is displayed in ascending order from your current location



### Required Version
* **Android Froyo** or later

### Tech

Project consist of 4 java class only.
* **GPSTracker** to get the location in background and pass it jsonparse.
* **FoursquareVenue** is getter setter class for fields city,name field and category object.It implements comparable<T> to sort the list according to distance
* **AndroidFoursquare** is main class which is also the homepage of application.This class contains most of the code from getting JSON data to sort them by distance and displaly to the list.
* **MapView** is class which will display all the location on google maps with a coffee icon at the place.Basically it will display google map on screen with marking the coffee shops.

#### GPSTracker
This class main significance is go provide all service related to location.We can set all messages to display if GPS is not enabled or not working etc.
In addition to that it will also set 2 variable of 
* How often should location be updated? (1 minute in this case)
* After how much distance? (every 10 meters in this case)

#### FoursquareVenue
Getter and setter class for City,Name and Category. Category contains field distance.It implements comparable<T> to sort the list by distance. 

#### AndroidFoursquare
AndroidFoursquare class will do all the magic.It extends ListActivity as our main page contains list of all coffee stores.
It will get the latitude and longitude onCreate meathod.
Foursquare class extends AsynchTask because it will parse the url string in background and fetched data will be handeled to display on the UI.
Data fetched will be converted to arraylist type and handeled properly to get the desired field.
JSON data is fetched and stored in array and handeled as needed.
Sorting is done using collection.sort methd

### Installation
Only additional lib required is Google Play services.
Go to Project->properties->android in the section lib.
Add the library and apply.

### Layout
Very simple and basic layout of list is used to display coffeestores.

### Assumptions
Main assumption i made was that it will only display data which contains all 3 values of Name,Address,city and Distance.
Displayed distance is as it is in meters.