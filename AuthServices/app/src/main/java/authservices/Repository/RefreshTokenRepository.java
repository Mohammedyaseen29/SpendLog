package authservices.Repository;


import authservices.entities.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<Token,Integer> {
   Optional<Token> findByToken(String token);
}
