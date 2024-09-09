# twitch-rec-engine

## Summary
<code>twitch-rec-engine</code> is an API that delivers Twitch Stream Recommendations based on the user's consumption history and views preferences, favorites, and up-to-date Twitch popular streaming activity. Sign up, log in, view Twitch, and explore our platform's Twitch Recommendations!

## Build Details
- twitch-rec-engine uses Java version 17.
- We use Gradle for the build automation tool. 
- We use MySQL database connections for our data store. 
- We use JUnit, Mockito for Unit Testing. 

## API Routes

```GET /recommendation```

Based on the login user, get the list of recommended items based on consumption activity, popular items. and friends item consumption activity, with logic defined by the recommendation engine. 

```GET /game```

Get a list of Twitch's top games currently defined via the Twitch API. 

```POST /register```

Initiate a user registration path. In the body, we require username, password, firstname, and lastname. 

```POST /login```

Given a username and password, attempt to login. 

```POST /logout```

Logout of the current logged in user. 

```GET /search?game_id={game_id}```

Given a gameId, return TypeGroupedItemList from the Twitch API.

```GET /favorite```

Return a list of the user's favorites. 

```POST /favorite```

Given the body.favorite(), add the favorite item from the user favorites. 

```DELETE /favorite```

Given the body.favorite(), delete the favorite item from the user favorites. 

## Key Featuers

### Twitch API Client 
We use the TwitchApiClient to communicate with the Twitch API to fetch information on users, friends of users, etc. on Twitch item consumption activty, and item metadata. Here are the get mappings the program invokes to gather data currently: 

- ```@GetMapping("/games")```
- ```@GetMapping("/games/top")```
- ```@GetMapping("/videos/")```
- ```@GetMapping("/clips/")```
- ```@GetMapping("/streams/")```

### User Authentication
The AppConfig class is responsible for configuring Spring Security settings for authentication and authorization, including rules for permitting or restricting access to various paths and resources. It also customizes the behavior of the authentication and authorization process, as well as the handling of login, logout, and error situations.
- Uses formLogin() via Spring for login logic. 
- Uses session based cookies - upon successful login, a session is created, and a session cookie is sent to the client to maintain the user's authentication state.

### Security
For encryption, for proprietary data, we use Spring to store data as hashed. 
 
## Running the app
To run the app, run the build via gradle and run the app:

```gradle build```

```gradle bootRun```


## Deploying Live
One way to deploy this live is to use the following services on AWS: 
- Use AWS App Runner to host and run the app. No scalability settings required. Fairly easy and straightforward. Also integrates easily with...
- AWS RDS with MySQL. Also straightforward to run. 


## Unit Testing Walkthrough
Here is a walkthrough of what we used for unit testing, some methods/annotations we use and rationale:

### JUnit 5 (org.junit.jupiter):
A widely-used testing framework for Java for unit testing.
Key components used:
- ```@Test```: Marks a method as a test method.
- ```@BeforeEach```: Annotates a method to be run before each test.
- Assertions: Contains assertion methods for comparing expected results with actual outcomes.

### Mockito (org.mockito):
A popular mocking framework used to mock dependencies and verify interactions in unit tests.
Key components used:
- ```@Mock```: To mock external dependencies (ItemRepository, FavoriteRecordRepository).
- ```@Captor```: Used to capture arguments passed to a method call (favoriteRecordArgumentCaptor).
- ```Mockito.when()```: To define the behavior of mocked methods.
- ```Mockito.verify()```: To verify interactions with mocked objects.
- ```Mockito.never()```, ```Mockito.verifyNoInteractions()```: To ensure that certain interactions did not occur.

### Mockito JUnit 5 Extension (MockitoExtension):
Used to initialize and manage mocks created by Mockito.
It is added via @ExtendWith(MockitoExtension.class).