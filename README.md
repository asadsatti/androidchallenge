# Android Coding Challenge

### Explanation of your code architecture or libraries employed

* Used MVVM design patterns and some components of clean Architecture to make code testable
* Used viewBinding to display in the Activity/Fragment
* Used Coroutines to make async call on IO Dispatcher to remote service
* Used Retrofits to make REST API calls to the remote service
* Used converter-gson to convert JSON to data classes
* Used OkHttpClient as HTTP client for Retrofits
* Used play-services to get device location

Future Work
1. Use Navigation Components for navigation
2. Use complete Clean Architecture to make code testable as below:
  * ViewModel should be injected with UseCase that are used to request the weather data
  * UseCase should be injected with Repository that makes the REST API async call on IO Dispatcher to remote service via WeatherApi to retrieve the weather data and store the result in the local database using Realm or Room
  * While REST API async call to remote service is in progress we find all the weather in the local database and return as a flow to UseCase
  * When local database is updated with the new weather data, it will update the flow to reflect the updated weather
3. Use dependency injection framework like Dagger2
4. Write Unit tests for each layer of the Clean Architecture
5. Use authentication to get the appid key instead of hard coding it in the app
6. Use Timber logging library so that we can disable logs for production
7. Write code to handle Exceptions e.g. HttpException, IOException etc.
8. getWeatherApi() should be initialized lazily to prevent it from being created for each request
9. OkHttpClient should be injected for Retrofit creation & Retrofit should be injected for WeatherApi creation
