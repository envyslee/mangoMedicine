package litepalDB;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by eggri_000 on 2015/11/9.
 */
public class CollectionItem extends DataSupport {

    public CollectionItem(String c_id, String c_title, String c_subtitle, int c_type) {
        this.c_id = c_id;
        this.c_title = c_title;
        this.c_subtitle = c_subtitle;
        this.c_type = c_type;
    }

    @Column(nullable = false)
    public String c_id;

    @Column(nullable = false)
    public String c_title;

    @Column(nullable = false)
    public String c_subtitle;

    @Column(nullable = false)
    public int c_type;//0:文章 1：药品

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getC_subtitle() {
        return c_subtitle;
    }

    public void setC_subtitle(String c_subtitle) {
        this.c_subtitle = c_subtitle;
    }

    public int getC_type() {
        return c_type;
    }

    public void setC_type(int c_type) {
        this.c_type = c_type;
    }
}
