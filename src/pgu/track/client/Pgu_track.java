package pgu.track.client;

import java.util.ArrayList;

import pgu.track.shared.Editorial;
import pgu.track.shared.Family;
import pgu.track.shared.MySubscription;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pgu_track implements EntryPoint {

    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    final SimplePanel                  page            = new SimplePanel();
    SigninView                         signinView;
    FamilyView                         familyView;
    EditorialView                      editorialView;
    AdminView                          adminView;

    private native void exportJS() /*-{
        $wnd.pgu_track_channel_on_message = $entry(@pgu.track.client.Pgu_track::onMessageFromChannel(Ljava/lang/String;Ljava/lang/String;));
    }-*/;

    @Override
    public void onModuleLoad() {
        static_self = this;
        exportJS();

        signinView = new SigninView(this);
        familyView = new FamilyView(this);
        editorialView = new EditorialView(this);
        adminView = new AdminView(this);

        RootPanel.get().add(page);
        page.setWidget(signinView);
    }

    private native void console(String msg) /*-{
		$wnd.console.log(msg);
    }-*/;

    public void onClickFamily(final String user) {
        if ("".equals(user.trim())) {
            Window.alert("Give me a name");
            return;
        }

        familyView.setUser(user);

        page.setWidget(familyView);
        fetchFamilies();
    }

    private void fetchFamilies() {
        greetingService.fetchFamilies(new AsyncCallback<ArrayList<Family>>() {

            @Override
            public void onFailure(final Throwable caught) {
                console("error");
            }

            @Override
            public void onSuccess(final ArrayList<Family> families) {
                console("families nb: " + families.size());
                familyView.setFamilies(families);
            }

        });
    }

    public void onClickEditorial(final String user) {
        if ("".equals(user.trim())) {
            Window.alert("Give me a name");
            return;
        }

        editorialView.setUser(user);

        page.setWidget(editorialView);
        fetchEditorials();
    }

    private void fetchEditorials() {
        greetingService.fetchEditorials(new AsyncCallback<ArrayList<Editorial>>() {

            @Override
            public void onFailure(final Throwable caught) {
                console("error");
            }

            @Override
            public void onSuccess(final ArrayList<Editorial> editorials) {
                console("editorials nb: " + editorials.size());
                editorialView.setEditorials(editorials);
            }

        });
    }

    public void onClickAdmin(final String pass) {
        if (!"comment".equals(pass)) {
            return;
        }

        page.setWidget(adminView);
        fetchSubscriptions();
        askChannelToken();
    }

    private void askChannelToken() {
        greetingService.askChannelToken(new AsyncCallback<String>() {

            @Override
            public void onFailure(final Throwable caught) {
                Window.alert("failure");
            }

            @Override
            public void onSuccess(final String token) {
                openChannel(token);
            }
        });
    }

    private native void openChannel(String token) /*-{
		$wnd.pgu_track_open_channel(token);
    }-*/;

    private void fetchSubscriptions() {
        greetingService.fetchSubscriptions(new AsyncCallback<ArrayList<MySubscription>>() {

            @Override
            public void onFailure(final Throwable caught) {
                console("error");
            }

            @Override
            public void onSuccess(final ArrayList<MySubscription> subscriptions) {
                console("subscriptions nb: " + subscriptions.size());
                adminView.setSubscriptions(subscriptions);
            }

        });
    }

    public void createFamily(final String name, final String description) {
        if ("".equals(name.trim())) {
            return;
        }

        final Family newFamily = new Family();
        newFamily.name = name;
        newFamily.description = description;

        greetingService.createFamily(familyView.getUser(), newFamily, new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable caught) {
                console("error");
            }

            @Override
            public void onSuccess(final Void result) {
                fetchFamilies();
            }
        });
    }

    public void createEditorial(final String title) {
        if ("".equals(title.trim())) {
            return;
        }

        final Editorial newEditorial = new Editorial();
        newEditorial.title = title;

        greetingService.createEditorial(editorialView.getUser(), newEditorial, new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable caught) {
                console("error");
            }

            @Override
            public void onSuccess(final Void result) {
                fetchEditorials();
            }
        });
    }

    public void createSubscription(final String topic, final String subscription, final String query) {
        if ("".equals(topic.trim()) //
                || "".equals(subscription.trim()) //
                || "".equals(query.trim()) //
                ) {
            return;
        }

        final MySubscription newSubscription = new MySubscription();
        newSubscription.topic = topic;
        newSubscription.id = subscription;
        newSubscription.query = query;

        greetingService.createSubscription(newSubscription, new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable caught) {
                console("error");
            }

            @Override
            public void onSuccess(final Void result) {
                fetchSubscriptions();
            }
        });
    }

    private static Pgu_track static_self;

    public static void onMessageFromChannel(final String type, final String body) {
        static_self.adminView.addMessageFromChannel(type, body);
    }

    public void unsubscribe() {
        greetingService.unsubscribe(new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable caught) {
                console("error");
            }

            @Override
            public void onSuccess(final Void result) {
                fetchSubscriptions();
            }
        });
    }

}
