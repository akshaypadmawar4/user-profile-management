package com.uxpsystems.assignment.dao;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uxpsystems.assignment.entity.UserProfile;

@Repository
public interface ProfileRepository extends CrudRepository<UserProfile, Long>{

	Optional<UserProfile> findById(Integer profileId);

}
