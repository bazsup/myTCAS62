package th.in.tcas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import th.in.tcas.models.User;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    public User findByCitizenId(String citizenId);

    public User findById(long userId);
}
