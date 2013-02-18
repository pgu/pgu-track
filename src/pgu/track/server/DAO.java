package pgu.track.server;

import pgu.track.shared.Editorial;
import pgu.track.shared.Family;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class DAO extends DAOBase {

    static {
        ObjectifyService.register(Family.class);
        ObjectifyService.register(Editorial.class);
    }


}
