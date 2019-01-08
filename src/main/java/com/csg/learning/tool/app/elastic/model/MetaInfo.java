package com.csg.learning.tool.app.elastic.model;

import java.util.Date;

public class MetaInfo {

    private Date validFrom;
    private Date validUntil;
    private Date systemFrom;
    private Date systemUntil;

    public MetaInfo(){}

    public MetaInfo(Date validFrom, Date validUntil, Date systemFrom, Date systemUntil) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.systemFrom = systemFrom;
        this.systemUntil = systemUntil;
    }

    public Date getValidFrom() { return validFrom; }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Date getSystemFrom() {
        return systemFrom;
    }

    public void setSystemFrom(Date systemFrom) {
        this.systemFrom = systemFrom;
    }

    public Date getSystemUntil() {
        return systemUntil;
    }

    public void setSystemUntil(Date systemUntil) {
        this.systemUntil = systemUntil;
    }
}
