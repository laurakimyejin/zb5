package com.shiromi.ashiura.domain.entity;

import com.shiromi.ashiura.domain.dto.UserDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "username", length = 40, unique = true
            ,updatable = false, nullable = false
    )
    private String userName;

    @Column(name= "name", length =40)
    private String name;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name= "phone_number", length = 60)
    private String phoneNumber;

    @Column(name = "rating", length = 1)
    private String rating;

//    @ElementCollection(fetch = FetchType.EAGER)
//    private List<String> rating = List.of("N");

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String rating : rating.split(",")){
            authorities.add(new SimpleGrantedAuthority(rating));
        }
        return authorities;
        // rating을 ","로 구분해서 리스트로 정렬후 권한확인
        // 아래는 소스였던 List타입의 rating을 사용하는 코드들

//        return this.rating.stram()
//                 .map(SimpleGrantedAuthority::new)
//                 .collect(Collectors.toList());


//        return new SimpleGrantedAuthority(this.rating)
//                .collect(Collectors.toList());

    }


//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<Authority> rating = new ArrayList<>();
//    public void serRoles(List<Authority> role) {
//        this.rating = role;
//        role.forEach(o -> o.setUser(this));
//    }

    @Builder
    public UserEntity(long id, String userName, String name, String password, String phoneNumber, String rating) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
    }


    public UserDomain toDomain() {
        return UserDomain.builder()
                .userName(userName)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .rating(rating)
                .build();
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


