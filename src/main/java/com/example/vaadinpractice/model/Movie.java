package com.example.vaadinpractice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//import javax.persistence.Entity;
//import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "title"})
//@Entity
public class Movie {
//    @Id
    private long id;
    private String title;
}
