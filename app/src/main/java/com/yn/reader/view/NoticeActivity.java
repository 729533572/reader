package com.yn.reader.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.util.EditListener;
import com.yn.reader.view.adapter.NoticePagerAdapter;
import com.yn.reader.view.fragment.notice.NoticeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息中心
 */
public class NoticeActivity extends BaseActivity {
    @BindView(R.id.notice_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.toolbar_back)
    ImageView toolbar_back;

    @BindView(R.id.select_all)
    TextView select_all;
    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.fl_operate_layout)
    FrameLayout fl_operate_layout;

    @BindView(R.id.tv_bottom_left)
    TextView mark_to_read;

    @BindView(R.id.delete)
    TextView delete;

    @BindView(R.id.bottom_divider)
    View bottom_divider;

    private boolean isEditModel;

    private NoticePagerAdapter mNoticePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        mNoticePagerAdapter = new NoticePagerAdapter(getFragmentManager(), this);
        mViewPager.setAdapter(mNoticePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isEditModel) {
                    onSaveClicked();
                }
                for (int i = 0; i < mNoticePagerAdapter.getBaseFragments().length; i++) {
                    NoticeFragment fragment = (NoticeFragment) mNoticePagerAdapter.getBaseFragments()[i];
                    fragment.onCompleteClicked();
                }
                NoticeFragment noticeFragment = (NoticeFragment) mNoticePagerAdapter.getBaseFragments()[mViewPager.getCurrentItem()];
                if (noticeFragment.getNoticeList().size() > 0) save.setVisibility(View.VISIBLE);
                else save.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editDone();
            }
        }, 50);
    }

    @OnClick(R.id.save)
    public void onSaveClicked() {

        isEditModel = !isEditModel;
        if (isEditModel) {
            toolbar_back.setVisibility(View.GONE);
            select_all.setVisibility(View.VISIBLE);
            save.setText(R.string.complete);
            getCurrentEditListener().onEditClicked();
            fl_operate_layout.setVisibility(View.VISIBLE);
        } else {
            toolbar_back.setVisibility(View.VISIBLE);
            select_all.setVisibility(View.GONE);
            save.setText(R.string.edit);
            getCurrentEditListener().onCompleteClicked();
            editDone();
        }
    }


    @OnClick(R.id.select_all)
    public void selectAll() {
        if (isEditModel) {
            select_all.setText(select_all.getText().toString().equals(R.string.cancel) ? R.string.select_all : R.string.cancel);
            getCurrentEditListener().onSelectAllChecked();
        }
    }

    @OnClick(R.id.toolbar_back)
    public void back() {
        if (!isEditModel) finish();
    }

    @OnClick(R.id.delete)
    public void deleteSelect() {
        getCurrentEditListener().deleteSelect();
    }

    @OnClick(R.id.tv_bottom_left)
    public void mark_to_read() {
        getCurrentEditListener().mark2Read();
    }

    private EditListener getCurrentEditListener() {
        NoticeFragment fragment = (NoticeFragment) mNoticePagerAdapter.getBaseFragments()[mViewPager.getCurrentItem()];
        return fragment;
    }

    public void editDone() {
        NoticeFragment noticeFragment = (NoticeFragment) mNoticePagerAdapter.getBaseFragments()[mViewPager.getCurrentItem()];
        if (noticeFragment.getNoticeList().size() > 0) save.setVisibility(View.VISIBLE);
        else save.setVisibility(View.GONE);
        fl_operate_layout.setVisibility(View.GONE);
    }
}
