package alserdar.widewall.models;

public class CountModel {

    String postKeyCount ;
    private int countReplies, countLikes, countAdds, countFavourites, countReport , countListeners;

    public CountModel() {
    }

    public CountModel(String postKeyCount, int countReplies, int countLikes, int countAdds, int countFavourites, int countReport, int countListeners) {
        this.postKeyCount = postKeyCount;
        this.countReplies = countReplies;
        this.countLikes = countLikes;
        this.countAdds = countAdds;
        this.countFavourites = countFavourites;
        this.countReport = countReport;
        this.countListeners = countListeners;
    }

    public String getPostKeyCount() {
        return postKeyCount;
    }

    public void setPostKeyCount(String postKeyCount) {
        this.postKeyCount = postKeyCount;
    }

    public int getCountReplies() {
        return countReplies;
    }

    public void setCountReplies(int countReplies) {
        this.countReplies = countReplies;
    }

    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }

    public int getCountAdds() {
        return countAdds;
    }

    public void setCountAdds(int countAdds) {
        this.countAdds = countAdds;
    }

    public int getCountFavourites() {
        return countFavourites;
    }

    public void setCountFavourites(int countFavourites) {
        this.countFavourites = countFavourites;
    }

    public int getCountReport() {
        return countReport;
    }

    public void setCountReport(int countReport) {
        this.countReport = countReport;
    }

    public int getCountListeners() {
        return countListeners;
    }

    public void setCountListeners(int countListeners) {
        this.countListeners = countListeners;
    }
}
