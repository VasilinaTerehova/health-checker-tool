package com.epam.facade.model.fs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeDiskInfo {
    String infoAddr;
    String usedSpace;
    String adminState;

    public String getInfoAddr() {
        return infoAddr;
    }

    public void setInfoAddr(String infoAddr) {
        this.infoAddr = infoAddr;
    }

    public String getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(String usedSpace) {
        this.usedSpace = usedSpace;
    }

    public String getAdminState() {
        return adminState;
    }

    public void setAdminState(String adminState) {
        this.adminState = adminState;
    }
}
