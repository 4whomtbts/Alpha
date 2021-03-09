package com.dna.rna.domain.instanceLog;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Getter
@Setter
@Entity(name = "instance_log")
public class InstanceLog {

    public static final String INSTANCE_LOG_ID = "instance_log_id";

    public static final int GENERAL = 0;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name= INSTANCE_LOG_ID)
    private long instanceLogId;

    private int logType;

    @Column(nullable = false)
    private String logFileName;

    public InstanceLog(String logFileName) {
        requireNonNull(logFileName, InstanceLog.class.getName() + "의 logFileName은 null 일 수 없습니다.");
        this.logType = GENERAL;
        this.logFileName = logFileName;
    }

}
