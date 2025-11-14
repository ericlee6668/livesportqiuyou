package com.yunbao.common.http;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;




public class DataNewX implements Serializable {


    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public String getFirstPageUrl() {
        return firstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        this.firstPageUrl = firstPageUrl;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public String getLastPageUrl() {
        return lastPageUrl;
    }

    public void setLastPageUrl(String lastPageUrl) {
        this.lastPageUrl = lastPageUrl;
    }

    public List<LinksDTO> getLinks() {
        return links;
    }

    public void setLinks(List<LinksDTO> links) {
        this.links = links;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPerPage() {
        return perPage;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(Object prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @SerializedName("current_page")
    private String currentPage;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("first_page_url")
    private String firstPageUrl;
    @SerializedName("from")
    private String from;
    @SerializedName("last_page")
    private String lastPage;
    @SerializedName("last_page_url")
    private String lastPageUrl;
    @SerializedName("links")
    private List<LinksDTO> links;
    @SerializedName("next_page_url")
    private String nextPageUrl;
    @SerializedName("path")
    private String path;
    @SerializedName("per_page")
    private String perPage;
    @SerializedName("prev_page_url")
    private Object prevPageUrl;
    @SerializedName("to")
    private String to;
    @SerializedName("total")
    private String total;


    public static class DataDTO implements Serializable {
        @SerializedName("uid")
        private String uid;
        @SerializedName("showid")
        private String showid;
        @SerializedName("islive")
        private String islive;
        @SerializedName("starttime")
        private String starttime;
        @SerializedName("title")
        private String title;
        @SerializedName("province")
        private String province;
        @SerializedName("city")
        private String city;
        @SerializedName("txzb_order")
        private String txzbOrder;
        @SerializedName("stream")
        private String stream;
        @SerializedName("thumb")
        private String thumb;
        @SerializedName("pull")
        private String pull;
        @SerializedName("third_pull")
        private String thirdPull;
        @SerializedName("lng")
        private String lng;
        @SerializedName("lat")
        private String lat;
        @SerializedName("type")
        private String type;
        @SerializedName("type_val")
        private String typeVal;
        @SerializedName("isvideo")
        private String isvideo;
        @SerializedName("wy_cid")
        private String wyCid;
        @SerializedName("goodnum")
        private String goodnum;
        @SerializedName("anyway")
        private String anyway;
        @SerializedName("liveclassid")
        private String liveclassid;
        @SerializedName("hotvotes")
        private String hotvotes;
        @SerializedName("pkuid")
        private String pkuid;
        @SerializedName("pkstream")
        private String pkstream;
        @SerializedName("ismic")
        private String ismic;
        @SerializedName("ishot")
        private String ishot;
        @SerializedName("isrecommend")
        private String isrecommend;
        @SerializedName("deviceinfo")
        private String deviceinfo;
        @SerializedName("isshop")
        private String isshop;
        @SerializedName("game_action")
        private String gameAction;
        @SerializedName("banker_coin")
        private String bankerCoin;
        @SerializedName("isoff")
        private String isoff;
        @SerializedName("offtime")
        private String offtime;
        @SerializedName("recom_sort")
        private String recomSort;
        @SerializedName("notice")
        private String notice;
        @SerializedName("match_id")
        private String matchId;
        @SerializedName("pic_full_url")
        private Object picFullUrl;
        @SerializedName("isrecord")
        private String isrecord;
        @SerializedName("recordid")
        private String recordid;
        @SerializedName("is_chat_off")
        private String isChatOff;
        @SerializedName("is_manual_close")
        private String isManualClose;

        @SerializedName("is_hot_live")
        private String isHotLive;
        @SerializedName("user")
        private UserDTO user;
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getShowid() {
            return showid;
        }

        public void setShowid(String showid) {
            this.showid = showid;
        }

        public String getIslive() {
            return islive;
        }

        public void setIslive(String islive) {
            this.islive = islive;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTxzbOrder() {
            return txzbOrder;
        }

        public void setTxzbOrder(String txzbOrder) {
            this.txzbOrder = txzbOrder;
        }

        public String getStream() {
            return stream;
        }

        public void setStream(String stream) {
            this.stream = stream;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getPull() {
            return pull;
        }

        public void setPull(String pull) {
            this.pull = pull;
        }

        public String getThirdPull() {
            return thirdPull;
        }

        public void setThirdPull(String thirdPull) {
            this.thirdPull = thirdPull;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeVal() {
            return typeVal;
        }

        public void setTypeVal(String typeVal) {
            this.typeVal = typeVal;
        }

        public String getIsvideo() {
            return isvideo;
        }

        public void setIsvideo(String isvideo) {
            this.isvideo = isvideo;
        }

        public String getWyCid() {
            return wyCid;
        }

        public void setWyCid(String wyCid) {
            this.wyCid = wyCid;
        }

        public String getGoodnum() {
            return goodnum;
        }

        public void setGoodnum(String goodnum) {
            this.goodnum = goodnum;
        }

        public String getAnyway() {
            return anyway;
        }

        public void setAnyway(String anyway) {
            this.anyway = anyway;
        }

        public String getLiveclassid() {
            return liveclassid;
        }

        public void setLiveclassid(String liveclassid) {
            this.liveclassid = liveclassid;
        }

        public String getHotvotes() {
            return hotvotes;
        }

        public void setHotvotes(String hotvotes) {
            this.hotvotes = hotvotes;
        }

        public String getPkuid() {
            return pkuid;
        }

        public void setPkuid(String pkuid) {
            this.pkuid = pkuid;
        }

        public String getPkstream() {
            return pkstream;
        }

        public void setPkstream(String pkstream) {
            this.pkstream = pkstream;
        }

        public String getIsmic() {
            return ismic;
        }

        public void setIsmic(String ismic) {
            this.ismic = ismic;
        }

        public String getIshot() {
            return ishot;
        }

        public void setIshot(String ishot) {
            this.ishot = ishot;
        }

        public String getIsrecommend() {
            return isrecommend;
        }

        public void setIsrecommend(String isrecommend) {
            this.isrecommend = isrecommend;
        }

        public String getDeviceinfo() {
            return deviceinfo;
        }

        public void setDeviceinfo(String deviceinfo) {
            this.deviceinfo = deviceinfo;
        }

        public String getIsshop() {
            return isshop;
        }

        public void setIsshop(String isshop) {
            this.isshop = isshop;
        }

        public String getGameAction() {
            return gameAction;
        }

        public void setGameAction(String gameAction) {
            this.gameAction = gameAction;
        }

        public String getBankerCoin() {
            return bankerCoin;
        }

        public void setBankerCoin(String bankerCoin) {
            this.bankerCoin = bankerCoin;
        }

        public String getIsoff() {
            return isoff;
        }

        public void setIsoff(String isoff) {
            this.isoff = isoff;
        }

        public String getOfftime() {
            return offtime;
        }

        public void setOfftime(String offtime) {
            this.offtime = offtime;
        }

        public String getRecomSort() {
            return recomSort;
        }

        public void setRecomSort(String recomSort) {
            this.recomSort = recomSort;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public Object getPicFullUrl() {
            return picFullUrl;
        }

        public void setPicFullUrl(Object picFullUrl) {
            this.picFullUrl = picFullUrl;
        }

        public String getIsrecord() {
            return isrecord;
        }

        public void setIsrecord(String isrecord) {
            this.isrecord = isrecord;
        }

        public String getRecordid() {
            return recordid;
        }

        public void setRecordid(String recordid) {
            this.recordid = recordid;
        }

        public String getIsChatOff() {
            return isChatOff;
        }

        public void setIsChatOff(String isChatOff) {
            this.isChatOff = isChatOff;
        }

        public String getIsManualClose() {
            return isManualClose;
        }

        public void setIsManualClose(String isManualClose) {
            this.isManualClose = isManualClose;
        }

        public String getIsHotLive() {
            return isHotLive;
        }

        public void setIsHotLive(String isHotLive) {
            this.isHotLive = isHotLive;
        }

        public UserDTO getUser() {
            return user;
        }

        public void setUser(UserDTO user) {
            this.user = user;
        }



        public static class UserDTO  implements Serializable{
            @SerializedName("id")
            private String id;
            @SerializedName("user_nicename")
            private String userNicename;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("avatar_thumb")
            private String avatarThumb;
            @SerializedName("viewnum")
            private String viewnum;
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserNicename() {
                return userNicename;
            }

            public void setUserNicename(String userNicename) {
                this.userNicename = userNicename;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getAvatarThumb() {
                return avatarThumb;
            }

            public void setAvatarThumb(String avatarThumb) {
                this.avatarThumb = avatarThumb;
            }

            public String getViewnum() {
                return viewnum;
            }

            public void setViewnum(String viewnum) {
                this.viewnum = viewnum;
            }
        }
    }


    public static class LinksDTO implements Serializable{
        @SerializedName("url")
        private Object url;
        @SerializedName("label")
        private String label;
        @SerializedName("active")
        private Boolean active;
        public Object getUrl() {
            return url;
        }

        public void setUrl(Object url) {
            this.url = url;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }


    }
}
