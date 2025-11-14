package com.yunbao.common.bean;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/2 14:31
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class GameOpening {
    private List<MatchIndex> eu;
    private List<MatchIndex> bs;
    private List<MatchIndex> asia;

    public List<MatchIndex> getEu() {
        return eu;
    }

    public void setEu(List<MatchIndex> eu) {
        this.eu = eu;
    }

    public List<MatchIndex> getBs() {
        return bs;
    }

    public void setBs(List<MatchIndex> bs) {
        this.bs = bs;
    }

    public List<MatchIndex> getAsia() {
        return asia;
    }

    public void setAsia(List<MatchIndex> asia) {
        this.asia = asia;
    }
}
