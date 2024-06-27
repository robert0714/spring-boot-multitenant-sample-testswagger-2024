package io.jomatt.multitenant.sample.common.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor 
@ToString
@Entity
@Table(name = "usr")
public class User {
	public User(){}
    @Id
    @NotNull
    @Column(name = "name", updatable = false, nullable = false, unique = true)
    private String name;

}
