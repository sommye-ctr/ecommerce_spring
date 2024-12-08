package org.example.ecommerce.repositories;

import org.example.ecommerce.models.AppRole;
import org.example.ecommerce.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(AppRole appRole);
}
