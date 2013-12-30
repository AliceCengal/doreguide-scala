# DoreGuide

This is a rewrite of the GuideAndroid project in Scala, mainly as a 
practice for me, but getting it production ready is not out of the 
picture. The initial setup is taken from the Scaloid project's Maven
template.

Prerequisites
-------------
* Maven 3
  - 3.0.5 to be specific. It doesn't work with 3.1.1 for now because of some crazy bug. 
  Please download the right version from http://maven.apache.org/download.cgi and 
  follow the instruction at the bottom of the page to set the default Maven version.
  
* Android SDK
  - Both SDK Level 8 and the most recent version should be installed.

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


