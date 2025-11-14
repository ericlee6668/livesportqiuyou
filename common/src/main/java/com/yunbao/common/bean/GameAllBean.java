package com.yunbao.common.bean;

import java.util.List;

public class GameAllBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":{"today":[{"id":"8","league_id":"634","match_id":"66876","status":"0","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-24","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"V3","team_a_logo":"https://qn.feijing88.com/egame/lol/team/b07da61aeb6ed14990378d5265540a26.png","team_b_name":"UOL","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/5d185f59b2f24ba799daa6d447ba0191.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0}],"tomorrow":[],"over":[{"id":"10","league_id":"634","match_id":"3798","status":"0","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-23","starttime":"23:09","league_name":"S10 世界总决赛","team_a_name":"UOL","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/5d185f59b2f24ba799daa6d447ba0191.png","team_b_name":"PSG","team_b_logo":"https://qn.feijing88.com/egame/lol/team/bc0e8dc69590e5126feb9023363efe0f.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"9","league_id":"634","match_id":"66877","status":"0","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-23","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"LGD","team_a_logo":"https://qn.feijing88.com/egame/lol/team/fc6b5787b145e075b751c29f1497904e.png","team_b_name":"R7","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/0c1557b4f5eb4d1c86c55ce49117afb5.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"1","league_id":"634","match_id":"66869","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"MAD","team_a_logo":"https://qn.feijing88.com/egame/lol/team/6be30c2bf2faef88f0c2f3c44af095d2.png","team_b_name":"ITZ","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/20190701/f92bd683b3394c22b6552094f8809911.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"2","league_id":"634","match_id":"66861","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"PSG","team_a_logo":"https://qn.feijing88.com/egame/lol/team/bc0e8dc69590e5126feb9023363efe0f.png","team_b_name":"R7","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/0c1557b4f5eb4d1c86c55ce49117afb5.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"3","league_id":"634","match_id":"66862","status":"1","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"LGC","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/864196c825504efc8bb97fc7dfc10a14.png","team_b_name":"ITZ","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/20190701/f92bd683b3394c22b6552094f8809911.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"4","league_id":"634","match_id":"66863","status":"1","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"LGD","team_a_logo":"https://qn.feijing88.com/egame/lol/team/fc6b5787b145e075b751c29f1497904e.png","team_b_name":"PSG","team_b_logo":"https://qn.feijing88.com/egame/lol/team/bc0e8dc69590e5126feb9023363efe0f.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"5","league_id":"634","match_id":"66864","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"TL","team_a_logo":"https://qn.feijing88.com/egame/lol/team/59251a2c32c4a6f0592aaa25fe92c8a0.png","team_b_name":"MAD","team_b_logo":"https://qn.feijing88.com/egame/lol/team/6be30c2bf2faef88f0c2f3c44af095d2.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"6","league_id":"634","match_id":"66865","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"R7","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/0c1557b4f5eb4d1c86c55ce49117afb5.png","team_b_name":"V3","team_b_logo":"https://qn.feijing88.com/egame/lol/team/b07da61aeb6ed14990378d5265540a26.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"7","league_id":"634","match_id":"66870","status":"1","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"ITZ","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/20190701/f92bd683b3394c22b6552094f8809911.png","team_b_name":"SUP","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/824a28139cd34f9ca5e7b4da0fe327d9.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0}]}}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 0
         * msg :
         * info : {"today":[{"id":"8","league_id":"634","match_id":"66876","status":"0","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-24","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"V3","team_a_logo":"https://qn.feijing88.com/egame/lol/team/b07da61aeb6ed14990378d5265540a26.png","team_b_name":"UOL","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/5d185f59b2f24ba799daa6d447ba0191.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0}],"tomorrow":[],"over":[{"id":"10","league_id":"634","match_id":"3798","status":"0","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-23","starttime":"23:09","league_name":"S10 世界总决赛","team_a_name":"UOL","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/5d185f59b2f24ba799daa6d447ba0191.png","team_b_name":"PSG","team_b_logo":"https://qn.feijing88.com/egame/lol/team/bc0e8dc69590e5126feb9023363efe0f.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"9","league_id":"634","match_id":"66877","status":"0","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-23","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"LGD","team_a_logo":"https://qn.feijing88.com/egame/lol/team/fc6b5787b145e075b751c29f1497904e.png","team_b_name":"R7","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/0c1557b4f5eb4d1c86c55ce49117afb5.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"1","league_id":"634","match_id":"66869","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"MAD","team_a_logo":"https://qn.feijing88.com/egame/lol/team/6be30c2bf2faef88f0c2f3c44af095d2.png","team_b_name":"ITZ","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/20190701/f92bd683b3394c22b6552094f8809911.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"2","league_id":"634","match_id":"66861","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"PSG","team_a_logo":"https://qn.feijing88.com/egame/lol/team/bc0e8dc69590e5126feb9023363efe0f.png","team_b_name":"R7","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/0c1557b4f5eb4d1c86c55ce49117afb5.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"3","league_id":"634","match_id":"66862","status":"1","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"LGC","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/864196c825504efc8bb97fc7dfc10a14.png","team_b_name":"ITZ","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/20190701/f92bd683b3394c22b6552094f8809911.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"4","league_id":"634","match_id":"66863","status":"1","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"LGD","team_a_logo":"https://qn.feijing88.com/egame/lol/team/fc6b5787b145e075b751c29f1497904e.png","team_b_name":"PSG","team_b_logo":"https://qn.feijing88.com/egame/lol/team/bc0e8dc69590e5126feb9023363efe0f.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"5","league_id":"634","match_id":"66864","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第一日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"TL","team_a_logo":"https://qn.feijing88.com/egame/lol/team/59251a2c32c4a6f0592aaa25fe92c8a0.png","team_b_name":"MAD","team_b_logo":"https://qn.feijing88.com/egame/lol/team/6be30c2bf2faef88f0c2f3c44af095d2.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"6","league_id":"634","match_id":"66865","status":"2","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"R7","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/0c1557b4f5eb4d1c86c55ce49117afb5.png","team_b_name":"V3","team_b_logo":"https://qn.feijing88.com/egame/lol/team/b07da61aeb6ed14990378d5265540a26.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0},{"id":"7","league_id":"634","match_id":"66870","status":"1","viewnum":"0","team_a_score":"0","team_b_score":"0","round_son_name":"第二日","startdate":"09-21","starttime":"22:09","league_name":"S10 世界总决赛","team_a_name":"ITZ","team_a_logo":"https://qn.feijing88.com/feijing-home/egame/image/20190701/f92bd683b3394c22b6552094f8809911.png","team_b_name":"SUP","team_b_logo":"https://qn.feijing88.com/feijing-home/egame/image/2020917/824a28139cd34f9ca5e7b4da0fe327d9.png","team_a_odds":0,"team_b_odds":0,"team_a_kill_count":0,"team_b_kill_count":0}]}
         */

        private int code;
        private String msg;
        private Object info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getInfo() {
            return info;
        }

        public void setInfo(Object info) {
            this.info = info;
        }

    }
}
