package alserdar.widewall.models;

public class ListenModel {

    public ListenModel() {
    }

    private String theUID , nickName , handle , keyPost , audioPath , listenerUID , listenerNickname , listenerHandle ;
    boolean listened ;

    public ListenModel(String theUID, String nickName, String handle,
                       String keyPost, String audioPath, String listenerUID,
                       String listenerNickname, String listenerHandle, boolean listened) {
        this.theUID = theUID;
        this.nickName = nickName;
        this.handle = handle;
        this.keyPost = keyPost;
        this.audioPath = audioPath;
        this.listenerUID = listenerUID;
        this.listenerNickname = listenerNickname;
        this.listenerHandle = listenerHandle;
        this.listened = listened;
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

    public String getKeyPost() {
        return keyPost;
    }

    public void setKeyPost(String keyPost) {
        this.keyPost = keyPost;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getListenerUID() {
        return listenerUID;
    }

    public void setListenerUID(String listenerUID) {
        this.listenerUID = listenerUID;
    }

    public String getListenerNickname() {
        return listenerNickname;
    }

    public void setListenerNickname(String listenerNickname) {
        this.listenerNickname = listenerNickname;
    }

    public String getListenerHandle() {
        return listenerHandle;
    }

    public void setListenerHandle(String listenerHandle) {
        this.listenerHandle = listenerHandle;
    }

    public boolean isListened() {
        return listened;
    }

    public void setListened(boolean listened) {
        this.listened = listened;
    }
}
