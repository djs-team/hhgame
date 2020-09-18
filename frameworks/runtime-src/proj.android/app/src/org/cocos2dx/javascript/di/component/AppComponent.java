package org.cocos2dx.javascript.di.component;

import com.deepsea.mua.kit.di.module.AppModule;

import org.cocos2dx.javascript.app.App;
import org.cocos2dx.javascript.di.module.ActivitysModule;
import org.cocos2dx.javascript.di.module.FragmentsModule;
import org.cocos2dx.javascript.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by JUN on 2019/3/22
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivitysModule.class,
        FragmentsModule.class,
        ViewModelModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);

        AppComponent build();
    }

    void inject(App app);
}
