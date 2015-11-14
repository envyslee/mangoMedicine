package com.nicholas.fastmedicine;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by eggri_000 on 2015/10/16.
 */
public class SearchActivity extends AppCompatActivity {




    private TextView keyword;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
/*     MenuItem   delItem=  menu.findItem(R.id.delete_btn);
        delItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);*/
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        keyword=(TextView)findViewById(R.id.keyword);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.delete_btn:
                        keyword.setText("");
                        break;*/
                    case R.id.search_btn:
                        Toast.makeText(SearchActivity.this, keyword.getText(), Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });


    }
}
