package com.nicholas.fastmedicine;


import android.support.v4.app.Fragment;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class BaseFragment extends Fragment {
    private String title;
    private int iconId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

}
