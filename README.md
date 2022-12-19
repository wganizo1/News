# News

News Readme

- On launch we have the app showing the loading dialog and immediately after, the top headlines with the following details
    - Banner
    - Title
    - Author 
    - Publish date

- The loading dialog is a custom dialog found in the dialogs package as “ProgressDialog”
- If a headline item is clicked a new screen will appear showing the following details
    - Banner
    - Title
    - Author 
    - Publish date
    - Content
    - A “READ MORE” Button that initiates an implicit intent to open the entire article using the device’s default browser
    - A “SAVE” Button that saves the article for quick access by clicking the star icon in the navigation button (After considering the multiple options i had for saving these articles i decided to store them by way of writing a json file “favorites.txt” )
- The refresh button is placed at the bottom for the purpose of reloading the page
- The App uses the Glide Library to Load the image
- A RecycleView was used to display the top headlines 
- “top_headlines_cardview” is the Cardview where the design for the Recycler view is defines


- To switch between countries ie “us” and “ca” - You simple go to the bottom right hand corner of the screen and click the spinner to pick a country

- You can also view the version control history to follow the development process as pushed my code after each new feature implimentation
