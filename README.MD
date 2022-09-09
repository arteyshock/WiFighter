A simple application for quickly opening Wi-Fi settings and turning Wi-Fi on/off on the WearOS watch
After installation, you will have 3 options:

- ##### Standard Application
  You can launch it from the list of applications or setup on the quick launch button

- ##### Complication
  If your watchface supports complications, you can add one.
  It can be a small icon showing the status of Wi-Fi, or a large text that also shows your current IP address (if connected).

- ##### Tile
  There is a button on the tile that shows the Wi-Fi status. And the IP address if the watch is connected.
  **You can update the tile by clicking on the text line** (*for example: if wifi is on but still there is no IP shown*)

#### How it works
The app launches activity without any layout. It just starts *ACTION_WIFI_SETTINGS* intent and requests for updating both complication and tile. Then it quits.
On receiving request for update, both complication and tile wait for ~9 seconds (to let user connect to Wi-Fi)  and then show new status of Wi-Fi connection ¯\\_(ツ)\_/¯.

**NOTE:** since there is no possibility to catch *WIFI_STATE_CHANGED* action on WearOS 2.x by broadcast receiver in manifest, complication and tile get update request only from this app. If you change Wi-Fi connection otside this app, the actual status may not be displayed correctly.
To fix it you can tap on IP address line on tile or tap on complication and just swipe settings off/

*If you know how can I improve this app or have a job for me - [mail me](mailto:art.an.savin@gmail.com)*