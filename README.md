# RepeatingAlarm
Take and send photo every hour(minute, day ,month, year) to a specific gmail address.

APPLICATION IS BASED/EXTENDED from Android exmaple RepeatingAlarm 

Consfiguration is in : base-strings.xml

STARTUP SETTINGS

 <string name="startup_register">ON</string> - when set to ON the application will Activate/Set the alarm automaticaluy without human intervention. Any other value will require the alarm to set Activate/Set using the UI - after first time the alarm will be active until application is killed/stopped from android device settings.

 <string name="startup_on_boot">ON</string> - when set to ON the application will start automaticaly after boot. Any other value will require manual start of the application.
 
GMAIL SETTINGS

<string name="email_user_name">sy.alfil.sl34@gmail.com</string> user name/email in gmail on which behalf the emails will be sent and will receice the emails also.

<string name="email_user_password">password</string> password for the gmail account

Subject of the email will have the following format : CEMARA timestamp e.g. : CAMERA 20170312_16
The camera text can be changed from the following setting :

<string name="camera_name">CAMERA</string>

Image file name  will have the following format : PREFIX+timestamp e.g. : ALFIL_20170312_16
The prefix can be changes from the following setting:

 <string name="image_prefix">ALFIL_</string>

!!!IMPORTANT!!!
In order he email sending to work you need to set in your google account lesser security apps to ON e.g. :

https://www.google.com/settings/security/lesssecureapps - need to be Turned ON

IMAGE SETTINGS

Depending on your device camera you can use the following settings

<string name="image_preview_size">1920x1080</string>
<string name="image_picture_size">2560x1920</string>
<string name="image_picture_format">jpeg</string>
<string name="image_jpeg_quality">85</string>
<string name="image_orientation">landscape</string>
<string name="image_rotation">0</string>
<string name="image_flash_mode">off</string>
<string name="image_iso_speed">auto</string>
<string name="image_whitebalance">auto</string>
<string name="image_scene_mode">auto</string>
<string name="image_focus_mode">continuous-picture</string>

TIME INTERVAL SETTING

The interval in which the images are taken and sent to the email is controlled from teh following setting:

 <string name="image_timestamp_format">yyyyMMdd_HH</string>
 
 every minute (once per minute) : yyyyMMdd_HHmm
 every hour (once per hour) : yyyyMMdd_HH
 every day (once per day): yyyyMMdd
 every month (once per month) : yyyyMM
 every year(once per year) : yyyy
 
 

 
 
