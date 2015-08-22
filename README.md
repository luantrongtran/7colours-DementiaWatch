# 7colours-DementiaWatch

##Material
### Please feel free to add websites or documents which are helpful

		Google Endpoints: https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
		Location API: http://www.vogella.com/tutorials/AndroidLocationAPI/article.html
		Background services: https://guides.codepath.com/android/Starting-Background-Services

## How to run the project locally
This instruction is for people who want to run the project on a real android device or an emulator which is different from the integrated one in Android Studio. This way can also be applied for the emulator integrated within Android Studio

###Step 1: 
you need to configure your backend server. Follow these 2 pictures:
		
		pic1: https://gyazo.com/5a80fef555dcbd35c24e46d11d990f01
		pic2: https://gyazo.com/d45b31ee40038a872177b9cba22ff4b8
		
###Step2: 
you need to change the web service url on your Android application. There are 2 Android applications in this project. They have the same structure. If you want to configure the web service url then change "webServiceUrl" variable in the Constants class in both Android applications. Particularly, you should change it into your computer's ip address. For example my ip is 192.168.0.10 then you should change the webServiceUrl to 

		 public static String webServiceUrl = "http://192.168.0.10:8080/_ah/api/";
