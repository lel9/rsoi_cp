package ru.bmstu.cp.rsoi.profile.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.bmstu.cp.rsoi.profile.domain.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String> {
}
