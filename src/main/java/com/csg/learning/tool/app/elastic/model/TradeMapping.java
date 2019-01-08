package com.csg.learning.tool.app.elastic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TradeMapping {


    private String id;
    private String cif;
    private String tid;
    private String system;
    private String bu;
    private MetaInfo metaInfo;

    public TradeMapping(){

    }
    public TradeMapping(String id, String cif, String tid, String system, String bu) {
        this.id = id;
        this.cif = cif;
        this.tid = tid;
        this.system = system;
        this.bu = bu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public String toString() {
        return "TradeMapping{" +
                "id='" + id + '\'' +
                ", cif='" + cif + '\'' +
                ", tid='" + tid + '\'' +
                ", system='" + system + '\'' +
                ", bu='" + bu + '\'' +
                '}';
    }
}
