package alserdar.widewall.models;

public class MessagesListModel {

    public MessagesListModel() {
    }

    private String theUID , id , nickNAme , handle , country , messageDate , messageTime , lastUIDTalkedTo , lastMessage;

    private boolean readIt ;

    public MessagesListModel(String theUID, String id, String nickNAme,
                             String handle, String country, String messageDate,
                             String messageTime , String lastUIDTalkedTo  , boolean readIt) {
        this.theUID = theUID;
        this.id = id;
        this.nickNAme = nickNAme;
        this.handle = handle;
        this.country = country;
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.lastUIDTalkedTo = lastUIDTalkedTo;
        this.readIt = readIt ;
    }


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastUIDTalkedTo() {
        return lastUIDTalkedTo;
    }

    public void setLastUIDTalkedTo(String lastUIDTalkedTo) {
        this.lastUIDTalkedTo = lastUIDTalkedTo;
    }

    public boolean isReadIt() {
        return readIt;
    }

    public void setReadIt(boolean readIt) {
        this.readIt = readIt;
    }

    public String getTheUID() {
        return theUID;
    }

    public void setTheUID(String theUID) {
        this.theUID = theUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickNAme() {
        return nickNAme;
    }

    public void setNickNAme(String nickNAme) {
        this.nickNAme = nickNAme;
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
        this.messageTime = messageTime;
    }
}
