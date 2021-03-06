package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.Observer.Observer;
import com.example.toysocialnetworkgui.domain.Event;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainController implements Observer {

    SuperService superService;
    private User currentUser;

    ObservableList<User> allUsers = FXCollections.observableArrayList();
    ObservableList<Friendship> allRequests = FXCollections.observableArrayList();
    ObservableList<Event> allEvents = FXCollections.observableArrayList();

    @FXML
    Button sendDeleteButton;
    @FXML
    Button sendRequestButton;
    @FXML
    Button sendAcceptButton;
    @FXML
    Button sendRejectButton;
    @FXML
    Button viewFriendsButton;
    @FXML
    TextField usernameTextField;

    @FXML
    Label userName;

    @FXML
    TextField searchTextField;

    @FXML
    TableView<User> usersTableView;
    @FXML
    TableColumn<User,String> lastNameColumn;
    @FXML
    TableColumn<User,String> firstNameColumn;

    @FXML
    TableView<Friendship> friendshipTableView;

    @FXML
    TableColumn<Friendship,String> fromColumn;
    @FXML
    TableColumn<Friendship,String> toColumn;
    @FXML
    TableColumn<Friendship,String> statusColumn;
    @FXML
    TableColumn<Friendship, Date>  dateColumn;

    @FXML
    TableView<Event> eventsTableView;

    @FXML
    TableColumn<Event,String> eventNameColumn;
    @FXML
    TableColumn<Event,String> eventDescriptionColumn;
    @FXML
    TableColumn<Event,String> eventDateColumn;



    @FXML
    public void initialize() {
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));


        fromColumn.setCellValueFactory( param -> {
            User user = this.superService.findUserById(param.getValue().getFr1());
            ReadOnlyObjectWrapper<String> str = new ReadOnlyObjectWrapper<>();
            if(user != null)
                str.set(user.getLastName());
            return str;
        });
        toColumn.setCellValueFactory( param -> {
            User user = this.superService.findUserById(param.getValue().getFr2());
            ReadOnlyObjectWrapper<String> str = new ReadOnlyObjectWrapper<>();
            if(user != null)
                str.set(user.getLastName());
            return str;
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipStatus"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Descriere"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        usersTableView.setItems(allUsers);
        friendshipTableView.setItems(allRequests);
        eventsTableView.setItems(allEvents);
    }

        public void setServiceController(SuperService superService) {
            this.superService = superService;
        }

        public void setCurrentUser(User user) {
            this.currentUser = user;
        }

    public void afterLoad(SuperService superService,User user) {
        this.superService = superService;
        this.setCurrentUser(user);
        //Adaugam observer
        this.superService.addObserver(this);
        this.updateUsers();
        userName.setText(currentUser.getLastName());
        this.updateEvents();
        this.updateRequests();
    }

    @Override
    public void updateEvents() {
        this.allEvents.clear();
        Iterable<Event> events = this.superService.getAllEventsForUser(currentUser.getId());
        this.setEvents(events);
    }

    private void setEvents(Iterable<Event> events) {
        events.forEach(u->{this.allEvents.add(u);});
    }

    @Override
    public void updateRequests(){
        this.allRequests.clear();
        Iterable<Friendship> friendships = this.superService.allRequestsOfAUser(currentUser.getId());
        this.setFriendships(friendships);
    }
    public void setUsernames(Iterable<User> users) {
        users.forEach( u -> this.allUsers.add(u));
    }
    public void setFriendships(Iterable<Friendship> friendships) {
        friendships.forEach( u -> this.allRequests.add(u));
    }

    public void updateUsers() {
        this.allUsers.clear();
        Iterable<User> users = this.superService.getAllUsers();
        this.setUsernames(users);
    }

    @FXML
    public void sendRequest() {

        try {
            if (usersTableView.getSelectionModel().getSelectedItem() == null)
                return;
            this.superService.sendFriendRequest(currentUser.getId(),usersTableView.getSelectionModel().getSelectedItem().getId());
            this.updateRequests();
        }
        catch (ServiceException e){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error!");
           alert.setHeaderText("Can't send friend request!");
           alert.showAndWait();
        }
    }

    @FXML
    public void acceptRequest() {
        String response = "approved";
        try {
            if (friendshipTableView.getSelectionModel().getSelectedItem() == null)
                return;
            Long id_receiver;
            if (friendshipTableView.getSelectionModel().getSelectedItem().getFr1() == currentUser.getId())
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr2();
            else
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr1();

            Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();

            if (currentUser.getId() != friendship.getSender()  && (currentUser.getId() == friendship.getFr1() || currentUser.getId() == friendship.getFr2())  ) {
                this.superService.responseToFriendRequest(currentUser.getId(), id_receiver, response);
                //INAINTE DE OBSERVER TREBUIA APELATA FUNCTIA AICI
                //this.updateRequests();
            }
        }

        catch (ServiceException e){
            e.printStackTrace();
        }
    }



    @FXML
    public void rejectRequest() {
        String response = "rejected";
        try {
            if (friendshipTableView.getSelectionModel().getSelectedItem() == null)
                return;
            Long id_receiver;
            if(friendshipTableView.getSelectionModel().getSelectedItem().getFr1() == currentUser.getId())
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr2();
            else
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr1();
            Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();
            if (currentUser.getId() != friendship.getSender()  && (currentUser.getId() == friendship.getFr1() || currentUser.getId() == friendship.getFr2())  )
            {
                this.superService.responseToFriendRequest(currentUser.getId(),id_receiver,response);
                //INAINTE DE OBSERVER TREBUIA APELATA FUNCTIA AICI
                //this.updateRequests();
            }
        }
        catch (ServiceException e){
            e.printStackTrace();
        }
    }



    @FXML
    public void cancelRequest(){

        if (friendshipTableView.getSelectionModel().getSelectedItem() == null)
            return;

        Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();
        if (currentUser.getId() == friendship.getSender()  && (currentUser.getId() == friendship.getFr1() || currentUser.getId() == friendship.getFr2())  )
        {
            //delete friendship from db
            if(friendship.getFriendshipStatus().equals("pending")) {
                this.superService.deleteFriendship(friendship.getFr1(), friendship.getFr2());
                //INAINTE DE OBSERVER TREBUIA APELATA FUNCTIA AICI
                //this.updateRequests();
            }
        }
    }



    @FXML
    public void friendsButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("friends-view.fxml"));
            Parent root = fxmlLoader.load();
            //Scene scene = new Scene(root, 700, 600);
            Scene scene = new Scene(root, 950, 623, Color.SEAGREEN);
            current.setTitle("Micro");
            current.setScene(scene);
            FriendsListController ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messagesButtonClick(ActionEvent actionEvent) {
        try {
            User selected = usersTableView.getSelectionModel().getSelectedItem();
            if(selected == null)
                return;
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("chat-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 950, 623);
            current.setTitle("Messages");
            current.setScene(scene);
            ChatController ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,currentUser,selected);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void LogoutButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 700, 600);
            current.setTitle("MicroSocialNetwork");
            current.setScene(scene);
            LoginController mainController = fxmlLoader.getController();
            mainController.setServiceController(superService);

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

       @FXML
    public void eventsButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("events-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 950, 623);
            current.setTitle("MicroSocialNetwork");
            current.setScene(scene);
            EventController mainController = fxmlLoader.getController();
            mainController.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void notificationsButtonClick(ActionEvent actionEvent) {
    }

    public void showButton(ActionEvent actionEvent) {
        String username = searchTextField.getText();
        if(username.strip().isBlank())
            return;
        updateUserAfterSearch(username);
    }

    private void updateUserAfterSearch(String username) {
        this.allUsers.clear();
        Iterable<User> users = this.superService.findUsersByLastName(username);
        this.setUsernames(users);
    }

    public void refreshButtonClick(ActionEvent actionEvent) {
        updateUsers();
    }

    public void unsubButtonClick(ActionEvent actionEvent) {
        Event selected = eventsTableView.getSelectionModel().getSelectedItem();
        if(selected == null)
            return;
        this.superService.unsubscribeUserToEvent(currentUser.getId(),selected.getId());
        this.updateEvents();
    }
}

