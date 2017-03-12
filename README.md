# RepeatingAlarm
Take and send photo every hour(minute, day ,month, year) to a specific gmail address.

APPLICATION IS BASED/EXTENDED from Android exmaple RepeatingAlarm 

<pre><code>

Consfiguration is in : base-strings.xml

STARTUP SETTINGS

 &lt;string name="startup_register"&gt;ON&lt;/string&gt; - when set to ON the application will Activate/Set the alarm automaticaluy without human intervention. Any other value will require the alarm to set Activate/Set using the UI - after first time the alarm will be active until application is killed/stopped from android device settings.

 &lt;string name="startup_on_boot"&gt;ON&lt;/string&gt; - when set to ON the application will start automaticaly after boot. Any other value will require manual start of the application.
 
GMAIL SETTINGS

&lt;string name="email_user_name"&gt;sy.alfil.sl34@gmail.com&lt;/string&gt; user name/email in gmail on which behalf the emails will be sent and will receice the emails also.

&lt;string name="email_user_password"&gt;password&lt;/string&gt; password for the gmail account

Subject of the email will have the following format : CEMARA timestamp e.g. : CAMERA 20170312_16
The camera text can be changed from the following setting :

&lt;string name="camera_name"&gt;CAMERA&lt;/string&gt;

Image file name  will have the following format : PREFIX+timestamp e.g. : ALFIL_20170312_16
The prefix can be changes from the following setting:

 &lt;string name="image_prefix"&gt;ALFIL_&lt;/string&gt;

!!!IMPORTANT!!!
In order he email sending to work you need to set in your google account lesser security apps to ON e.g. :

https://www.google.com/settings/security/lesssecureapps - need to be Turned ON

IMAGE SETTINGS

Depending on your device camera you can use the following settings

&lt;string name="image_preview_size"&gt;1920x1080&lt;/string&gt;
&lt;string name="image_picture_size"&gt;2560x1920&lt;/string&gt;
&lt;string name="image_picture_format"&gt;jpeg&lt;/string&gt;
&lt;string name="image_jpeg_quality"&gt;85&lt;/string&gt;
&lt;string name="image_orientation"&gt;landscape&lt;/string&gt;
&lt;string name="image_rotation"&gt;0&lt;/string&gt;
&lt;string name="image_flash_mode"&gt;off&lt;/string&gt;
&lt;string name="image_iso_speed"&gt;auto&lt;/string&gt;
&lt;string name="image_whitebalance"&gt;auto&lt;/string&gt;
&lt;string name="image_scene_mode"&gt;auto&lt;/string&gt;
&lt;string name="image_focus_mode"&gt;continuous-picture&lt;/string&gt;

TIME INTERVAL SETTING

The interval in which the images are taken and sent to the email is controlled from teh following setting:

 &lt;string name="image_timestamp_format"&gt;yyyyMMdd_HH&lt;/string&gt;
 
 every minute (once per minute) : yyyyMMdd_HHmm
 every hour (once per hour) : yyyyMMdd_HH
 every day (once per day): yyyyMMdd
 every month (once per month) : yyyyMM
 every year(once per year) : yyyy
 
 
 </code></pre>
 
 
