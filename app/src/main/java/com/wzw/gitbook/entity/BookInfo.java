package com.wzw.gitbook.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public class BookInfo {
    /**
     * id : quanke/think-in-java
     * status : published
     * name : think-in-java
     * title : Thinking in Java (Java 编程思想)
     * description : Thinking in Java (Java 编程思想)
     * public : true
     * template : base
     * topics : ["java"]
     * license : apache-2
     * language : zh
     * locked : false
     * cover : {"large":"/cover/book/quanke/think-in-java.jpg?build=1471970478211","small":"/cover/book/quanke/think-in-java.jpg?build=1471970478211&size=small"}
     * urls : {"git":"https://git.gitbook.com/quanke/think-in-java.git","access":"https://www.gitbook.com/book/quanke/think-in-java","homepage":"https://quanke.gitbooks.io/think-in-java/","read":"https://www.gitbook.com/read/book/quanke/think-in-java","edit":"https://www.gitbook.com/book/quanke/think-in-java/edit","content":"http://java.quanke.name/","download":{"epub":"https://www.gitbook.com/download/epub/book/quanke/think-in-java","mobi":"https://www.gitbook.com/download/mobi/book/quanke/think-in-java","pdf":"https://www.gitbook.com/download/pdf/book/quanke/think-in-java"}}
     * counts : {"stars":904,"subscriptions":117,"updates":23,"discussions":38,"changeRequests":0}
     * dates : {"build":"2016-08-23T16:41:18.211Z","created":"2016-05-04T04:06:30.991Z"}
     * permissions : {"edit":false,"admin":false,"important":false}
     * publish : {"ebooks":true,"defaultBranch":null,"builder":null}
     * author : {"id":"550c4f97e9a91c0300e9dd10","type":"User","username":"quanke","name":"quanke","location":"shanghai","website":"https://github.com/quanke","bio":"http://quanke.name\r\n","verified":true,"locked":false,"site_admin":false,"urls":{"profile":"https://www.gitbook.com/@quanke","stars":"https://www.gitbook.com/@quanke/starred","avatar":"https://avatars0.githubusercontent.com/quanke"},"permissions":{},"dates":{"created":"2015-03-20T16:49:27.165Z"},"counts":{},"github":{"username":"quanke"}}
     */

    private String id;
    private String status;
    private String name;
    private String title;
    private String description;
    @SerializedName("public")
    private boolean publicX;
    private String template;
    private String license;
    private String language;
    private boolean locked;
    private CoverBean cover;
    private UrlsBean urls;
    private CountsBean counts;
    private DatesBean dates;
    private PermissionsBean permissions;
    private PublishBean publish;
    private AuthorBean author;
    private List<String> topics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicX() {
        return publicX;
    }

    public void setPublicX(boolean publicX) {
        this.publicX = publicX;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public CoverBean getCover() {
        return cover;
    }

    public void setCover(CoverBean cover) {
        this.cover = cover;
    }

    public UrlsBean getUrls() {
        return urls;
    }

    public void setUrls(UrlsBean urls) {
        this.urls = urls;
    }

    public CountsBean getCounts() {
        return counts;
    }

    public void setCounts(CountsBean counts) {
        this.counts = counts;
    }

    public DatesBean getDates() {
        return dates;
    }

    public void setDates(DatesBean dates) {
        this.dates = dates;
    }

    public PermissionsBean getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionsBean permissions) {
        this.permissions = permissions;
    }

    public PublishBean getPublish() {
        return publish;
    }

    public void setPublish(PublishBean publish) {
        this.publish = publish;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public static class CoverBean {
        /**
         * large : /cover/book/quanke/think-in-java.jpg?build=1471970478211
         * small : /cover/book/quanke/think-in-java.jpg?build=1471970478211&size=small
         */

        private String large;
        private String small;

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }
    }

    public static class UrlsBean {
        /**
         * git : https://git.gitbook.com/quanke/think-in-java.git
         * access : https://www.gitbook.com/book/quanke/think-in-java
         * homepage : https://quanke.gitbooks.io/think-in-java/
         * read : https://www.gitbook.com/read/book/quanke/think-in-java
         * edit : https://www.gitbook.com/book/quanke/think-in-java/edit
         * content : http://java.quanke.name/
         * download : {"epub":"https://www.gitbook.com/download/epub/book/quanke/think-in-java","mobi":"https://www.gitbook.com/download/mobi/book/quanke/think-in-java","pdf":"https://www.gitbook.com/download/pdf/book/quanke/think-in-java"}
         */

        private String git;
        private String access;
        private String homepage;
        private String read;
        private String edit;
        private String content;
        private DownloadBean download;

        public String getGit() {
            return git;
        }

        public void setGit(String git) {
            this.git = git;
        }

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public String getHomepage() {
            return homepage;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }

        public String getRead() {
            return read;
        }

        public void setRead(String read) {
            this.read = read;
        }

        public String getEdit() {
            return edit;
        }

        public void setEdit(String edit) {
            this.edit = edit;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public DownloadBean getDownload() {
            return download;
        }

        public void setDownload(DownloadBean download) {
            this.download = download;
        }

        public static class DownloadBean {
            /**
             * epub : https://www.gitbook.com/download/epub/book/quanke/think-in-java
             * mobi : https://www.gitbook.com/download/mobi/book/quanke/think-in-java
             * pdf : https://www.gitbook.com/download/pdf/book/quanke/think-in-java
             */

            private String epub;
            private String mobi;
            private String pdf;

            public String getEpub() {
                return epub;
            }

            public void setEpub(String epub) {
                this.epub = epub;
            }

            public String getMobi() {
                return mobi;
            }

            public void setMobi(String mobi) {
                this.mobi = mobi;
            }

            public String getPdf() {
                return pdf;
            }

            public void setPdf(String pdf) {
                this.pdf = pdf;
            }
        }
    }

    public static class CountsBean {
        /**
         * stars : 904
         * subscriptions : 117
         * updates : 23
         * discussions : 38
         * changeRequests : 0
         */

        private int stars;
        private int subscriptions;
        private int updates;
        private int discussions;
        private int changeRequests;

        public int getStars() {
            return stars;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }

        public int getSubscriptions() {
            return subscriptions;
        }

        public void setSubscriptions(int subscriptions) {
            this.subscriptions = subscriptions;
        }

        public int getUpdates() {
            return updates;
        }

        public void setUpdates(int updates) {
            this.updates = updates;
        }

        public int getDiscussions() {
            return discussions;
        }

        public void setDiscussions(int discussions) {
            this.discussions = discussions;
        }

        public int getChangeRequests() {
            return changeRequests;
        }

        public void setChangeRequests(int changeRequests) {
            this.changeRequests = changeRequests;
        }
    }

    public static class DatesBean {
        /**
         * build : 2016-08-23T16:41:18.211Z
         * created : 2016-05-04T04:06:30.991Z
         */

        private String build;
        private String created;

        public String getBuild() {
            return build;
        }

        public void setBuild(String build) {
            this.build = build;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }

    public static class PermissionsBean {
        /**
         * edit : false
         * admin : false
         * important : false
         */

        private boolean edit;
        private boolean admin;
        private boolean important;

        public boolean isEdit() {
            return edit;
        }

        public void setEdit(boolean edit) {
            this.edit = edit;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public boolean isImportant() {
            return important;
        }

        public void setImportant(boolean important) {
            this.important = important;
        }
    }

    public static class PublishBean {
        /**
         * ebooks : true
         * defaultBranch : null
         * builder : null
         */

        private boolean ebooks;
        private Object defaultBranch;
        private Object builder;

        public boolean isEbooks() {
            return ebooks;
        }

        public void setEbooks(boolean ebooks) {
            this.ebooks = ebooks;
        }

        public Object getDefaultBranch() {
            return defaultBranch;
        }

        public void setDefaultBranch(Object defaultBranch) {
            this.defaultBranch = defaultBranch;
        }

        public Object getBuilder() {
            return builder;
        }

        public void setBuilder(Object builder) {
            this.builder = builder;
        }
    }

    public static class AuthorBean {
        /**
         * id : 550c4f97e9a91c0300e9dd10
         * type : User
         * username : quanke
         * name : quanke
         * location : shanghai
         * website : https://github.com/quanke
         * bio : http://quanke.name

         * verified : true
         * locked : false
         * site_admin : false
         * urls : {"profile":"https://www.gitbook.com/@quanke","stars":"https://www.gitbook.com/@quanke/starred","avatar":"https://avatars0.githubusercontent.com/quanke"}
         * permissions : {}
         * dates : {"created":"2015-03-20T16:49:27.165Z"}
         * counts : {}
         * github : {"username":"quanke"}
         */

        private String id;
        private String type;
        private String username;
        private String name;
        private String location;
        private String website;
        private String bio;
        private boolean verified;
        private boolean locked;
        private boolean site_admin;
        private UrlsBeanX urls;
        private PermissionsBeanX permissions;
        private DatesBeanX dates;
        private CountsBeanX counts;
        private GithubBean github;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public boolean isSite_admin() {
            return site_admin;
        }

        public void setSite_admin(boolean site_admin) {
            this.site_admin = site_admin;
        }

        public UrlsBeanX getUrls() {
            return urls;
        }

        public void setUrls(UrlsBeanX urls) {
            this.urls = urls;
        }

        public PermissionsBeanX getPermissions() {
            return permissions;
        }

        public void setPermissions(PermissionsBeanX permissions) {
            this.permissions = permissions;
        }

        public DatesBeanX getDates() {
            return dates;
        }

        public void setDates(DatesBeanX dates) {
            this.dates = dates;
        }

        public CountsBeanX getCounts() {
            return counts;
        }

        public void setCounts(CountsBeanX counts) {
            this.counts = counts;
        }

        public GithubBean getGithub() {
            return github;
        }

        public void setGithub(GithubBean github) {
            this.github = github;
        }

        public static class UrlsBeanX {
            private String avatar;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }

        public static class PermissionsBeanX {
        }

        public static class DatesBeanX {
        }

        public static class CountsBeanX {
        }

        public static class GithubBean {
            /**
             * username : quanke
             */

            private String username;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
