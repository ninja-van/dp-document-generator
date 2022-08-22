package guice;

import com.google.inject.AbstractModule;
import helpers.NotificationsHelper;

public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(NotificationsHelper.class).asEagerSingleton();
    }
}
