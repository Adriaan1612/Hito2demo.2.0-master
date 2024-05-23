package com.empresa.hito2demo;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.bson.Document;

public class HelloController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField filterField;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> ageColumn;

    @FXML
    private TableColumn<User, String> phoneColumn;

    @FXML
    private TableColumn<User, String> postalCodeColumn;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private ObservableList<User> allUsers;
    private int selectedIndex = -1;

    public HelloController() {
        try {
            mongoClient = MongoClients.create("mongodb+srv://admin:curso@cluster0.g0oznlj.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
            database = mongoClient.getDatabase("hito");
            collection = database.getCollection("hito");
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción apropiadamente
        }
    }

    @FXML
    private void addUser() {
        String name = nameField.getText();
        String age = ageField.getText();
        String phone = phoneField.getText();
        String postalCode = postalCodeField.getText();

        // Aseguramos que el campo de teléfono solo contenga números
        if (!phone.matches("^\\d+$")) {
            showAlert("Error", "Número de Teléfono Inválido", "El número de teléfono solo puede contener números.");
            return;
        }

        // Validar que el código postal tenga 5 dígitos
        if (!isValidPostalCode(postalCode)) {
            showAlert("Error", "Código Postal Inválido", "El código postal debe tener exactamente 5 dígitos.");
            return;
        }

        // Validar que el número de teléfono tenga al menos 9 dígitos
        if (!isValidPhoneNumber(phone)) {
            showAlert("Error", "Número de Teléfono Inválido", "El número de teléfono debe tener al menos 9 dígitos.");
            return;
        }

        User user = new User(name, age, phone, postalCode);
        userTableView.getItems().add(user);
        clearInputs();

        try {
            Document doc = new Document("nombre", name)
                    .append("edad", age)
                    .append("telefono", phone)
                    .append("codigo_postal", postalCode);
            collection.insertOne(doc);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción apropiadamente
        }
    }

    @FXML
    private void removeUser() {
        if (selectedIndex >= 0) {
            User selectedUser = userTableView.getItems().get(selectedIndex);

            Document filter = new Document("nombre", selectedUser.getName())
                    .append("edad", selectedUser.getAge())
                    .append("telefono", selectedUser.getPhone())
                    .append("codigo_postal", selectedUser.getPostalCode());

            collection.deleteOne(filter);

            userTableView.getItems().remove(selectedIndex);
            clearInputs();
        } else {
            System.err.println("No se ha seleccionado ningún usuario para eliminar.");
        }
    }

    @FXML
    private void updateUser() {
        if (selectedIndex >= 0) {
            User selectedUser = userTableView.getItems().get(selectedIndex);

            String newName = nameField.getText();
            String newAge = ageField.getText();
            String newPhone = phoneField.getText();
            String newPostalCode = postalCodeField.getText();

            // Aseguramos que el campo de teléfono solo contenga números
            if (!newPhone.matches("^\\d+$")) {
                showAlert("Error", "Número de Teléfono Inválido", "El número de teléfono solo puede contener números.");
                return;
            }

            // Validar que el código postal tenga 5 dígitos
            if (!isValidPostalCode(newPostalCode)) {
                showAlert("Error", "Código Postal Inválido", "El código postal debe tener exactamente 5 dígitos.");
                return;
            }

            // Validar que el número de teléfono tenga al menos 9 dígitos
            if (!isValidPhoneNumber(newPhone)) {
                showAlert("Error", "Número de Teléfono Inválido", "El número de teléfono debe tener 9 dígitos.");
                return;
            }

            Document filter = new Document("nombre", selectedUser.getName())
                    .append("edad", selectedUser.getAge())
                    .append("telefono", selectedUser.getPhone())
                    .append("codigo_postal", selectedUser.getPostalCode());

            Document updatedDoc = new Document("nombre", newName)
                    .append("edad", newAge)
                    .append("telefono", newPhone)
                    .append("codigo_postal", newPostalCode);

            collection.replaceOne(filter, updatedDoc);

            selectedUser.setName(newName);
            selectedUser.setAge(newAge);
            selectedUser.setPhone(newPhone);
            selectedUser.setPostalCode(newPostalCode);

            userTableView.refresh();
            clearInputs();
        } else {
            System.err.println("No se ha seleccionado ningún usuario para actualizar.");
        }
    }

    @FXML
    private void filterUsers() {
        String searchText = filterField.getText().toLowerCase();
        ObservableList<User> filteredList = FXCollections.observableArrayList();

        for (User user : allUsers) {
            if (user.getName().toLowerCase().contains(searchText)) {
                filteredList.add(user);
            }
        }

        userTableView.setItems(filteredList);
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        userTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                ageField.setText(newValue.getAge());
                phoneField.setText(newValue.getPhone());
                postalCodeField.setText(newValue.getPostalCode());
                selectedIndex = userTableView.getSelectionModel().getSelectedIndex();
                userTableView.setStyle("-fx-background-color: #f8ff00; -fx-border-color: #ff0303; -fx-border-width: 1px;");
            }
        });

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        allUsers = FXCollections.observableArrayList();
        ObservableList<User> users = FXCollections.observableArrayList();
        for (Document doc : collection.find()) {
            String name = doc.getString("nombre");
            String age = doc
                    .getString("edad");
            String phone = doc.getString("telefono");
            String postalCode = doc.getString("codigo_postal");
            User user = new User(name, age, phone, postalCode);
            users.add(user);
            allUsers.add(user);
        }
        userTableView.setItems(users);
    }

    private void clearInputs() {
        nameField.clear();
        ageField.clear();
        phoneField.clear();
        postalCodeField.clear();
    }

    private boolean isValidPostalCode(String postalCode) {
        return postalCode.matches("^\\d{5}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{9,}$");
    }

    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public void buttonMouseExited(MouseEvent mouseEvent) {
    }

    public void buttonMouseEntered(MouseEvent mouseEvent) {
    }

    public static class User {
        private String name;
        private String age;
        private String phone;
        private String postalCode;

        public User(String name, String age, String phone, String postalCode) {
            this.name = name;
            this.age = age;
            this.phone = phone;
            this.postalCode = postalCode;
        }

        public String getName() { return name; }
        public String getAge() { return age; }
        public String getPhone() { return phone; }
        public String getPostalCode() { return postalCode; }

        public void setName(String name) { this.name = name; }
        public void setAge(String age) { this.age = age; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    }
}
