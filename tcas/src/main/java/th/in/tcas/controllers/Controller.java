package th.in.tcas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.in.tcas.DTO.JWTResponse;
import th.in.tcas.exception.UserNotFoundException;
import th.in.tcas.logger.AuditLogger;
import th.in.tcas.models.User;
import th.in.tcas.services.JWTService;
import th.in.tcas.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class Controller {
    AuditLogger logger = new AuditLogger(this.getClass().getName());

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("citizenId") String citizenId,
            @RequestParam("password") String password
    ) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(password);
        user.setCitizenid(citizenId);
        return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        User inboundUser = new User();
        inboundUser.setCitizenid(username);
        inboundUser.setPassword(password);
        User user = userService.login(inboundUser);
        if (user == null) {
            throw new UserNotFoundException("user not found for user: " + username);
        }
        JWTResponse jwtResponse = jwtService.createToken(user);
        return new ResponseEntity<JWTResponse>(jwtResponse, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(@RequestHeader(name = "Authorization", required = true) String token,
                                        HttpServletRequest request) {
        long userId = jwtService.getUserIdFromToken(token);
        User user = userService.getUserById(userId);
        logger.info(request, user.getId() + " get student data");
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @GetMapping("/user/{citizen}")
    public ResponseEntity<User> getUser(@PathVariable String citizen) {
        return new ResponseEntity<User>(userService.getUserByCitizenId(citizen), HttpStatus.OK);
    }
}
