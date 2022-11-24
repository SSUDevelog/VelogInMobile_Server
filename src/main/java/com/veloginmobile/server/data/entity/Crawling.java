package com.veloginmobile.server.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Crawling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String velogUserName;

    @ElementCollection
    private List<String> userList = new ArrayList<>();

}
