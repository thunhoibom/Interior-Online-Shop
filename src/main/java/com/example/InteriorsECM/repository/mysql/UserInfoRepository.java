package com.example.InteriorsECM.repository.mysql;

import com.example.InteriorsECM.model.mysql.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
}
