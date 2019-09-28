package com.dagf.uweyt3.livestreaming;

public class Message {

    public boolean isAd;

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public String getName_of() {
        return name_of;
    }

    public void setName_of(String name_of) {
        this.name_of = name_of;
    }


    public String getUrlProfilePic() {
        return urlProfilePic;
    }

    public void setUrlProfilePic(String urlProfilePic) {
        this.urlProfilePic = urlProfilePic;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    private String mesg;

    public String getDescmedia() {
        return descmedia;
    }

    public void setDescmedia(String descmedia) {
        this.descmedia = descmedia;
    }

    private String descmedia;
    private String name_of;
    private String urlProfilePic;

    private String type_mensaje;

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    private String isAdmin = "false";

    public String getUrl_img_media() {
        return url_img_media;
    }

    public void setUrl_img_media(String url_img_media) {
        this.url_img_media = url_img_media;
    }

    private String url_img_media;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String action;


    public String getDataToClick() {
        return dataToClick;
    }

    public void setDataToClick(String dataToClick) {
        this.dataToClick = dataToClick;
    }

    private String dataToClick;

    public String getSnap() {
        return snap;
    }

    public void setSnap(String snap) {
        this.snap = snap;
    }

    private String snap;
}
