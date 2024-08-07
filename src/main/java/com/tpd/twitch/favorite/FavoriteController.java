package com.tpd.twitch.favorite;

import com.tpd.twitch.db.entity.UserEntity;
import com.tpd.twitch.model.FavoriteRequestBody;
import com.tpd.twitch.model.TypeGroupedItemList;
import com.tpd.twitch.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;




@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final UserService userService;
    // Hard-coded user for temporary use, will be replaced when writing authentication code in the future
    // private final UserEntity userEntity = new UserEntity(1L, "user0", "Foo", "Bar", "password");
    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @GetMapping
    public TypeGroupedItemList getFavoriteItems(@AuthenticationPrincipal User user) {
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        return favoriteService.getGroupedFavoriteItems(userEntity);
    }

    @PostMapping

    // The exception here can be thrown directly on the function with DuplicateFavoriteException, passing
    // the exception back to the calling function. However, this is not a very good architectural practice;
    // it's better to handle it within your own scope. Don't have exceptions here that are not related to
    // you. Here, we use try-catch to control it ourselves.
    public void setFavoriteItem(@AuthenticationPrincipal User user, @RequestBody FavoriteRequestBody body) throws DuplicateFavoriteException {


    //@RequestBody extracts the content from the JSON and processes it??
    // Why do we use try-catch here, and then throw an exception in catch? Because we want to separate the logic.
        // Errors should be handled in the controller. Even if used elsewhere via dependency injection and then called,
        // we should inform the frontend that this error is related to the controller, not the current calling function.
    // How do we understand single responsibility? How do we understand decoupling? This is a good example for design and architecture.
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        try {
            favoriteService.setFavoriteItem(userEntity, body.favorite());
        } catch (DuplicateFavoriteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate entry for favorite record", e);
        }
    }
    @DeleteMapping
    public void unsetFavoriteItem(@AuthenticationPrincipal User user, @RequestBody FavoriteRequestBody body) {
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        favoriteService.unsetFavoriteItem(userEntity, body.favorite().twitchId());
    }
}
