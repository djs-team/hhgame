package org.cocos2dx.javascript.di.module;

import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.di.mapkey.ViewModelKey;
import com.deepsea.mua.kit.di.module.ViewModelModuleKit;
import com.deepsea.mua.voice.viewmodel.FullServiceSortModel;

import org.cocos2dx.javascript.ui.main.viewmodel.MainViewModel;
import org.cocos2dx.javascript.ui.splash.viewmodel.SplashViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by JUN on 2019/3/26
 */
@Module(includes = ViewModelModuleKit.class)
public abstract class ViewModelModule {



    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindsMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel bindsSplashViewModel(SplashViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FullServiceSortModel.class)
    abstract ViewModel bindFullServiceSortModel(FullServiceSortModel viewModel);
}
