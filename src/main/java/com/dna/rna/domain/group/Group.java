package com.dna.rna.domain.group;

import com.dna.rna.DCloudUtil;
import com.dna.rna.domain.BaseAuditorEntity;
import com.dna.rna.domain.groupUser.GroupUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "group_table")
public class Group extends BaseAuditorEntity {

    public static final String GROUP_ID = "group_id";
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = GROUP_ID)
    private long groupId;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private List<GroupUser> members;

    @Column(name = "group_share_dir_name")
    private String groupShareDirName;

    protected Group() {}

    public Group(String groupName) {
        this.groupName = groupName;
        this.members = new ArrayList<>();
        this.groupShareDirName = DCloudUtil.generateRandomMd5DirName(this.groupName);
    }
}
