package alserdar.widewall.models;

public class NotificationModel {

    public NotificationModel() {
    }

    //for functions
    private  String thePosterUID ,thePosterNickName , thePosterHandle ,
            thePost , thePostKey , whichRoom,
            NotificationerUID , NotificationType ,
            NotificationerName , NotificationerHandle ,
            theReply , theReplyKey ,  theReplyCountKey , audioStatus, picPath , audioPath  ;

    private String theDate  , theTime;

    public NotificationModel(String thePosterUID, String thePosterNickName,
                             String thePosterHandle, String thePost, String thePostKey,
                             String whichRoom, String notificationerUID,
                             String notificationType, String notificationerName,
                             String notificationerHandle, String theReply,
                             String theReplyKey, String theReplyCountKey ,
                             String audioStatus ,String picPath ,String audioPath , String theDate  , String theTime)
    {
        this.thePosterUID = thePosterUID;
        this.thePosterNickName = thePosterNickName;
        this.thePosterHandle = thePosterHandle;
        this.thePost = thePost;
        this.thePostKey = thePostKey;
        this.whichRoom = whichRoom;
        NotificationerUID = notificationerUID;
        NotificationType = notificationType;
        NotificationerName = notificationerName;
        NotificationerHandle = notificationerHandle;
        this.theReply = theReply;
        this.theReplyKey = theReplyKey;
        this.theReplyCountKey = theReplyCountKey;
        this.audioStatus = audioStatus ;
        this.picPath = picPath ;
        this.audioPath = audioPath ;
        this.theDate = theDate ;
        this.theTime = theTime ;

    }

    public String getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(String audioStatus) {
        this.audioStatus = audioStatus;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getThePosterUID() {
        return thePosterUID;
    }

    public void setThePosterUID(String thePosterUID) {
        this.thePosterUID = thePosterUID;
    }

    public String getThePosterNickName() {
        return thePosterNickName;
    }

    public void setThePosterNickName(String thePosterNickName) {
        this.thePosterNickName = thePosterNickName;
    }

    public String getThePosterHandle() {
        return thePosterHandle;
    }

    public void setThePosterHandle(String thePosterHandle) {
        this.thePosterHandle = thePosterHandle;
    }

    public String getThePost() {
        return thePost;
    }

    public void setThePost(String thePost) {
        this.thePost = thePost;
    }

    public String getThePostKey() {
        return thePostKey;
    }

    public void setThePostKey(String thePostKey) {
        this.thePostKey = thePostKey;
    }

    public String getWhichRoom() {
        return whichRoom;
    }

    public void setWhichRoom(String whichRoom) {
        this.whichRoom = whichRoom;
    }

    public String getNotificationerUID() {
        return NotificationerUID;
    }

    public void setNotificationerUID(String notificationerUID) {
        NotificationerUID = notificationerUID;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String notificationType) {
        NotificationType = notificationType;
    }

    public String getNotificationerName() {
        return NotificationerName;
    }

    public void setNotificationerName(String notificationerName) {
        NotificationerName = notificationerName;
    }

    public String getNotificationerHandle() {
        return NotificationerHandle;
    }

    public void setNotificationerHandle(String notificationerHandle) {
        NotificationerHandle = notificationerHandle;
    }

    public String getTheReply() {
        return theReply;
    }

    public void setTheReply(String theReply) {
        this.theReply = theReply;
    }

    public String getTheReplyKey() {
        return theReplyKey;
    }

    public void setTheReplyKey(String theReplyKey) {
        this.theReplyKey = theReplyKey;
    }

    public String getTheReplyCountKey() {
        return theReplyCountKey;
    }

    public void setTheReplyCountKey(String theReplyCountKey) {
        this.theReplyCountKey = theReplyCountKey;
    }

    public String getTheDate() {
        return theDate;
    }

    public void setTheDate(String theDate) {
        this.theDate = theDate;
    }

    public String getTheTime() {
        return theTime;
    }

    public void setTheTime(String theTime) {
        this.theTime = theTime;
    }
}
