package com.veloginmobile.server.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "velog_user_name")
    private Target target;

}
