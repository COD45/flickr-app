h# Erste Bank Digital / George Labs Home Task

## Project tech stack overview

DI: Hilt
UI: Compose
Thread handling: Flows / Coroutines
Networking: Retrofit / OkHttp
Unit tests: JUnit5
UI tests: Compose UI tests / Espresso-like

## Android Home Task:

Flickr has some public accessible feeds:
https://www.flickr.com/services/feeds/
One feed provides a list of public photos. See more at
https://www.flickr.com/services/feeds/docs/photos_public/
You do not need to create an API key.
To get a JSON response use this format:
https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1

## Task:

Create a simple Android application that uses this API. For the Design you can express your own
creativity.
Let the user enter some tags he/she wants to search for and return a list of these images in a list
or grid.
Show the title of every image.
On image tap open a full screen view of the image.
Preferred language: Kotlin
