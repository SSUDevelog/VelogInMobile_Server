package com.veloginmobile.server.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Target implements Serializable {
//이상하게 Serializable붙여주면 허용됨.
    //확인 결과 pk아닌 애 참조하려면 Seializable을 사용해야 한다고함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "velog_user_name")
    private String velogUserName;

    //영속성 전이. 삭제되면 아래 얘도 삭제 되도록
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
    private List<Subscribe> subscribes = new ArrayList<>();

}
