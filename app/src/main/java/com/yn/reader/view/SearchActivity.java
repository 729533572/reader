package com.yn.reader.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.db.SearchHistoryBeanDao;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.SearchPresenter;
import com.yn.reader.mvp.views.SearchView;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.dao.SearchHistoryBean;
import com.yn.reader.model.hotSearch.HotSearch;
import com.yn.reader.widget.tag.OnTagClickListener;
import com.yn.reader.widget.tag.OnTagDeleteListener;
import com.yn.reader.widget.tag.Tag;
import com.yn.reader.widget.tag.TagView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseMvpActivity implements SearchView {
    private SearchPresenter mSearchPresenter;

    @BindView(R.id.ll_search_history)
    LinearLayout ll_search_history;

    @BindView(R.id.tag_search_history)
    TagView mSearchHistoryTagGroup;

    @BindView(R.id.tag_hot_search)
    TagView mHotSearchTagGroup;

    @BindView(R.id.searchEngine)
    EditText searchEngine;

    private List<HotSearch> mHotSearches;
    private Map<Tag, SearchHistoryBean> mTagSearchHistoryBeanMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setStatusBarColor2White();
        ButterKnife.bind(this);
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSearchHistoryTagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(int position, Tag tag) {
                mSearchPresenter.searchByHistory(mTagSearchHistoryBeanMap.get(tag));
            }
        });
        mSearchHistoryTagGroup.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(int position, Tag tag) {
                SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getInstance().getDaoSession().getSearchHistoryBeanDao();
                List<SearchHistoryBean> searchHistoryBeans = searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Name.eq(tag.text)).build().list();

                for (SearchHistoryBean bean : searchHistoryBeans) {
                    searchHistoryBeanDao.delete(bean);
                }
                mTagSearchHistoryBeanMap.remove(tag);
                if (mTagSearchHistoryBeanMap.size() > 0)
                    ll_search_history.setVisibility(View.VISIBLE);
                else ll_search_history.setVisibility(View.GONE);
            }
        });
        mHotSearchTagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(int position, Tag tag) {
                HotSearch hotSearch = getHotSearchKeyWordByTag(tag);
                mSearchPresenter.searchByHotSearch(hotSearch);
            }
        });
        searchEngine.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String keyword = searchEngine.getText().toString();
                    if (StringUtil.isEmpty(keyword)) {
                        ToastUtil.showShort(SearchActivity.this, getString(R.string.tip_search_blank));
                        return false;
                    }
                    mSearchPresenter.searchByKeyWord(keyword);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocalData();
    }

    @OnClick(R.id.tv_delete_search_record)
    public void deleteSearchRecord() {
        new MaterialDialog.Builder(getContext())
                .content(R.string.tip_delete_all)
                .canceledOnTouchOutside(false)
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getInstance().getDaoSession().getSearchHistoryBeanDao();
                        searchHistoryBeanDao.deleteAll();
                        loadLocalData();
                    }
                }).show();
    }

    private void loadData() {
        mSearchPresenter = new SearchPresenter(this);
        mSearchPresenter.getHotSearchkeyWords();
    }

    private void loadLocalData() {
        SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getInstance().getDaoSession().getSearchHistoryBeanDao();
        List<SearchHistoryBean> history = searchHistoryBeanDao.loadAll();

//        String[] history = new String[]{"我的妹妹", "追龙", "火影忍者","我的妹妹", "追龙", "火影忍者","我的妹妹", "追龙", "火影忍者"};
        mSearchHistoryTagGroup.removeAllTags();
        mTagSearchHistoryBeanMap = new HashMap<>();
        for (int i = history.size() - 1; i >= 0; i--) {
            Tag tag = new Tag(history.get(i).getName());
            mTagSearchHistoryBeanMap.put(tag, history.get(i));

            tag.tagTextColor = Color.parseColor("#FFFFFF");
            tag.layoutColor = Color.parseColor("#f8b551");
            tag.layoutColorPress = Color.parseColor("#555555");
            tag.radius = 24f;
            tag.tagTextSize = 14f;
            tag.layoutBorderSize = 1f;
            tag.layoutBorderColor = Color.parseColor("#FFFFFF");
            tag.isDeletable = true;
            mSearchHistoryTagGroup.addTag(tag);
        }
        if (mTagSearchHistoryBeanMap.size() > 0)
            ll_search_history.setVisibility(View.VISIBLE);
        else ll_search_history.setVisibility(View.GONE);
    }

    @OnClick(R.id.cancel_search)
    public void cancel() {
        finish();
    }

    @Override
    public BasePresenter getPresenter() {
        return mSearchPresenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onHotSearchKeyWordsLoaded(List<HotSearch> data) {
        if (data == null || data.size() == 0) return;
        mHotSearches = data;
        for (int i = 0; i < data.size(); i++) {

            Tag tag = new Tag(data.get(i).getName());
            tag.radius = 24f;
            tag.tagTextSize = 14f;

            if (i < 3) tag.tagTextColor = Color.parseColor("#FFFFFF");
            else tag.tagTextColor = Color.parseColor("#1e1e1e");
            switch (i) {
                case 0:
                    tag.layoutColor = Color.parseColor("#ff5b5b");
                    break;
                case 1:
                    tag.layoutColor = Color.parseColor("#00a0e9");
                    break;
                case 2:
                    tag.layoutColor = Color.parseColor("#f8b551");
                    break;
                default:
                    tag.layoutColor = Color.parseColor("#bababa");
                    break;
            }
            tag.layoutColorPress = Color.parseColor("#555555");
            mHotSearchTagGroup.addTag(tag);
        }
    }

    @Override
    public void cleanInputKeyword() {
        searchEngine.setText("");
    }

    private HotSearch getHotSearchKeyWordByTag(Tag tag) {
        HotSearch hotSearch = null;
        for (HotSearch bean : mHotSearches) {
            if (bean.getName().equals(tag.text)) {
                hotSearch = bean;
                break;
            }
        }
        return hotSearch;
    }
}
