package com.dna.rna.domain.groupUser;

import com.dna.rna.domain.group.Group;
import com.dna.rna.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "group_user")
public class GroupUser {

    public static final String GROUP_USER_ID = "group_user_id";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = GROUP_USER_ID)
    private long groupUserId;

    @ManyToOne
    private Group group;

    @ManyToOne
    private User user;

    @Column(name = "group_user_type")
    private GroupUserType groupUserType;

    protected GroupUser() {}

    public GroupUser(User user, Group group, GroupUserType groupUserType) {
        this.user = user;
        this.group = group;
        this.groupUserType = groupUserType;
    }
}
