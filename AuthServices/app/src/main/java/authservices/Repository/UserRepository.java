package authservices.Repository;

import authservices.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserInfo,String> {
    UserInfo findByUserName(String username);
}
