package com.IDM3.CRUDmuse.repository;

import com.IDM3.CRUDmuse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNameAndPassword(String username, String password);

}
