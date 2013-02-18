package pgu.track.client;

import java.util.ArrayList;

import pgu.track.shared.MySubscription;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DataGrid;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminView extends Composite {

    private static AdminViewUiBinder uiBinder = GWT.create(AdminViewUiBinder.class);

    interface AdminViewUiBinder extends UiBinder<Widget, AdminView> {
    }

    private final Pgu_track presenter;

    @UiField
    TextBox topicBox, subscriptionBox, queryBox;
    @UiField
    Button createBtn, unsubscribeBtn;
    @UiField
    SimplePanel containerSubscriptions;
    @UiField
    FlowPanel containerResults;

    private DataGrid<MySubscription> subscriptionsPanel;

    public AdminView(final Pgu_track pgu_track) {
        initWidget(uiBinder.createAndBindUi(this));
        presenter = pgu_track;
    }

    @UiHandler("unsubscribeBtn")
    public void unsubscribe(final ClickEvent e) {
        presenter.unsubscribe();
    }

    @UiHandler("createBtn")
    public void createSubscription(final ClickEvent e) {
        final String topic = topicBox.getText();
        final String subscription = subscriptionBox.getText();
        final String query = queryBox.getText();
        presenter.createSubscription(topic, subscription, query);
    }

    public void setSubscriptions(final ArrayList<MySubscription> subscriptions) {

        final TextColumn<MySubscription> idColumn = new TextColumn<MySubscription>() {
            @Override
            public String getValue(final MySubscription object) {
                return "" + object.id;
            }
        };
        final TextColumn<MySubscription> topicColumn = new TextColumn<MySubscription>() {
            @Override
            public String getValue(final MySubscription object) {
                return object.topic;
            }
        };
        final TextColumn<MySubscription> queryColumn = new TextColumn<MySubscription>() {
            @Override
            public String getValue(final MySubscription object) {
                return object.query;
            }
        };

        subscriptionsPanel = new DataGrid<MySubscription>();
        subscriptionsPanel.addColumn(idColumn, "Subscription");
        subscriptionsPanel.addColumn(topicColumn, "Topic");
        subscriptionsPanel.addColumn(queryColumn, "Query");

        subscriptionsPanel.setRowCount(subscriptions.size(), true);
        subscriptionsPanel.setRowData(0, subscriptions);

        subscriptionsPanel.setWidth("100%");
        subscriptionsPanel.setHeight("200px");

        containerSubscriptions.setWidget(subscriptionsPanel);
    }

    public void addMessageFromChannel(final String type, final String body) {

        final Alert message = new Alert();
        if ("family".equals(type)) {
            message.setType(AlertType.SUCCESS);

        } else if ("editorial".equals(type)) {
            message.setType(AlertType.INFO);

        } else {
            message.setType(AlertType.ERROR);

        }
        message.setText(body);

        if (containerResults.getWidgetCount() == 0 ) {
            containerResults.add(message);
        } else {
            containerResults.insert(message, 0);
        }
    }

}
