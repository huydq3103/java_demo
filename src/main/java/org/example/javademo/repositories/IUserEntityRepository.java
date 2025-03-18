package org.example.javademo.repositories;

import org.example.javademo.enity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserEntityRepository extends JpaRepository<UserEntity,Long> {
}
