# OpenAI Client for Android
A light weight OpenAI's API client for Android. This tool is specifically designed for Android developers to interact with OpenAI's API in a lightweight and efficient manner. With this client, Android developers will be able to access OpenAI's services seamlessly, without having to worry about heavy resource consumption or slow performance. This tool promises to be an invaluable asset for Android developers and users who require quick and easy access to OpenAI's API.

[![Jitpack](https://jitpack.io/v/mardillu/OpenAI-Client-Android.svg)](https://jitpack.io/#mardillu/OpenAI-Client-Android)
[![Build & Unit Test](https://github.com/mardillu/OpenAI-Client-Android/actions/workflows/build.yml/badge.svg)](https://github.com/mardillu/OpenAI-Client-Android/actions/workflows/build.yml)
[![License](https://img.shields.io/github/license/Aallam/openai-kotlin?color=yellow)](LICENSE.md)


## Disclaimer
OpenAI Client for Android is an open-sourced software licensed under the [MIT license](https://github.com/mardillu/OpenAI-Client-Android/blob/master/LICENSE).  **This is an unofficial library, it is not affiliated with nor endorsed by OpenAI**. Contributions are welcome.

## Setup
1. Add this to your Project level `build.gradle` at the end of repositories:
~~~groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
~~~
2. Add the dependency to your module level `build.gradle`:
~~~groovy
dependencies {
    //replace XX with the latest version
    implementation 'com.github.hannesa2:OpenAI-Client-Android:XX'
}
~~~

## Getting started
1. Initialize the client by calling the following function. You only need to do this once, ideally, in your Application class using environment variables for the API_KEY.
~~~kotlin
OpenAiInitializer.initialize("API_KEY")
~~~
2. Create an instance of `OpenAiClient`
```kotlin
val client = OpenApiClient()
```
3. Make a request
```kotlin
client.getTextCompletion("Hello chat gpt! what is the meaning of life?") { result, error ->
    if (error != null) {
        // Handle error
    } else if (result != null) {
        Log.d("TAG", result.choices[0].text)
    }
}
```
## Supported APIs
- [x] Completions
- [x] Chat
- [x] Edits
- [x] Images
  - Create
  - Edit
  - Variations
- [x] Embeddings
- [x] Moderations
- [x] Models
- [x] Audio
  - Transcribe
  - Translate

## Upcoming APIs
- [ ] Fine-tunes
- [ ] Files

## License
MIT License

Copyright (c) 2023 Ezekiel Sebastine

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
