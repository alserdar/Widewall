package alserdar.widewall.models;

import java.util.Date;

public class UserModel {

    public UserModel() {
    }

    private String theUID ,  userName , handle , gender , country , town , age , relationship , aboutUser , linkUser , birthDate
            , id , phone , email ;
    private boolean online , star , globallyPerson , locallyPerson , paid , sus , privateAccount , hasPic;

    private boolean gotNotification , gotMessages;

    public boolean isGotMessages() {
        return gotMessages;
    }

    public void setGotMessages(boolean gotMessages) {
        this.gotMessages = gotMessages;
    }

    public boolean isGotNotification() {
        return gotNotification;
    }

    public void setGotNotification(boolean gotNotification) {
        this.gotNotification = gotNotification;
    }

    public String getLinkUser() {
        return linkUser;
    }

    public void setLinkUser(String linkUser) {
        this.linkUser = linkUser;
    }

    private int reportAccounts, getX ;

    private Date createAccount , lastJoin ;

    public boolean isHasPic() {
        return hasPic;
    }

    public void setHasPic(boolean hasPic) {
        this.hasPic = hasPic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTheUID() {
        return theUID;
    }

    public void setTheUID(String theUID) {
        this.theUID = theUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(String aboutUser) {
        this.aboutUser = aboutUser;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public boolean isGloballyPerson() {
        return globallyPerson;
    }

    public void setGloballyPerson(boolean globallyPerson) {
        this.globallyPerson = globallyPerson;
    }

    public boolean isLocallyPerson() {
        return locallyPerson;
    }

    public void setLocallyPerson(boolean locallyPerson) {
        this.locallyPerson = locallyPerson;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isSus() {
        return sus;
    }

    public void setSus(boolean sus) {
        this.sus = sus;
    }

    public boolean isPrivateAccount() {
        return privateAccount;
    }

    public void setPrivateAccount(boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

    public int getReportAccounts() {
        return reportAccounts;
    }

    public void setReportAccounts(int reportAccounts) {
        this.reportAccounts = reportAccounts;
    }

    public int getGetX() {
        return getX;
    }

    public void setGetX(int getX) {
        this.getX = getX;
    }

    public Date getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(Date createAccount) {
        this.createAccount = createAccount;
    }

    public Date getLastJoin() {
        return lastJoin;
    }

    public void setLastJoin(Date lastJoin) {
        this.lastJoin = lastJoin;
    }
}
