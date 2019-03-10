package alserdar.widewall.models;

public class PostModel {

    public PostModel() {
    }

    private String thePosterUID, thePosterMessage, thePosterNickname,
            thePosterHandle, currentCountry , postCountry , whichRoom,
            audioStatus, picPath , audioPath , theMainPlanetKeyPost , theKeyPost , countKeyPost;

    private String addKey, likeKey , favouriteKey ;

    private boolean reported ;

    private String thePostDate  , thePostTime;


    public PostModel(String thePosterUID, String thePosterMessage, String thePosterNickname,
                     String thePosterHandle, String currentCountry, String postCountry,
                     String whichRoom , String voiceNotes, String picPath, String audioPath,
                     String theMainPlanetKeyPost, String theKeyPost , String countKeyPost ,
                     boolean reported, String thePostDate , String thePostTime , String addKey, String likeKey , String favouriteKey) {
        this.thePosterUID = thePosterUID;
        this.thePosterMessage = thePosterMessage;
        this.thePosterNickname = thePosterNickname;
        this.thePosterHandle = thePosterHandle;
        this.currentCountry = currentCountry;
        this.postCountry = postCountry;
        this.whichRoom = whichRoom ;
        this.audioStatus = voiceNotes;
        this.picPath = picPath;
        this.audioPath = audioPath;
        this.theMainPlanetKeyPost = theMainPlanetKeyPost;
        this.theKeyPost = theKeyPost ;
        this.countKeyPost = countKeyPost ;
        this.reported = reported;
        this.thePostDate = thePostDate;
        this.thePostTime = thePostTime;
        this.addKey = addKey;
        this.likeKey = likeKey ;
        this.favouriteKey = favouriteKey ;
    }

    public String getThePostTime() {
        return thePostTime;
    }

    public void setThePostTime(String thePostTime) {
        this.thePostTime = thePostTime;
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
        favouriteKey = favouriteKey;
    }

    public String getCountKeyPost() {
        return countKeyPost;
    }

    public void setCountKeyPost(String countKeyPost) {
        this.countKeyPost = countKeyPost;
    }

    public String getTheKeyPost() {
        return theKeyPost;
    }

    public void setTheKeyPost(String theKeyPost) {
        this.theKeyPost = theKeyPost;
    }

    public String getWhichRoom() {
        return whichRoom;
    }

    public void setWhichRoom(String whichRoom) {
        this.whichRoom = whichRoom;
    }

    public String getThePosterUID() {
        return thePosterUID;
    }

    public void setThePosterUID(String thePosterUID) {
        this.thePosterUID = thePosterUID;
    }

    public String getThePosterMessage() {
        return thePosterMessage;
    }

    public void setThePosterMessage(String thePosterMessage) {
        this.thePosterMessage = thePosterMessage;
    }

    public String getThePosterNickname() {
        return thePosterNickname;
    }

    public void setThePosterNickname(String thePosterNickname) {
        this.thePosterNickname = thePosterNickname;
    }

    public String getThePosterHandle() {
        return thePosterHandle;
    }

    public void setThePosterHandle(String thePosterHandle) {
        this.thePosterHandle = thePosterHandle;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getPostCountry() {
        return postCountry;
    }

    public void setPostCountry(String postCountry) {
        this.postCountry = postCountry;
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

    public String getTheMainPlanetKeyPost() {
        return theMainPlanetKeyPost;
    }

    public void setTheMainPlanetKeyPost(String theMainPlanetKeyPost) {
        this.theMainPlanetKeyPost = theMainPlanetKeyPost;
    }
    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }

    public Object getThePostDate() {
        return thePostDate;
    }

    public void setThePostDate(String thePostDate) {
        this.thePostDate = thePostDate;
    }
}
