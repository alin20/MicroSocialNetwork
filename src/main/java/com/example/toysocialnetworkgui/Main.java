package com.example.toysocialnetworkgui;
import com.example.toysocialnetworkgui.domain.*;
import com.example.toysocialnetworkgui.domain.validators.EventValidator;
import com.example.toysocialnetworkgui.domain.validators.FriendshipValidator;
import com.example.toysocialnetworkgui.domain.validators.MessageValidator;
import com.example.toysocialnetworkgui.domain.validators.UserValidator;
import com.example.toysocialnetworkgui.repository.Repository;
import com.example.toysocialnetworkgui.repository.database.EventsDbRepository;
import com.example.toysocialnetworkgui.repository.database.FriendshipsDbRepository;
import com.example.toysocialnetworkgui.repository.database.MessageDbRepository;
import com.example.toysocialnetworkgui.repository.database.UserDbRepository;
import com.example.toysocialnetworkgui.repository.repoExceptions.FileError;
import com.example.toysocialnetworkgui.repository.repoExceptions.RepoException;
import com.example.toysocialnetworkgui.service.*;
import com.example.toysocialnetworkgui.ui.Runner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fileName="data/users.csv";
        String fileName2="data/friendships.csv";
        Repository<Long, User> userFileRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = null;
        Repository<Long, User> userDbRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipDbRepository = null;
        Repository<Long, Message> messageDbRepository = null;
        Repository<Long, Event> eventDbRepository = null;
        try {
            userDbRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","22adc#cJf6", new UserValidator());
            friendshipDbRepository = new FriendshipsDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","22adc#cJf6",new FriendshipValidator());
            messageDbRepository = new MessageDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","22adc#cJf6", new MessageValidator());
            eventDbRepository = new EventsDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","22adc#cJf6", new EventValidator());

        }
        catch (FileError ex){
            System.out.println(ex.getMessage());
            return;
        }
        catch (RepoException ex){
            System.out.println(ex.getMessage());
            return;
        }


        //DataBase
        UserService userService = new UserService(userDbRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipDbRepository);
        MessageService messageService = new MessageService(messageDbRepository);
        EventService eventService = new EventService(eventDbRepository);
        SuperService superService = new SuperService(friendshipService,userService,messageService,eventService);


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        LoginController mainController = fxmlLoader.getController();
        mainController.setServiceController(superService);


        Scene scene = new Scene(root, 700, 297, Color.SEAGREEN);
        primaryStage.setTitle("Micro Social Network");

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}



