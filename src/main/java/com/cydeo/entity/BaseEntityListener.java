package com.cydeo.entity;

import com.cydeo.entity.common.UserPrincipal;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {
// ^^ separate from BaseEntity b/c this portion needs to extend AuditingEntityListener
// serves an extra layer on top of BaseEntity to map all the extra fields such as
// insert/update DateTime, userID, etc., to the Spring Security User object**

    @PrePersist
    private void onPrePersist(BaseEntity baseEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // ^^ get authentication of logged-in user from Spring Security

        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if (authentication != null && !authentication.getName().equals("anonymousUser")) {

            Object principal = authentication.getPrincipal();
            baseEntity.setInsertUserId(( (UserPrincipal) principal).getId());
            baseEntity.setLastUpdateUserId(( (UserPrincipal) principal).getId());
        }
    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if (authentication != null && !authentication.getName().equals("anonymousUser")) {

            Object principal = authentication.getPrincipal();
            baseEntity.setInsertUserId(( (UserPrincipal) principal).getId());
            baseEntity.setLastUpdateUserId(( (UserPrincipal) principal).getId());
        }
    }

}
