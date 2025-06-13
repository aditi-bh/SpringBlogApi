package com.springBootBlogapis.Repository;

import com.springBootBlogapis.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);

}
