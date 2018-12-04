package th.in.tcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.in.tcas.models.User;
import th.in.tcas.repositories.UserRepositoryInterface;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepositoryInterface userRepositoryInterface;

    public User createUser(User user) {
        return userRepositoryInterface.save(user);
    }

    public User login(User inboundUser) {
        User user = userRepositoryInterface.findByCitizenId(inboundUser.getCitizenid());
        if (user != null && user.getPassword().equals(inboundUser.getPassword()) ) {
            return user;
        }
        return null;
    }

    public User getUserByCitizenId(String citizen) {
        return userRepositoryInterface.findByCitizenId(citizen);
    }

    public User getUserById(long userId) {
        return userRepositoryInterface.findById(userId);
    }
}
