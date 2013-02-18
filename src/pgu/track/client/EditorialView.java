package pgu.track.client;

import java.util.ArrayList;

import pgu.track.shared.Editorial;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DataGrid;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class EditorialView extends Composite {

    private static EditorialViewUiBinder uiBinder = GWT.create(EditorialViewUiBinder.class);

    interface EditorialViewUiBinder extends UiBinder<Widget, EditorialView> {
    }

    @UiField
    Alert userBox;
    @UiField
    TextBox titleBox;
    @UiField
    Button createBtn;
    @UiField
    SimplePanel containerEditorials;

    DataGrid<Editorial> editorialsPanel;

    private final Pgu_track presenter;

    public EditorialView(final Pgu_track pgu_track) {
        initWidget(uiBinder.createAndBindUi(this));
        presenter = pgu_track;
    }

    @UiHandler("createBtn")
    public void createEditorial(final ClickEvent e) {
        final String title = titleBox.getText();
        presenter.createEditorial(title);
    }

    public void setUser(final String user) {
        this.user = user;
        userBox.setText("Hello " + user);
    }

    public void setEditorials(final ArrayList<Editorial> editorials) {
        final TextColumn<Editorial> idColumn = new TextColumn<Editorial>() {
            @Override
            public String getValue(final Editorial object) {
                return "" + object.id;
            }
        };
        final TextColumn<Editorial> titleColumn = new TextColumn<Editorial>() {
            @Override
            public String getValue(final Editorial object) {
                return object.title;
            }
        };

        editorialsPanel = new DataGrid<Editorial>();
        editorialsPanel.addColumn(idColumn, "Id");
        editorialsPanel.addColumn(titleColumn, "Title");

        editorialsPanel.setRowCount(editorials.size(), true);
        editorialsPanel.setRowData(0, editorials);

        editorialsPanel.setWidth("100%");
        editorialsPanel.setHeight("200px");

        containerEditorials.setWidget(editorialsPanel);
    }

    private String user;

    public String getUser() {
        return user;
    }

}
