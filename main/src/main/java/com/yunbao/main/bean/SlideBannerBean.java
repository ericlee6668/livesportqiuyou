package com.yunbao.main.bean;

import java.util.List;

public class SlideBannerBean {

        /**
         * position : 1
         * items : [{"id":"11","slide_id":"1","title":"测","image":"admin/20201019/f3f96e1508ad4c924b975ad531f0cc4a.jpg","url":"","target":"","description":"","content":"","more":null}]
         */

        private int position;
        private List<ItemsBean> items;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * id : 11
             * slide_id : 1
             * title : 测
             * image : admin/20201019/f3f96e1508ad4c924b975ad531f0cc4a.jpg
             * url :
             * target :
             * description :
             * content :
             * more : null
             */

            private String id;
            private String slide_id;
            private String title;
            private String image;
            private String url;
            private String target;
            private String description;
            private String content;
            private String more;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSlide_id() {
                return slide_id;
            }

            public void setSlide_id(String slide_id) {
                this.slide_id = slide_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getMore() {
                return more;
            }

            public void setMore(String more) {
                this.more = more;
            }
        }

}
