package pgu.track.client;

import java.util.ArrayList;

import pgu.track.shared.Family;

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

public class FamilyView extends Composite  {

    private static FamilyViewUiBinder uiBinder = GWT.create(FamilyViewUiBinder.class);

    interface FamilyViewUiBinder extends UiBinder<Widget, FamilyView> {
    }

    private final Pgu_track presenter;

    @UiField
    Alert userBox;
    @UiField
    TextBox nameBox, descriptionBox;
    @UiField
    Button createBtn;
    @UiField
    SimplePanel containerFamilies;

    DataGrid<Family> familiesPanel;

    public FamilyView(final Pgu_track pgu_track) {
        initWidget(uiBinder.createAndBindUi(this));
        presenter = pgu_track;
    }

    @UiHandler("createBtn")
    public void createFamily(final ClickEvent e) {
        final String name = nameBox.getText();
        final String description = descriptionBox.getText();
        presenter.createFamily(name, description);
    }

    public void setFamilies(final ArrayList<Family> families) {

        final TextColumn<Family> idColumn = new TextColumn<Family>() {
            @Override
            public String getValue(final Family object) {
                return "" + object.id;
            }
        };
        final TextColumn<Family> nameColumn = new TextColumn<Family>() {
            @Override
            public String getValue(final Family object) {
                return object.name;
            }
        };
        final TextColumn<Family> descriptionColumn = new TextColumn<Family>() {
            @Override
            public String getValue(final Family object) {
                return object.description;
            }
        };

        familiesPanel = new DataGrid<Family>();
        familiesPanel.addColumn(idColumn, "Id");
        familiesPanel.addColumn(nameColumn, "Name");
        familiesPanel.addColumn(descriptionColumn, "Description");

        familiesPanel.setRowCount(families.size(), true);
        familiesPanel.setRowData(0, families);

        familiesPanel.setWidth("100%");
        familiesPanel.setHeight("200px");

        containerFamilies.setWidget(familiesPanel);
    }

    private String user;

    public void setUser(final String user) {
        this.user = user;
        userBox.setText("Hello " + user);
    }

    public String getUser() {
        return user;
    }

}
