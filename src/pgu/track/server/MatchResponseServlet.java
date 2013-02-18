package pgu.track.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;

@SuppressWarnings("serial")
public class MatchResponseServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    ChannelService channelService = ChannelServiceFactory.getChannelService();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        log.info("### matching response");

        final int resultsOffset = Integer.parseInt(req.getParameter("results_offset"));
        final int resultsCount = Integer.parseInt(req.getParameter("results_count"));

        final String [] reqSubIDs = req.getParameterValues("id");

        log.info("### offset: " + resultsOffset);
        log.info("### count: " + resultsCount);
        log.info("### subIds: " + Arrays.toString(reqSubIDs));

        // Optional inclusion of matched entity if requested in original match(...) request:
        Entity matchedEntity = null;
        if (req.getParameter("document") != null) {
            matchedEntity =
                    ProspectiveSearchServiceFactory.getProspectiveSearchService().getDocument(req);

            log.info("### matched entity");
            log.info(matchedEntity.toString());
            final String kind = matchedEntity.getKey().getKind();
            log.info("> kind > " + kind); // Comment
            log.info("> k#id > " + matchedEntity.getKey().getId()); // id numerique

            final StringBuilder info = new StringBuilder();

            if ("family".equals(kind)) {
                info.append("family:" + matchedEntity.getProperty("id"));
                info.append(", ");
                info.append("name:" + matchedEntity.getProperty("name"));
                info.append(", ");
                info.append("description:" + matchedEntity.getProperty("description"));
                info.append(", ");
                info.append("from the user: " + matchedEntity.getProperty("user"));

            } else if ("editorial".equals(kind)) {
                info.append("editorial:" + matchedEntity.getProperty("id"));
                info.append(", ");
                info.append("title:" + matchedEntity.getProperty("title"));
                info.append(", ");
                info.append("from the user: " + matchedEntity.getProperty("user"));

            } else {
                throw new IllegalArgumentException("Unknown kind: " + kind);
            }

            info.append(", ");
            info.append("for the subscriptions: " + Arrays.toString(reqSubIDs));

            final String channelMessage = //
                    "{\"type\":\""+ kind +"\",\"body\":\"" + //
                    info.toString() + //
                    "\"}";

            final String channelClientId = "1";
            channelService.sendMessage(new ChannelMessage(channelClientId, channelMessage));

        } else {
            log.info("No document");
        }
    }

}
