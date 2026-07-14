package DATN.example.demo.repository;

import DATN.example.demo.entity.User;
import DATN.example.demo.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUserUserId(long userId);
}
