package com.yn.reader.view.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.util.Constant;
import com.yn.reader.view.fragment.BaseFragment;

import butterknife.ButterKnife;

/**
 * 女生书籍
 * Created by sunxy on 2018/2/7.
 */

public class GirlsFragment extends BaseHomeFragment {

    @Override
    protected int getHomeType() {
        return Constant.HOME_TYPE_GIRL;
    }
}
