package bepoland.bookconference.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Size;

//@DynamicUpdate
@Entity
@Table(name = "users")
public class User {

    private Long id;
    private String name;
    private String surname;
    private String login;
    private String password;


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @Size(max = 50)
    @Column(nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @Size(max = 100)
    @Column(nullable = false, length = 100)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

//    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

//    @Size(min = 6, max = 100)
    @Column(nullable = false, length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
