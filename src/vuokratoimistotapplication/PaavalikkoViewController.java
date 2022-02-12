/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author matty
 */
public class PaavalikkoViewController implements Initializable {
   
    /**
     * Metodi joka sulkee tietokantayhteyden
     * @param c Yhteys
     * @throws java.sql.SQLException SQL poikkeus
     */
    public static void closeConnection(Connection c) throws SQLException {
        if (c != null) {
            c.close();
        }
        System.out.println("\t>> Tietokantayhteys suljettu");
    }  
    /**
     * Initializes the controller class.
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void menuCloseClicked(ActionEvent event) {
        
        
        // Yritetaan sulkea tietokantayhteys
        try {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
        closeConnection(conn);
        }
        // Napataan kiinni mahdolliset SQL poikkeukset
        catch (SQLException ex) {
            System.out.println("Catchiin meni");
            java.util.logging.Logger.getLogger(PaavalikkoViewController.class.getName()).log(Level.SEVERE, null, ex);       
        }
        // Suljetaan ohjelma ja ikkkuna
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void menuAboutClicked(ActionEvent event) {
        Alert about = new Alert(Alert.AlertType.INFORMATION, "(c)\tMatti Hanninen\n\tHoang Tran\n\tJanne Peiponen\n\tJanne Komu\n\tJasu Kokkola", ButtonType.CLOSE);
        about.setTitle("About");
        about.setHeaderText("VuokraToimistot Oy Application v1.0");
        about.show();
    }

    @FXML
    private void btnToimipisteidenHallinta(ActionEvent event) throws IOException {
        
        // Aukaistaan Toimipisteiden hallinta ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("ToimipisteidenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Toimipisteiden hallinta");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnPalveluidenHallinta(ActionEvent event) throws IOException {
        
        // Aukaistaan Palveluiden hallinta ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("PalveluidenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Palveluiden hallinta");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnVaraustenHallinta(ActionEvent event) throws IOException {
        
        // Aukaistaan Varausten hallinta ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("VaraustenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Varausten hallinta");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnAsiakkaidenHallinta(ActionEvent event) throws IOException {
        
        // Aukaistaan Asiakkaiden hallinta ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("AsiakkaidenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Asiakkaiden hallinta");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnLaskutusHallinta(ActionEvent event) throws IOException {
        
        // Aukaistaan Laskutus hallinta ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("LaskutusHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Laskujen hallinta");
        stage.setScene(scene);
        stage.show();
    }
    
     @FXML
    private void btnTyontekijoidenHallinta(ActionEvent event) throws IOException {
        
        // Aukaistaan Työntekijöiden hallinta ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("TyontekijoidenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Työntekijöiden hallinta");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void btnVuokratutTilatRaportointi(ActionEvent event) throws IOException {
        
        // Aukaistaan Vuokrattujen tilojen raportointi ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("VuokratutTilatRaportointiView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Vuokrattujen tilojen raportointi");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnLisapalvelutLaitteetRaportointi(ActionEvent event) throws IOException {
        
        // Aukaistaan Lisäpalveluiden ja laitteiden raportointi ikkuna
        Parent root = FXMLLoader.load(getClass().getResource("LisapalvelutLaitteetRaportointiView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Lisäpalveluiden ja laitteiden raportointi");
        stage.setScene(scene);
        stage.show();
    }

   
    
}
