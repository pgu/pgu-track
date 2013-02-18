package pgu.track.client;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SigninView extends Composite {

    private static SigninViewUiBinder uiBinder = GWT.create(SigninViewUiBinder.class);

    interface SigninViewUiBinder extends UiBinder<Widget, SigninView> {
    }

    @UiField
    TextBox familyUser, editorialUser;
    @UiField
    PasswordTextBox passBox;
    @UiField
    Button familyBtn, editorialBtn, adminBtn;

    private final Pgu_track presenter;

    public SigninView(final Pgu_track presenter) {
        initWidget(uiBinder.createAndBindUi(this));
        this.presenter= presenter;
    }

    @UiHandler("familyBtn")
    public void clickOnFamily(final ClickEvent e){
        presenter.onClickFamily(familyUser.getText());
    }

    @UiHandler("editorialBtn")
    public void clickOnEditorial(final ClickEvent e){
        presenter.onClickEditorial(editorialUser.getText());
    }

    @UiHandler("adminBtn")
    public void clickOnAdmin(final ClickEvent e){
        presenter.onClickAdmin(passBox.getText());
    }

}
