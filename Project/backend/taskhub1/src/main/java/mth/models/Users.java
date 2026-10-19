package mth.models;

import jakarta.persistence.*;

@Entity
@Table
public class Users {

	Long id;
	String fullname;
	String email;
	String password;
}
