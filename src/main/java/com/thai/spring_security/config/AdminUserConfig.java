package com.thai.spring_security.config;

import com.thai.spring_security.entities.Role;
import com.thai.spring_security.entities.User;
import com.thai.spring_security.respositories.RoleRepository;
import com.thai.spring_security.respositories.UserRespository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig  implements CommandLineRunner {
    private RoleRepository roleRepository;
    private UserRespository userRespository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig (RoleRepository roleRepository, UserRespository userRespository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRespository = userRespository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public void  run( String ...args) throws  Exception {
        var RoleAdmin = roleRepository.findByName(Role.Values.ADMIN.name()) ;
        var userAdmin = userRespository.findByUsername("admin");
        userAdmin.ifPresentOrElse((user)-> {
            System.out.println("admin exist");
        } , ()->{
            var user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123"));
            user.setRoles(Set.of(RoleAdmin));
            userRespository.save(user);
        });
    }
}
