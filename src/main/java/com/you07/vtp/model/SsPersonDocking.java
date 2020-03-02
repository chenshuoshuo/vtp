package com.you07.vtp.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author cs
 * @Date 2020/3/2 12:30
 * @Version 2.2.2.0
 **/
@Table(name = "ss_person_docking")
public class SsPersonDocking {
    @Id
    @Column(name = "docking_id")
    private int dockingId;
    private String dockingName;
    private Timestamp accessTime;


    public int getDockingId() {
        return dockingId;
    }

    public void setDockingId(int dockingId) {
        this.dockingId = dockingId;
    }

    public String getDockingName() {
        return dockingName;
    }

    public void setDockingName(String dockingName) {
        this.dockingName = dockingName;
    }

    public Timestamp getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Timestamp accessTime) {
        this.accessTime = accessTime;
    }

}
