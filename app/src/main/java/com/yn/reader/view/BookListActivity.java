package com.yn.reader.view;

import android.content.Intent;
import android.os.Bundle;

import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.util.Constant;
import com.yn.reader.view.fragment.BookListFragment;

import butterknife.ButterKnife;

/**
 * 书籍列表
 */
public class BookListActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        setStatusBarColor2Green();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        getFragmentManager().beginTransaction()
                .add(
                        R.id.fragment_container,
                        BookListFragment.getInstance(
                                intent.getIntExtra(Constant.KEY_FRAGMENT_TYPE, 0),
                                intent.getIntExtra(Constant.KEY_CATEGORY_ID, 0)
                                , intent.getIntExtra(Constant.KEY_CATEGORY_SEX, 0))
                )
                .commitAllowingStateLoss();
        setToolbarTitle(intent.getStringExtra(Constant.KEY_FRAGMENT_TITLE));
    }
}
