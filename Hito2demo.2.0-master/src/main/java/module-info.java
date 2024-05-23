module com.empresa.hito2demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens com.empresa.hito2demo to javafx.fxml;
    exports com.empresa.hito2demo;
}