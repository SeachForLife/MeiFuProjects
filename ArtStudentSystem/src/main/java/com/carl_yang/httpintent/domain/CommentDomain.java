package com.carl_yang.httpintent.domain;

import java.util.List;

/**
 * Created by Loren Yang on 2017/10/31.
 */

public class CommentDomain {

    private String success;

    private String voteCount;
    private List<Comment_content> cList;

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Comment_content> getcList() {
        return cList;
    }

    public void setcList(List<Comment_content> cList) {
        this.cList = cList;
    }

    public class Comment_content{
        private String teacName;
        private String createTime;
        private String content;

        public String getTeacName() {
            return teacName;
        }

        public void setTeacName(String teacName) {
            this.teacName = teacName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
