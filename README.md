# twitch-rec-engine

## Summary
An API to deliver Twitch Stream Recommendations based on the user's consumption history and views preferences, favorites, and up-to-date Twitch popular streaming activity.

## Build Details: 
- twitch-rec-engine uses Java version 17.
- We use Gradle for the build automation tool. 
- We use MySQL database connections for our data store. 

## API Routes

### GET /recommendation
Based on the login user, get the list of recommended items based on consumption activity, popular items. and friends item consumption activity, with logic defined by the recommendation engine. 

### GET /game
Get a list of Twitch's top games currently defined via the Twitch API. 

### POST /register
Initiate a user registration path. In the body, we require username, password, firstname, and lastname. 

### POST /login
Given a username and password, attempt to login. 

### POST /logout
Logout of the current logged in user. 

### GET /search?game_id={game_id}
Given a gameId, return TypeGroupedItemList from the Twitch API.

### GET /favorite
Return a list of the user's favorites. 

### POST /favorite
Given the body.favorite(), add the favorite item from the user favorites. 

### DELETE /favorite 
Given the body.favorite(), delete the favorite item from the user favorites. 

## Key Featuers

### Twitch API Client 
We use the TwitchApiClient to communicate with the Twitch API to fetch information on users, friends of users, etc. on Twitch item consumption activty, and item metadata. Here are the get mappings the program invokes to gather data currently: 

@GetMapping("/games")
@GetMapping("/games/top")
@GetMapping("/videos/")
@GetMapping("/clips/")
@GetMapping("/streams/")

### User Authentication
The AppConfig class is responsible for configuring Spring Security settings for authentication and authorization, including rules for permitting or restricting access to various paths and resources. It also customizes the behavior of the authentication and authorization process, as well as the handling of login, logout, and error situations.
- Uses formLogin() via Spring for login logic. 
- Uses session based cookies - upon successful login, a session is created, and a session cookie is sent to the client to maintain the user's authentication state.

### Security
The AppConfig class is responsible for configuring Spring Security settings for authentication and authorization, including rules for permitting or restricting access to various paths and resources. It also customizes the behavior of the authentication and authorization process, as well as the handling of login, logout, and error situations.

For encryption, for passwords and such, Spring stores passwords as hashed, not the original passwords set by the user, and the program implements additional hashing features for added security. 

## Running the app
To run the app, run the build via gradle and run the app:
>gradle build
>gradle bootRun