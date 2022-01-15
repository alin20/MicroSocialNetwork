package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.ServiceException;
import com.example.toysocialnetworkgui.service.SuperService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class FriendsListController {
    SuperService superService;
    private User currentUser;

    @FXML
    ListView<User> friendsList;
    ObservableList<User> allFriends = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        friendsList.setCellFactory(param->new  FriendsListViewCell(currentUser));
        this.friendsList.setItems(allFriends);
    }

    public void backButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 950, 623);
            current.setTitle("Micro");
            current.setScene(scene);
            MainController ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,currentUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonClick(ActionEvent actionEvent) {
        User selected = friendsList.getSelectionModel().getSelectedItem();
        if(selected == null)
            return;
        this.superService.deleteFriendship(currentUser.getId(),selected.getId());
        this.updateFriends();
    }


    static final class FriendsListViewCell extends ListCell<User> {
        private final User CurrentUser;

        public  FriendsListViewCell(User user){
            this.CurrentUser = user;
        }

        @Override
        public void updateItem(User item, boolean empty){
            super.updateItem(item,empty);
            if(empty){
                setGraphic(null);
            }
            else{
                HBox hBox = new HBox();
                Label name = styleLabel(item.getFirstName()+" "+item.getLastName());
                hBox.setAlignment(Pos.CENTER_LEFT);
                name.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().add(name);
                hBox.setBackground(new Background(new BackgroundFill(Color.YELLOWGREEN,null,null)));
                setGraphic(hBox);
            }
        }

        private Label styleLabel(String msg){
            var label=new Label(msg);
            label.setMinWidth(50);
            label.setMinHeight(50);
            label.setStyle("-fx-hgap: 10px;" +
                    "    -fx-padding: 20px;" +
                    "" +
                    "    -fx-background-color: #ADD8E6;" +
                    "    -fx-background-radius: 25px;" +
                    "" +
                    "    -fx-border-radius: 25px;" +
                    "    -fx-border-width: 5px;" +
                    "    -fx-border-color: #997dff;" +
                    "-fx-text-fill: black;"+
                    "-fx-font-size: 25px");
            return label;
        }

    }

    public void afterLoad(SuperService superService, User currentUser) {
        this.superService = superService;
        this.currentUser = currentUser;
        //this.superService.addObserver(this);
        this.updateFriends();
    }

    public void updateFriends(){
        this.allFriends.clear();
        Iterable<User> allFriendss = this.superService.getAllFriendsForGivenUser(currentUser);
        this.setFriends(allFriendss);
    }

    public void setFriends(Iterable<User> friendss){
        friendss.forEach(u->this.allFriends.add(u));
    }

    public void setUsers(Iterable<User> users) {
        users.forEach( u -> this.allFriends.add(u));
    }
}
