package com.example.toysocialnetworkgui;
import com.example.toysocialnetworkgui.Utils.constants.HashPassword;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.SuperService;
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

public class SignupController {
    SuperService superService;

    @FXML
    Label firstNameLabel;
    @FXML
    Label lastNameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    Button loginButton;


    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String first_name = firstNameTextField.getText();
        String last_name = lastNameTextField.getText();
        String password = HashPassword.passHashing(passwordTextField.getText());
        if(first_name.equals("") || last_name.equals("") || password.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error :( !");
            alert.setHeaderText("Invalid credentials!\n");
            alert.showAndWait();
            return;
        }
        this.superService.addUser(first_name,last_name,password);
    }

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }

    @FXML
    public void onBackButtonClick(ActionEvent event) {

        try {
            Node source = (Node) event.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Parent root = fxmlLoader.load();
            // Scene scene = new Scene(root, 700, 600);
            Scene scene = new Scene(root, 700, 600, Color.SEAGREEN);
            current.setTitle("Micro");
            current.setScene(scene);
            LoginController mainController = fxmlLoader.getController();
            mainController.setServiceController(superService);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
