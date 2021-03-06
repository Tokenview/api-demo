package com.tokenview.enums;

public enum Version {
    BTC(1);

    private Integer version;

    Version(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }
}
