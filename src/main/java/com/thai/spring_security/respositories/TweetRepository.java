package com.thai.spring_security.respositories;

import com.thai.spring_security.entities.Role;
import com.thai.spring_security.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
