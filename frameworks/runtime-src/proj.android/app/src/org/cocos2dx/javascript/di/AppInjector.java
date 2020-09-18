package org.cocos2dx.javascript.di;

import com.deepsea.mua.core.di.ActivityFragmentInjector;

import org.cocos2dx.javascript.app.App;
import org.cocos2dx.javascript.di.component.DaggerAppComponent;

/**
 * Created by JUN on 2019/3/22
 */
public class AppInjector {

    private AppInjector() {
    }

    public static void init(App app) {
        DaggerAppComponent
                .builder()
                .application(app)
                .build()
                .inject(app);
        ActivityFragmentInjector.init(app);
    }
}
