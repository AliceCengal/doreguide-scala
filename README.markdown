# DoreGuide

This is a rewrite of the [GuideAndroid](https://github.com/VandyMobile/guide-android) project in Scala, mainly as a 
practice for me, but getting it production ready is not out of the 
picture. The initial setup is taken from the Scaloid project's Maven
template.

Prerequisites
-------------
* Maven 3
  - 3.0.5 to be specific. It doesn't work with 3.1.1 for now because of some crazy bug. 
  Please download the right version from http://maven.apache.org/download.cgi and 
  follow the instruction at the bottom of the page to set the default Maven version. Check
  the version that you have with `mvn -v`
  
* Android SDK
  - Both SDK Level 8 and the most recent version (level 19) should be installed.

* Create a new file `res/values/mapsapikey.xml` (which is automatically excluded in `.gitignore`) and put in this text:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="maps_api_key_v2">xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</string>
</resources>
```

  Get your own API key by following the instructions on this page https://developers.google.com/maps/documentation/android/start#getting_the_google_maps_android_api_v2
  and place the key in here. Please double check before your first commit to make sure that your API key is not
  published to Github.
  
Build
-----
You can build using Maven:

    $ mvn clean package

This will compile the project and generate an APK. The generated APK is
signed with the Android debug certificate. To generate a zip-aligned APK
that is signed with an actual certificate, use:

    $ mvn clean package -Prelease

The configuration for which certificate to use is in pom.xml.

Run
---
Deploy to an Android virtual device (AVD):

    $ mvn android:deploy

You can use the Logcat script in the `/scripts` folder: `adb logcat DoreGuide:D SqliteDatabaseCpp:S *:E`. Adjust the
parameters to get a readable output.

Programming Notes
-----------------
In object `util.LogUtil`, set the variable `LogEnabled` to `false` for the production build. This turns off all logging.

Architecture
------------

    Views           |Uis              |Controllers         |Services
    ----------------|-----------------|--------------------|----------
    DataAdapter     |MainActivity     |MainController      |Dore
    ViewHolder      |PlaceListFrag    |PlacesController    |PlaceServer
    PlaceView       |AgendaTabFrag    |AgendaController    |Geomancer
    TourView        |TourTabFrag      |ToursController     |AgendaManager
    ...             |NavigationTabFrag|NavigationCont.     |TourServer
                    |MapFragment      |MapController       |ImageServer
                    |PlaceDetailer    |...                 |NodeServer
                    |...              |                    |...

**Services** connects the app to the outside world. They handle downloads and fetching of data, and they are source
agnostic. The data could be from a server somewhere, or it could be cached in a local SQL database. The Services
objects abstracts this so that UI programming becomes easier. All the Services are available as a member of the `Dore`
singleton, which is initialized and destroyed according to the lifecycle of `MainActivity`, so do not instantiate them.

All of the Services classes are `Actors`. Request data by sending a message defined in their respective companion object
and wait for their response. `Server`s are immutable and they serve static data. `Manager`s serves editable data which
can be modified by sending the appropriate messages. `Geomancer` is just a wizard.

**Controllers** is the intermediary between the Services layer with the UI layer. They are actors too, and they forward
request from the UI layer to the Services Actors, and they listen to the reply from the Services and pass the data to
the UI. Controllers also act as the intermediary between Activities and Fragments, providing a more dynamic interface
than the static TabListener.

**UIs** are the plain old Activities and Fragments. There would usually be a Controller Actor defined in the same file
for communication with the Services layer.

**Views** are custom Views and adapters used for translating data into visual elements. The UI layer will instantiate
these elements using the data passed to them from their Controllers.

