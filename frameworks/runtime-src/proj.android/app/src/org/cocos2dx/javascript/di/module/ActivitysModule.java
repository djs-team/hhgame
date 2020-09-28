package org.cocos2dx.javascript.di.module;

import com.deepsea.mua.kit.di.module.ActivitysModuleKit;
import com.deepsea.mua.kit.di.scope.ActivityScope;

import org.cocos2dx.javascript.ui.main.MainActivity;
import org.cocos2dx.javascript.ui.splash.activity.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by JUN on 2019/3/24
 */
@Module(includes = ActivitysModuleKit.class)
public abstract class ActivitysModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();
    @ActivityScope
    @ContributesAndroidInjector
    abstract SplashActivity contributesSplashActivity();

}
