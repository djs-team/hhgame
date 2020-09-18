package org.cocos2dx.javascript.di.module;

import com.deepsea.mua.kit.di.module.ActivitysModuleKit;
import com.deepsea.mua.kit.di.scope.ActivityScope;

import org.cocos2dx.javascript.ui.login.activity.BindPhoneActivity;
import org.cocos2dx.javascript.ui.login.activity.LoginActivity;
import org.cocos2dx.javascript.ui.login.activity.LoginMainActivity;
import org.cocos2dx.javascript.ui.login.activity.PermissionReqActivity;
import org.cocos2dx.javascript.ui.login.activity.RegisterActivity;
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
    abstract RegisterActivity contributesRegisterActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract LoginActivity contributesLoginActivity();
    @ActivityScope
    @ContributesAndroidInjector
    abstract LoginMainActivity contributesLoginMainActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract SplashActivity contributesSplashActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract BindPhoneActivity contributesBindPhoneActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();
    @ActivityScope
    @ContributesAndroidInjector
    abstract PermissionReqActivity contributesPermissionReqActivity();
}
