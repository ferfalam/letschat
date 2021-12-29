package com.faridcodeur.letschat.entities;

public class Setting {
    private String nameInfo;
    private String info;
    private String description;
    private int icon;

    public Setting() {
    }

    public Setting(String nameInfo, String info, String description, int icon) {
        this.nameInfo = nameInfo;
        this.info = info;
        this.description = description;
        this.icon = icon;
    }

    public Setting(String nameInfo, String info, int icon) {
        this.nameInfo = nameInfo;
        this.info = info;
        this.icon = icon;
    }

    public String getNameInfo() {
        return nameInfo;
    }

    public void setNameInfo(String nameInfo) {
        this.nameInfo = nameInfo;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "nameInfo='" + nameInfo + '\'' +
                ", info='" + info + '\'' +
                ", description='" + description + '\'' +
                ", icon=" + icon +
                '}';
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}