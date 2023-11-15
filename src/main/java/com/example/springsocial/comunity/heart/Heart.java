package com.example.springsocial.comunity.heart;

import javax.persistence.*;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.model.User;

@Entity
@Table(name = "heart")
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comunity_id")
    private Comunity comunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    public Heart() {
    }
        
	public Heart(User user,Comunity comunity) {
		this.user = user;
		this.comunity = comunity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Comunity getComunity() {
		return comunity;
	}

	public void setComunity(Comunity comunity) {
		this.comunity = comunity;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    // Getter and Setter
}
