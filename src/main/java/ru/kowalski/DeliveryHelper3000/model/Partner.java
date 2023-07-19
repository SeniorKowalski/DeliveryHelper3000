package ru.kowalski.DeliveryHelper3000.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Нужно указать название регистрируемой компании")
    @Size(min = 2, max = 128, message = "Название должно содержать от 2 до 128 символов")
    @Column(name = "partner_name")
    private String partnerName;

    @NotEmpty(message = "Нужно указать адрес регистрируемой компании")
    @Size(min = 2, max = 128, message = "Адрес должен содержать от 2 до 128 символов")
    @Column(name = "partner_address")
    private String partnerAddress;

    @Column(name = "partner_coordinates")
    private String partnerCoordinates;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}