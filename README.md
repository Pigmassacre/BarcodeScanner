# Barcode Scanner

Hello!

## Barcode Scanner and Eclipse

Here follows the instructions for importing and running the project in the Eclipse IDE:

### Requirements

  - Eclipse IDE: http://www.eclipse.org/
  - Android SDK: http://developer.android.com/sdk/index.html
  - Android Development Tools: http://developer.android.com/sdk/installing/installing-adt.html

### Instructions

After setting up the requirements and setting up a virtual mobile device in eclipse, running Android version 4.1+, you can follow these instructions:

1. In eclipse: File -> Import -> Existing Android Code Into Project Into Workspace; note that you could also import the project as a Git project.
2. Click on "Browse" and navigate to the root of the git project. There you choose the "Barcodescanner" sub-folder and hit "Finish".
3. Now to run the project you simply run the project as an Android Application on your virtual device (you can also connect your android phone to eclipse and use it as your device).

There is also a test project that follows with the git repository. In this project all of the tests for the Barcode Scanner is contained. To import it, do as you did to import the Barcode Scanner but in step 2 you choose the "BarcodeScannerTest" sub-folder instead. 
If the test project are showing errors it is likely that the test project can not find the main project. To fix this you do as follows: 

1. In eclipse: Right click on BarcodeScannerTest in the package explorer, Build Path -> Configure Build Path.
2. Under the "Projects" tab, click "Add..." and select the "BarcodeScanner". Press "Ok".
The Problems should now be non-existent.

## Informative Stuff

[Android Camera Tutorial](http://manijshrestha.wordpress.com/2011/11/10/working-with-camera-on-android-sdk/)

[Barcode Scanner Libraries](http://stackoverflow.com/questions/8009309/how-to-create-barcode-scanner-android)

