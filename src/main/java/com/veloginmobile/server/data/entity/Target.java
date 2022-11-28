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
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "velog_user_name")
    private String velogUserName;

    @OneToMany(mappedBy = "target")
    private List<Subscribe> subscribes = new ArrayList<>();

}
