package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Поле email не должно быть пустым")
    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Без пароля нельзя>")
    @Size(min = 4, max = 64, message = "Пароль должен содержать от 4 до 64 символов")
    @Column(name = "password")
    private String password;

    @Column(name = "user_role")
    private String role;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Partner> partners;

    public void addPartner(Partner partner) {
        partners.add(partner);
        partner.setPerson(this);
    }

    public void removePartner(Partner partner) {
        partners.remove(partner);
        partner.setPerson(null);
    }


}