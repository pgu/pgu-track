package pgu.track.client;

import java.util.ArrayList;

import pgu.track.shared.Editorial;
import pgu.track.shared.Family;
import pgu.track.shared.MySubscription;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    ArrayList<Family> fetchFamilies();

    void createFamily(String user, Family newFamily);

    ArrayList<Editorial> fetchEditorials();

    void createEditorial(String user, Editorial newEditorial);

    ArrayList<MySubscription> fetchSubscriptions();

    void createSubscription(MySubscription newSubscription);

    String askChannelToken();

    void unsubscribe();
}
