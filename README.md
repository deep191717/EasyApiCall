# EasyApiCall
[![](https://jitpack.io/v/deep191717/EasyApiCall.svg)](https://jitpack.io/#deep191717/EasyApiCall)

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
                implementation 'com.google.code.gson:gson:2.8.7'
	        implementation 'com.github.deep191717:EasyApiCall:2.1'
	}


 
 
Step 3. Implement that methode
 
 
       Api.with(this).setRequestMethod(RequestMethod.GET).call("https://google.com/", new Response() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
            }
            @Override
            public void onFailed(int code, String exception) {
                super.onFailed(code, exception);
            }
        });
