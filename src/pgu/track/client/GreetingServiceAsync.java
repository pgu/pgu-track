package pgu.track.client;

import java.util.ArrayList;

import pgu.track.shared.Editorial;
import pgu.track.shared.Family;
import pgu.track.shared.MySubscription;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

    void fetchFamilies(AsyncCallback<ArrayList<Family>> asyncCallback);

    void createFamily(String user, Family newFamily, AsyncCallback<Void> asyncCallback);

    void fetchEditorials(AsyncCallback<ArrayList<Editorial>> asyncCallback);

    void createEditorial(String user, Editorial newEditorial, AsyncCallback<Void> asyncCallback);

    void fetchSubscriptions(AsyncCallback<ArrayList<MySubscription>> asyncCallback);

    void createSubscription(MySubscription newSubscription, AsyncCallback<Void> asyncCallback);

    void askChannelToken(AsyncCallback<String> asyncCallback);

    void unsubscribe(AsyncCallback<Void> asyncCallback);
}
