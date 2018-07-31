# IoT Demo
This new no-touch app is built on the Android Things developer kit. It aims at improved touchless navigation within the news feed. We made our device sense the user's gestures and navigate through the app, by using APIs to control hardware peripherals. In order to interact with the content, i.e to move the pages, a user needs to wave his/her hands in the needed directions: up, down, left, right. 

## Getting Started
### Prerequisites
For stable work, you need one of the following kits:
* [Android Things](https://developer.android.com/things/get-started/kits)

### Installing
Clone repository and get your oun API keys for OpenWeatherMap API and News API. Then set them into in build.gradle instead "xxxxxxxxxxxx" both for release and debug buildTypes.
```
    buildConfigField "String", "WEATHER_API_KEY", "\"xxxxxxxxxxxxx\""
    buildConfigField "String", "NEWS_API_KEY", "\"xxxxxxxxxxxx\""
```

## Usage
### Gesture Detection
Activity should implement CameraGestureSensor.Listener and override callback methods:
```
    override fun onGestureUp(caller: CameraGestureSensor?, gestureLength: Long) {
      // to do smth
      // Note, it isn't main thread! 
    }

    override fun onGestureDown(caller: CameraGestureSensor?, gestureLength: Long) {
      // to do smth
      // Note, it isn't main thread! 
    }

    override fun onGestureLeft(caller: CameraGestureSensor?, gestureLength: Long) {
      // to do smth
      // Note, it isn't main thread! 
    }

    override fun onGestureRight(caller: CameraGestureSensor?, gestureLength: Long) {
      // to do smth
      // Note, it isn't main thread! 
    }
```
After that you can launch gesture detection by calling: 
```
    LocalOpenCV(this, this)
```


### RainbowHat 

Also, there is an ability to control Rainbow Hat controls(ABC buttons, ABC leds, Buzzer, Digits display, Ledstrip)
All, what you need to do is to create new instance of controller and call some actions. In example there is a Buzzer controller and it play Star Wars intro =)

```
var buzzerController = BuzzerController(BuzzerSupplierImpl(), lifecycle) 
buzzerController.playStarWars()
```
## Example
* [Youtube](https://www.youtube.com/watch?v=HHUKwmIfOKA&feature=youtu.be)

## Built With
* [NewHandwave library](https://github.com/jtkac/NewHandwave/) - Gesture getection
* [Koin](https://github.com/InsertKoinIO/koin/) - Dependency Injection
* [Retrofit 2](http://square.github.io/retrofit/) - HTTP client
* [Kotlin Coroutine Adapter](https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter/) - Adapter for Retrofit 2
* [Glide](https://github.com/bumptech/glide/) - Image processing
* [OpenWeatherMap](https://openweathermap.org/) - Retrieving weather
* [NewsAPI](https://newsapi.org/) - Retrieving last news
* [IpAPI](https://ipapi.co/) - Retrieving city by Ip

## License
Licensed under the Apache License, Version 2.0 (the "License"). You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

You agree that all contributions to this repository, in the form of fixes, pull-requests, new examples etc. follow the above-mentioned license.
