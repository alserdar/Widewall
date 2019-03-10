package alserdar.widewall.models;

public class MessagesModel {

    public MessagesModel() {
    }

    private String theUID , nickName , handle , country , hisUID , hisNickName , hisHandle , theMessage , picPath , picName ,
    hisCountry , messageDate , messageTime ;

    public MessagesModel(String theUID, String nickName, String handle,
                         String country, String hisUID, String hisNickName,
                         String hisHandle, String hisCountry,String theMessage,String picPath , String picName ,
                         String messageDate, String messageTime) {
        this.theUID = theUID;
        this.nickName = nickName;
        this.handle = handle;
        this.country = country;
        this.hisUID = hisUID;
        this.hisNickName = hisNickName;
        this.hisHandle = hisHandle;
        this.theMessage = theMessage;
        this.picPath = picPath ;
        this.picName = picName ;
        this.hisCountry = hisCountry;
        this.messageDate = messageDate;
        this.messageTime = messageTime;

    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getTheUID() {
        return theUID;
    }

    public void setTheUID(String theUID) {
        this.theUID = theUID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHisUID() {
        return hisUID;
    }

    public void setHisUID(String hisUID) {
        this.hisUID = hisUID;
    }

    public String getHisNickName() {
        return hisNickName;
    }

    public void setHisNickName(String hisNickName) {
        this.hisNickName = hisNickName;
    }

    public String getHisHandle() {
        return hisHandle;
    }

    public void setHisHandle(String hisHandle) {
        this.hisHandle = hisHandle;
    }

    public String getTheMessage() {
        return theMessage;
    }

    public void setTheMessage(String theMessage) {
        this.theMessage = theMessage;
    }

    public String getHisCountry() {
        return hisCountry;
    }

    public void setHisCountry(String hisCountry) {
        this.hisCountry = hisCountry;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        messageTime = messageTime;
    }
}
