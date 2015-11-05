package cityDB;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by eggri_000 on 2015/10/23.
 */
public class City  extends DataSupport {

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public int belongId;

    @Column(nullable = false)
    public int test1;
}
