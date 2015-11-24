package litepalDB;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2015/11/24.
 */
public class UserInfo extends DataSupport {
    @Column(nullable = false)
    public String u_n;

    @Column(nullable = false)
    public String u_p;

    @Column(nullable = false)
    public String u_i;

    public UserInfo(String u_n, String u_p, String u_i) {
        this.u_n = u_n;
        this.u_p = u_p;
        this.u_i = u_i;
    }

    public String getU_n() {
        return u_n;
    }

    public void setU_n(String u_n) {
        this.u_n = u_n;
    }

    public String getU_p() {
        return u_p;
    }

    public void setU_p(String u_p) {
        this.u_p = u_p;
    }

    public String getU_i() {
        return u_i;
    }

    public void setU_i(String u_i) {
        this.u_i = u_i;
    }
}
