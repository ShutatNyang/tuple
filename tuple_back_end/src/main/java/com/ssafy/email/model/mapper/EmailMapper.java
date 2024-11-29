package com.ssafy.email.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmailMapper {
    void saveEmailToken(@Param("email") String email, @Param("token") String token);
    String getEmailByToken(@Param("token") String token);
    void deleteEmailToken(@Param("token") String token);
    int authorizeMember(@Param("email") String email);

    void savePasswordResetToken(@Param("email") String email, @Param("token") String token);
    String getEmailByPasswordResetToken(@Param("token") String token);
    void deletePasswordResetToken(@Param("token") String token);

    void updatePassword(@Param("email") String email, @Param("hashedPassword") String hashedPassword);
}