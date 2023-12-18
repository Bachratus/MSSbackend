package com.mss.app.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import com.mss.app.domain.keys.UserAuthorityKey;

@Entity
@Table(name = "user_authority")
public class UserAuthority {
    @EmbeddedId
    private UserAuthorityKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("authorityName")
    @JoinColumn(name = "authority_name")
    private Authority authority;

    public UserAuthorityKey getId() {
        return id;
    }

    public void setId(UserAuthorityKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
