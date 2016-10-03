package com.recipe.roulette.app.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.recipe.roulette.app.R;
import com.recipe.roulette.app.constants.Constants;
import com.recipe.roulette.app.injection.component.AppComponent;
import com.recipe.roulette.app.injection.component.DaggerCustomViewComponent;
import com.recipe.roulette.app.injection.module.CustomViewModule;
import com.recipe.roulette.app.presenter.CustomPresenter;
import com.recipe.roulette.app.presenter.loader.PresenterFactory;
import com.recipe.roulette.app.util.LogUtil;
import com.recipe.roulette.app.util.UIUtil;
import com.recipe.roulette.app.view.CustomView;
import com.recipe.roulette.app.view.impl.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class MainFragment extends BaseFragment<CustomPresenter, CustomView> implements CustomView {

    @Inject
    PresenterFactory<CustomPresenter> mPresenterFactory;
    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    RequestManager mGlide;

    @BindView(R.id.keyword_search_view)
    SearchView mSearchView;
    @BindView(R.id.layout_switch)
    SwitchCompat mSwitch;
    @BindView(R.id.loading_image_view)
    ImageView mLoadingImageView;
    @BindView(R.id.search_button1)
    Button mSearchButton;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //hide keyboard outside SearchView
        UIUtil.hideSoftKeyOutsideET(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        setSwitch();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerCustomViewComponent.builder()
                .appComponent(parentComponent)
                .customViewModule(new CustomViewModule())
                .build()
                .inject(this);

    }

    @NonNull
    @Override
    protected PresenterFactory<CustomPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @OnClick(R.id.search_button1)
    public void onClick() {
        if (mPresenter != null
                && mSearchView != null
                && !TextUtils.isEmpty(mSearchView.getQuery())) {
            LogUtil.d(Constants.API_TAG, "MainFragment | search query = " + mSearchView.getQuery().toString());
            mPresenter.search(mSearchView.getQuery().toString());
        }

    }


    @Override
    public void setSwitch() {
        if (mSharedPreferences != null && mSwitch != null) {
            boolean isChecked = mSharedPreferences.getBoolean(Constants.LAYOUT_KEY, false);

            if (isChecked)
                mSwitch.setText(R.string.title_list);
            else
                mSwitch.setText(R.string.title_swipe);
            mSwitch.setChecked(isChecked);


            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mPresenter != null) {
                        LogUtil.d(Constants.UI_TAG, "MainFragment | mPresenter not NULL, calling setRecipeView()");
                        mPresenter.setRecipeView(buttonView, isChecked);
                    }
                }
            });
        }
    }
}
