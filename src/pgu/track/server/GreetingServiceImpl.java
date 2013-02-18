package pgu.track.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pgu.track.client.GreetingService;
import pgu.track.shared.Editorial;
import pgu.track.shared.Family;
import pgu.track.shared.MySubscription;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.prospectivesearch.FieldType;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchService;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;
import com.google.appengine.api.prospectivesearch.Subscription;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private final DAO                      dao                      = new DAO();

    private final ChannelService           channelService           = ChannelServiceFactory.getChannelService();
    private final ProspectiveSearchService prospectiveSearchService = ProspectiveSearchServiceFactory
            .getProspectiveSearchService();
    private final DatastoreService         datastoreService         = DatastoreServiceFactory.getDatastoreService();

    @Override
    public ArrayList<Family> fetchFamilies() {
        return new ArrayList<Family>(dao.ofy().query(Family.class).list());
    }

    @Override
    public void createFamily(final String user, final Family newFamily) {
        dao.ofy().put(newFamily);

        final Entity familyDoc = new Entity("family");
        familyDoc.setProperty("id", newFamily.id);
        familyDoc.setProperty("user", user);
        familyDoc.setProperty("name", newFamily.name);
        familyDoc.setProperty("description", newFamily.description);

        for (final String topic : prospectiveSearchService.listTopics("", 1000)) {
            prospectiveSearchService.match(familyDoc, topic);
        }

    }

    @Override
    public ArrayList<Editorial> fetchEditorials() {
        return new ArrayList<Editorial>(dao.ofy().query(Editorial.class).list());
    }

    @Override
    public void createEditorial(final String user, final Editorial newEditorial) {
        dao.ofy().put(newEditorial);

        final Entity editorialDoc = new Entity("editorial");
        editorialDoc.setProperty("id", newEditorial.id);
        editorialDoc.setProperty("user", user);
        editorialDoc.setProperty("title", newEditorial.title);

        for (final String topic : prospectiveSearchService.listTopics("", 1000)) {
            prospectiveSearchService.match(editorialDoc, topic);
        }
    }

    @Override
    public ArrayList<MySubscription> fetchSubscriptions() {

        final ArrayList<MySubscription> mys = new ArrayList<MySubscription>();

        final List<String> topics = prospectiveSearchService.listTopics("", 1000);
        for (final String topic : topics) {

            final List<Subscription> subscriptions = prospectiveSearchService.listSubscriptions(topic);
            for (final Subscription subscription : subscriptions) {

                final MySubscription s = new MySubscription();
                s.id = subscription.getId();
                s.query = subscription.getQuery();
                s.topic = topic;
                mys.add(s);
            }
        }

        return mys;
    }

    @Override
    public void createSubscription(final MySubscription newSubscription) {

        final HashMap<String, FieldType> schema = new HashMap<String, FieldType>();
        schema.put("name", FieldType.STRING);
        schema.put("description", FieldType.STRING);
        schema.put("title", FieldType.STRING);

        prospectiveSearchService.subscribe(newSubscription.topic, newSubscription.id, //
                0, newSubscription.query, schema);
    }

    @Override
    public void unsubscribe() {
        final List<String> topics = prospectiveSearchService.listTopics("", 1000);
        if (topics.isEmpty()) {
            return;
        }

        final String topic = topics.get(0);

        final List<Subscription> subscriptions = prospectiveSearchService.listSubscriptions(topic);
        if (subscriptions.isEmpty()) {
            return;
        }

        final Subscription sub = subscriptions.get(0);
        prospectiveSearchService.unsubscribe(topic, sub.getId());

    }

    @Override
    public String askChannelToken() {
        final String clientId = "1";
        final String token = channelService.createChannel(clientId);
        return token;
    }

}
