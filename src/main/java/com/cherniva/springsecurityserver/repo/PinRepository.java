package com.cherniva.springsecurityserver.repo;

import com.cherniva.springsecurityserver.model.Pin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PinRepository extends JpaRepository<Pin, Long>  {
    Pin findByValue(String value);

    Pin findByPassword(String password);

    Pin findByValueAndPassword(String value, String password);
}
