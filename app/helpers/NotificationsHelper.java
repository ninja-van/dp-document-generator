package helpers;

import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

@Singleton
public class NotificationsHelper {

    private static Messages messages;

    @Inject
    public NotificationsHelper(MessagesApi messagesApi) {
        Collection<Lang> candidates = Collections.singletonList(new Lang(Locale.US));
        NotificationsHelper.messages = messagesApi.preferred(candidates);
    }

    public static String get(String key, Object... args) {
        return messages.at(key, args);
    }
}
