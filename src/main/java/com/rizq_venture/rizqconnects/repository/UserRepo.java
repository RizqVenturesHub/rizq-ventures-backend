package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM Users u WHERE " +
            "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.headline) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.currentCompany) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:location IS NULL OR LOWER(u.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Users> searchUsers(@Param("query") String query,
                            @Param("location") String location,
                            Pageable pageable);
}
