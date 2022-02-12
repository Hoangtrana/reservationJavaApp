/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication;

import vuokratoimistotDatabase.vuokratoimistoDatabase;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vuokratoimistotapplication.Luokat.Toimipiste;
import static vuokratoimistotapplication.PaavalikkoViewController.closeConnection;

/**
 * FXML Controller class
 *
 * @author Matti
 */
public class ToimipisteidenHallintaViewController implements Initializable {

    @FXML
    private TableView<Toimipiste> tblViewToimipiste;
    @FXML
    private TableColumn<Toimipiste, Integer> toimipisteIDColumn;
    @FXML
    private TableColumn<Toimipiste, String> toimipisteNimiColumn;
    @FXML
    private TableColumn<Toimipiste, Integer> toimipisteHintaColumn;
    @FXML
    private TableColumn<Toimipiste, Integer> toimipisteKokoColumn;
    @FXML
    private TextField txtToimipisteID;
    @FXML
    private TextField txtToimipisteNimi;
    @FXML
    private TextField txtVuorokausiHinta;
    @FXML
    private TextField txtToimipisteKoko;

    /**
     * Initializes the controller class.
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Alustetaan columnit
        toimipisteIDColumn.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));
        toimipisteNimiColumn.setCellValueFactory(new PropertyValueFactory<>("toimipisteNimi"));
        toimipisteHintaColumn.setCellValueFactory(new PropertyValueFactory<>("vuorokausiHinta"));
        toimipisteKokoColumn.setCellValueFactory(new PropertyValueFactory<>("toimipisteKoko"));

        fillTableView();
    }

    @FXML
    private void menuAddNewClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.addToimipiste(conn, Integer.parseInt(txtToimipisteID.getText()), txtToimipisteNimi.getText(),
                    Integer.parseInt(txtVuorokausiHinta.getText()), Integer.parseInt(txtToimipisteKoko.getText()));

            // Tyhjennetaan TableView komponentti
            clearTableview();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableView();

            // Suljetaan tietokantayhteys
            vuokratoimistoDatabase.closeConnection(conn);
        }
        
        // Napataan kiinni mahdolliset SQL poikkeukset
        catch (SQLException ex) {
            System.out.println("Catchiin meni");
            java.util.logging.Logger.getLogger(ToimipisteidenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        // Suljetaan ikkkuna    
        Stage stage = (Stage) tblViewToimipiste.getScene().getWindow();
        stage.close();
    }


    /**
     * Metodi joka tayttaa TableView komponentin tietokannan tiedoilla
     */
    public void fillTableView() {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tietokannasta
            ResultSet toimipisteInfoResult = vuokratoimistoDatabase.selectToimipiste(conn);

            while (toimipisteInfoResult.next()) {
                Toimipiste toimipiste = new Toimipiste(toimipisteInfoResult.getInt("toimipisteID"), toimipisteInfoResult.getString("toimipisteNimi"),
                        toimipisteInfoResult.getInt("vuorokausiHinta"), toimipisteInfoResult.getInt("toimipisteKoko"));
                tblViewToimipiste.getItems().add(toimipiste);
            }

            // Suljetaan tietokantayhteys
            vuokratoimistoDatabase.closeConnection(conn);
        }

        // Napataan kiinni mahdolliset SQL poikkeukset
        catch (SQLException ex) {
            System.out.println("Catchiin meni");
            java.util.logging.Logger.getLogger(ToimipisteidenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodi joka tyhjentaa TableView komponentin
     */
    public void clearTableview() {
        for (int i = 0; i < tblViewToimipiste.getItems().size(); i++) {
            tblViewToimipiste.getItems().clear();
        }
    }

    @FXML
    private void menuEditClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.updateToimipiste(conn, Integer.parseInt(txtToimipisteID.getText()), txtToimipisteNimi.getText(),
                    Integer.parseInt(txtVuorokausiHinta.getText()), Integer.parseInt(txtToimipisteKoko.getText()));

            // Tyhjennetaan TableView komponentti
            clearTableview();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableView();

            // Suljetaan tietokantayhteys
            vuokratoimistoDatabase.closeConnection(conn);
        }

        // Napataan kiinni mahdolliset SQL poikkeukset
        catch (SQLException ex) {
            System.out.println("Catchiin meni");
            java.util.logging.Logger.getLogger(ToimipisteidenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void menuDeleteClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.deleteToimipiste(conn, Integer.parseInt(txtToimipisteID.getText()));

            // Tyhjennetaan TableView komponentti
            clearTableview();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableView();

            // Suljetaan tietokantayhteys
            vuokratoimistoDatabase.closeConnection(conn);
        }

        // Napataan kiinni mahdolliset SQL poikkeukset
        catch (SQLException ex) {
            System.out.println("Catchiin meni");
            java.util.logging.Logger.getLogger(ToimipisteidenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
