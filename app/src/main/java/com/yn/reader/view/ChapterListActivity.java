package com.yn.reader.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.util.BitIntentDataManager;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.util.UserInfoManager;
import com.yn.reader.widget.ChapterListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 章节列表
 */
public class ChapterListActivity extends BaseActivity {
    @BindView(R.id.chapterlist)
    ChapterListView chapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);
        ButterKnife.bind(this);
        setStatusBarColor2Green();
        chapterListView.setFullScreen();
        handleIntent();
    }

    private void handleIntent() {
        final Intent intent = getIntent();
        String key = intent.getStringExtra("data_key");
        final Book book = (Book) BitIntentDataManager.getInstance().getData(key);

        chapterListView.setBackgroundResource(R.color.read_bottom_bar_bg_color);
        chapterListView.setData(book, new ChapterListView.OnItemClickListener() {
            @Override
            public void itemClick(Object item, int index) {
                if (item instanceof ChapterListBean) {
                    ChapterListBean bean = (ChapterListBean) item;

                    if (!UserInfoManager.getInstance().isLanded() && bean.getChaptershoptype() != 1) {
                        IntentUtils.startActivity(ChapterListActivity.this, LoginTipActivity.class);
                        return;
                    }
                    book.setChapterid(bean.getChapterid());
                    BookDBManager.getInstance().addToHistory(book);
                    IntentUtils.startActivity(ChapterListActivity.this, ReaderActivity.class, book.getBookid());
                    finish();
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chapterListView.show(false);
    }
}
