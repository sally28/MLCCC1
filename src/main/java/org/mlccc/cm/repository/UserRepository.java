package org.mlccc.cm.repository;

import org.mlccc.cm.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = {"authorities", "schoolDistrict"})
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    @Query("SELECT u FROM User u WHERE lower(u.login) like (:search) or lower(u.firstName) like (:search) or lower(u.lastName) like (:search)")
    Page<User> findAllWithSearch(Pageable pageable, @Param("search") String search);
}
