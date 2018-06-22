package com.yn.reader.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.TimeUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.model.notice.Notice;
import com.yn.reader.util.Constant;
import com.yn.reader.view.adapter.NoticePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息详情
 */
public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.message_title)
    TextView title;
    @BindView(R.id.message_content)
    TextView message_content;
    @BindView(R.id.message_time)
    TextView message_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        Notice notice = intent.getParcelableExtra(Constant.KEY_MESSAGE_CONTENT);
        int type =  intent.getIntExtra(Constant.KEY_TYPE, NoticePagerAdapter.PAGER_NOTICE_SYSTEM);
        String topic = type==NoticePagerAdapter.PAGER_NOTICE_SYSTEM?"系统消息":"用户消息";
        setToolbarTitle(topic);
        message_content.setText(StringUtil.isEmpty(notice.getContent())?"":notice.getContent());
        message_time.setText(StringUtil.isEmpty(notice.getCreatedate())?"":TimeUtil.getDayDate(notice.getCreatedate()));
        title.setText(StringUtil.isEmpty(notice.getTitle())?topic:notice.getTitle());
    }
}
