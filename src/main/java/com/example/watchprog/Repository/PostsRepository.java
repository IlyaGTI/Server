package com.example.watchprog.Repository;

import com.example.watchprog.Entity.Posts;
import com.example.watchprog.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
   List<Posts> findAllByUserOrderByCreateDateDesc (User user);
   List<Posts> findAllByOrderByCreateDateDesc();
   Optional<Posts> findPostsByIdAndUser(Long id, User user);

}
