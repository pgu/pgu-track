package pgu.track.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MySubscription implements IsSerializable {

    public String id;
    public String topic;
    public String query;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MySubscription other = (MySubscription) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MySubscription [id=" + id + ", topic=" + topic + ", query=" + query + "]";
    }

}
