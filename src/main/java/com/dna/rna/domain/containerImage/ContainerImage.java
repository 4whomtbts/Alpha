package com.dna.rna.domain.containerImage;


import com.dna.rna.domain.instance.Instance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity
@Table(name= "container_image")
public class ContainerImage {

    public static final String CONTAINER_IMAGE_ID = "container_image_id";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = CONTAINER_IMAGE_ID)
    private Long containerImageId;

    @Column(name = "container_image_name")
    private String containerImageName;

    @Column(name = "container_image_nick_name")
    private String containerImageNickName;

    @Column(name = "container_image_desc")
    private String containerImageDescription;

    @OneToMany(mappedBy = "containerImage", fetch = FetchType.LAZY)
    private List<Instance> instanceList;

    private ContainerImage() {}

    public ContainerImage(String containerImageName, String containerImageNickName, String containerImageDescription) {
        requireNonNull(containerImageName, "containerImageName 는 null이 될 수 없습니다");
        requireNonNull(containerImageNickName, "containerImageNickName 는 null이 될 수 없습니다");
        requireNonNull(containerImageDescription, "containerImageDescription 는 null이 될 수 없습니다");
        this.containerImageName = containerImageName;
        this.containerImageNickName = containerImageNickName;
        this.containerImageDescription = containerImageDescription;
    }

}
