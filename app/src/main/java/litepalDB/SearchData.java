package litepalDB;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2015/11/24.
 */
public class SearchData extends DataSupport {
    @Column(nullable = false)
    public String s_k;

    public SearchData(String s_k) {
        this.s_k = s_k;
    }

    public String getS_k() {
        return s_k;
    }

    public void setS_k(String s_k) {
        this.s_k = s_k;
    }
}
