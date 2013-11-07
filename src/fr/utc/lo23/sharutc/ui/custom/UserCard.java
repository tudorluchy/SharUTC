package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.scene.control.Label;

public class UserCard extends SimpleCard {
    private final UserInfo mUserInfo;

    public Label userLogin;
    public Label userName;

    public UserCard(UserInfo userInfo) {
        super("../fxml/user_card.fxml");

        mUserInfo = userInfo;
        userLogin.setText(mUserInfo.getLogin());
        userName.setText(mUserInfo.getFirstName() + " " + mUserInfo.getLastName());
    }

    public UserInfo getModel() {
        return mUserInfo;
    }

}