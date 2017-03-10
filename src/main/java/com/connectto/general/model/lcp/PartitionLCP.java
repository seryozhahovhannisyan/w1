package com.connectto.general.model.lcp;

import com.connectto.general.util.Utils;

/**
 * Created by htdev01 on 11/12/15.
 */
public enum PartitionLCP {

    CONNECT_TO_TV               (1,  "Connectto TV",        "connecttotv.com",  "https://www.connecttotv.com",  Language.ENGLISH),
    HOLLOR                      (8,  "Hollor",              "hollor.com",       "https://www.hollor.com",  Language.ENGLISH),
    VSHOO_LA                    (13, "Vshoo LA",            "",                 "https://www.vshoo.com",  Language.ENGLISH),
    VSHOO_YEREVAN               (14, "Vshoo Yerevan",       "",                 "https://www.vshoo.am",  Language.ARMENIAN),
    VSHOO_USA                   (15, "Vshoo USA",           "vshoo.com",        "https://www.vshoo.com",  Language.ENGLISH),
    VSHOO_ARMENIA               (16, "Vshoo Armenia",       "vshoo.am",         "https://www.vshoo.am",  Language.ARMENIAN),
    VSHOO_ARMENIA_REGIONS       (17, "Vshoo Armenia Regions","",                "https://www.vshoo.am",  Language.ARMENIAN),
    VSHOO_IRAN                  (21, "Vshoo Iran",          "vshoo.ir",         "https://www.vshoo.ir", Language.PERSIAN);

    PartitionLCP(int id, String name, String dns, String host, Language language) {
        this.id = id;
        this.name = name;
        this.dns = dns;
        this.host = host;
        this.language = language;

    }

    public static PartitionLCP getDefault() {
        return null;
    }

    public static synchronized PartitionLCP valueOf(final int id) {
        for (PartitionLCP partition : PartitionLCP.values()) {
            if (partition.getId() == id) {
                return partition;
            }
        }
        return getDefault();
    }

    public static synchronized PartitionLCP findCorrespondingPartitionLCP(final String serverName) {
        for (PartitionLCP partition : PartitionLCP.values()) {
            if(Utils.isEmpty(partition.getDns())){
                continue;
            }
            if (serverName.contains(partition.getDns())) {
                return partition;
            }
        }
        return null;
    }

    public static synchronized String getDNS(int partitionId){
        if(partitionId == CONNECT_TO_TV.getId()){
            return "connecttotv";
        } else if(partitionId == HOLLOR.getId()){
            return "hollor";
        } else {
            return "vshoo";
        }
    }

    public static synchronized String getHost(int partitionId){
        PartitionLCP partitionLCP = PartitionLCP.valueOf(partitionId);
        if(partitionLCP == null){
            return null;
        }
        return partitionLCP.getHost();
    }

    public static synchronized boolean isArmenian(int partitionId){
        if(partitionId == VSHOO_ARMENIA.getId() || partitionId == VSHOO_ARMENIA_REGIONS.getId() ||partitionId == VSHOO_YEREVAN.getId()){
            return true;
        }
        return false;
    }

    public static synchronized boolean isVshoo(int partitionId){
        if( partitionId == VSHOO_USA.getId() || partitionId == VSHOO_LA.getId() ||
            partitionId == VSHOO_IRAN.getId() ||
            partitionId == VSHOO_ARMENIA.getId() || partitionId == VSHOO_ARMENIA_REGIONS.getId() || partitionId == VSHOO_YEREVAN.getId()){
            return true;
        }
        return false;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDns() {
        return dns;
    }

    public Language getLanguage() {
        return language;
    }

    public String getHost() {
        return host;
    }

    private int id;
    private String name;
    private String dns;
    private String host;
    private Language language;

}
