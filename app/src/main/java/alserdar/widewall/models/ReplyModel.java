package alserdar.widewall.models;

public class ReplyModel {

    public ReplyModel() {
    }

    private String thePosterUID , thePost , thePostKey , thePosterNickName , thePosterHandle , thePosterCountry , whichRoom ,
    theReplierUID , theReply , theReplierNickName , theReplierHandle , theReplierCountry , theReplyKey ,  theReplyCountKey;

    private String addKey, likeKey , favouriteKey ;

    private boolean reported ;

    private String replyDate , replyTime;

    public ReplyModel(String thePosterUID, String thePost, String thePostKey,
                      String thePosterNickName, String thePosterHandle,
                      String thePosterCountry, String whichRoom, String theReplierUID,
                      String theReply, String theReplierNickName, String theReplierHandle,
                      String theReplierCountry, String theReplyKey, String theReplyCountKey,
                      String addKey, String likeKey, String favouriteKey, boolean reported, String replyDate , String replyTime) {
        this.thePosterUID = thePosterUID;
        this.thePost = thePost;
        this.thePostKey = thePostKey;
        this.thePosterNickName = thePosterNickName;
        this.thePosterHandle = thePosterHandle;
        this.thePosterCountry = thePosterCountry;
        this.whichRoom = whichRoom;
        this.theReplierUID = theReplierUID;
        this.theReply = theReply;
        this.theReplierNickName = theReplierNickName;
        this.theReplierHandle = theReplierHandle;
        this.theReplierCountry = theReplierCountry;
        this.theReplyKey = theReplyKey;
        this.theReplyCountKey = theReplyCountKey;
        this.addKey = addKey;
        this.likeKey = likeKey;
        this.favouriteKey = favouriteKey;
        this.reported = reported;
        this.replyDate = replyDate;
        this.replyTime = replyTime;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getThePosterUID() {
        return thePosterUID;
    }

    public void setThePosterUID(String thePosterUID) {
        this.thePosterUID = thePosterUID;
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

    public String getThePosterCountry() {
        return thePosterCountry;
    }

    public void setThePosterCountry(String thePosterCountry) {
        this.thePosterCountry = thePosterCountry;
    }

    public String getWhichRoom() {
        return whichRoom;
    }

    public void setWhichRoom(String whichRoom) {
        this.whichRoom = whichRoom;
    }

    public String getTheReplierUID() {
        return theReplierUID;
    }

    public void setTheReplierUID(String theReplierUID) {
        this.theReplierUID = theReplierUID;
    }

    public String getTheReply() {
        return theReply;
    }

    public void setTheReply(String theReply) {
        this.theReply = theReply;
    }

    public String getTheReplierNickName() {
        return theReplierNickName;
    }

    public void setTheReplierNickName(String theReplierNickName) {
        this.theReplierNickName = theReplierNickName;
    }

    public String getTheReplierHandle() {
        return theReplierHandle;
    }

    public void setTheReplierHandle(String theReplierHandle) {
        this.theReplierHandle = theReplierHandle;
    }

    public String getTheReplierCountry() {
        return theReplierCountry;
    }

    public void setTheReplierCountry(String theReplierCountry) {
        this.theReplierCountry = theReplierCountry;
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

    public String getAddKey() {
        return addKey;
    }

    public void setAddKey(String addKey) {
        this.addKey = addKey;
    }

    public String getLikeKey() {
        return likeKey;
    }

    public void setLikeKey(String likeKey) {
        this.likeKey = likeKey;
    }

    public String getFavouriteKey() {
        return favouriteKey;
    }

    public void setFavouriteKey(String favouriteKey) {
        this.favouriteKey = favouriteKey;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }
}
