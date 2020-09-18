package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityMyTagsBinding;
import com.deepsea.mua.mine.viewmodel.TagsModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.TagBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.view.flowlayout.FlowLayout;
import com.deepsea.mua.stub.view.flowlayout.TagAdapter;
import com.deepsea.mua.stub.view.flowlayout.TagFlowLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

/**
 * Created by JUN on 2019/8/9
 * 注销账户
 */
public class MyTagsActivity extends BaseActivity<ActivityMyTagsBinding> {
    private int timeFlag = 15;
    private TagsModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;
    private String hobby_id = "";
    private String feature_id = "";
    private List<TagBean.TagItem> hobbyIds;
    private List<TagBean.TagItem> features;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_tags;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(TagsModel.class);
        fetchInfo();
    }

    private void fetchInfo() {
        mViewModel.fetchInfo().observe(this, new BaseObserver<TagBean>() {
            @Override
            public void onSuccess(TagBean result) {
                if (result != null) {
                    if (result.getHobbys() != null) {
                        hobbyIds = result.getHobbys();
                        initHobbyFlowLayout(result.getUser_arr().getHobby(), result.getHobbys());
                    }
                    if (result.getFeatures() != null) {
                        features = result.getFeatures();
                        initCharacterFlowLayout(result.getHobbys(), result.getUser_arr().getFeature(), result.getFeatures());
                    }
                    if (result.getUser_arr() != null) {
                        initTypeTitle(true, true, result.getUser_arr());
                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    private void editInfo() {
        showProgress();
        mViewModel.edit(hobby_id, feature_id).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                if (result != null) {
                    if (result.getCode() == 200) {
                        toastShort(result.getDesc());
                        finish();
                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                hideProgress();
            }
        });
    }

    private void initTypeTitle(boolean isHobby, boolean isFeature, TagBean.UserArrBean userArrBean) {
        initTypeTitle(isHobby, isFeature, userArrBean.getHobby(), userArrBean.getFeature());

    }

    private void initTypeTitle(boolean isHobby, boolean isFeature, String hobbyIdStr, String featureIdStr) {
        if (isHobby) {
            if (!TextUtils.isEmpty(hobbyIdStr)) {
                String[] strHobby = hobbyIdStr.split(",");
                String hobbyTitleStr = "";
                for (int i = 0; i < strHobby.length; i++) {
                    int position = Integer.valueOf(strHobby[i]) - 1;
                    hobbyTitleStr += (hobbyIds.get(position).getName() + ",");
                }
                ViewBindUtils.setText(mBinding.tvHobbyTitle, !TextUtils.isEmpty(hobbyTitleStr) ? hobbyTitleStr.substring(0, hobbyTitleStr.length() - 1) : "");
            } else {
                ViewBindUtils.setText(mBinding.tvHobbyTitle, "");
            }
        }
        if (isFeature) {
            if (!TextUtils.isEmpty(featureIdStr)) {
                String[] strFeature = featureIdStr.split(",");
                String featureTitleStr = "";
                for (int i = 0; i < strFeature.length; i++) {
                    featureTitleStr += (features.get(Integer.valueOf(strFeature[i]) - 1).getName() + ",");
                }
                ViewBindUtils.setText(mBinding.tvCharacterTitle, !TextUtils.isEmpty(featureTitleStr) ? featureTitleStr.substring(0, featureTitleStr.length() - 1) : "");
            } else {
                ViewBindUtils.setText(mBinding.tvCharacterTitle, "");
            }
        }
    }

    private void setTypeTitle(boolean isHobby, boolean isFeature) {
        Set<Integer> integersHobby = mBinding.hobbyFlowlayout.getSelectedList();
        Iterator<Integer> it = integersHobby.iterator();
        String hobby_id = "";
        while (it.hasNext()) {
            hobby_id += (hobbyIds.get(it.next()).getId() + ",");
        }
        Set<Integer> integersCharacter = mBinding.characterFlowlayout.getSelectedList();
        Iterator<Integer> it_character = integersCharacter.iterator();
        String feature_id = "";
        while (it_character.hasNext()) {
            feature_id += (features.get(it_character.next()).getId() + ",");
        }
        if (!TextUtils.isEmpty(hobby_id)) {
            hobby_id = hobby_id.substring(0, hobby_id.length() - 1);
        }
        if (!TextUtils.isEmpty(feature_id)) {
            feature_id = feature_id.substring(0, feature_id.length() - 1);
        }
        initTypeTitle(isHobby, isFeature, hobby_id, feature_id);
    }

    private TagAdapter hobbyAdapter;

    private void initHobbyFlowLayout(String selectHobby, List<TagBean.TagItem> hobbyData) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        hobbyAdapter = new TagAdapter(hobbyData) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_item,
                        mBinding.hobbyFlowlayout, false);
                TagBean.TagItem item = (TagBean.TagItem) o;
                tv.setText(item.getName());
                return tv;
            }
        };
        Set<Integer> selectPos = new HashSet<>();
        if (!TextUtils.isEmpty(selectHobby)) {
            String[] str = selectHobby.split(",");
            for (int i = 0; i < hobbyIds.size(); i++) {
                boolean flag = Arrays.asList(str).contains(hobbyIds.get(i).getId());
                if (flag) {
                    selectPos.add(i);
                }
            }
        }
        hobbyAdapter.setSelectedList(selectPos);
        mBinding.hobbyFlowlayout.setMaxSelectCount(3);
        mBinding.hobbyFlowlayout.setAdapter(hobbyAdapter);
        mBinding.hobbyFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mBinding.hobbyFlowlayout.getSelectedList().size() >= 3) {
                    boolean currentIsSelect = mBinding.hobbyFlowlayout.getSelectedList().contains(position);
                    if (!currentIsSelect) {
                        toastShort("最多只能选择3个兴趣爱好");
                    } else {
                        setTypeTitle(true, false);
                    }
                } else {
                    setTypeTitle(true, false);
                }
                return false;
            }
        });
    }

    private TagAdapter featureAdapter;

    private void initCharacterFlowLayout(List<TagBean.TagItem> hobbyData, String selectFeature, List<TagBean.TagItem> featuresData) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        mBinding.characterFlowlayout.setMaxSelectCount(3);
        featureAdapter = new TagAdapter(featuresData) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_item,
                        mBinding.characterFlowlayout, false);
                TagBean.TagItem item = (TagBean.TagItem) o;
                tv.setText(item.getName());
                return tv;
            }
        };
        mBinding.characterFlowlayout.setAdapter(featureAdapter);
        Set<Integer> selectPos = new HashSet<>();
        if (!TextUtils.isEmpty(selectFeature)) {
            String[] str = selectFeature.split(",");
            for (int i = 0; i < features.size(); i++) {
                boolean flag = Arrays.asList(str).contains(features.get(i).getId());
                if (flag) {
                    selectPos.add(i);
                }
            }
        }
        featureAdapter.setSelectedList(selectPos);

        mBinding.characterFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mBinding.characterFlowlayout.getSelectedList().size() >= 3) {
                    boolean currentIsSelect = mBinding.characterFlowlayout.getSelectedList().contains(position);
                    if (!currentIsSelect) {
                        toastShort("最多只能选择3个个性特征");
                    } else {
                        setTypeTitle(false, true);
                    }
                } else {
                    setTypeTitle(false, true);
                }
                return false;
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.saveTv, o -> {
            Set<Integer> integersHobby = mBinding.hobbyFlowlayout.getSelectedList();
            Iterator<Integer> it = integersHobby.iterator();
            while (it.hasNext()) {
                hobby_id += (hobbyIds.get(it.next()).getId() + ",");
            }
            Set<Integer> integersCharacter = mBinding.characterFlowlayout.getSelectedList();
            Iterator<Integer> it_character = integersCharacter.iterator();
            while (it_character.hasNext()) {
                feature_id += (features.get(it_character.next()).getId() + ",");
            }
            if (!TextUtils.isEmpty(hobby_id)) {
                hobby_id = hobby_id.substring(0, hobby_id.length() - 1);
            }
            if (!TextUtils.isEmpty(feature_id)) {
                feature_id = feature_id.substring(0, feature_id.length() - 1);
            }
            editInfo();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
