package com.joinjoy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormInput {
    private String name;
    private String email;
    private String phone;
    private String description;
    private Integer organizerId;

}
