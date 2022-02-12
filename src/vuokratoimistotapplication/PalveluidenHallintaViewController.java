/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import vuokratoimistotDatabase.vuokratoimistoDatabase;
import vuokratoimistotapplication.Luokat.Palvelu;
import vuokratoimistotapplication.Luokat.ToimipisteidenPalvelut;
import vuokratoimistotapplication.Luokat.Varaus;
import static vuokratoimistotapplication.PaavalikkoViewController.closeConnection;

/**
 * FXML Controller class
 *
 * @author Matti
 */
public class PalveluidenHallintaViewController implements Initializable {

    @FXML
    private TableView<Palvelu> tblViewPalvelu;
    @FXML
    private TextField txtPalvelunID;
    @FXML
    private TextField txtPalvelunNimi;
    @FXML
    private TextField txtPalvelunHinta;
    @FXML
    private TextField txtPalvelunKuvaus;
    @FXML
    private TableColumn<Palvelu, Integer> palvelunIDColumn;
    @FXML
    private TableColumn<Palvelu, String> palvelunNimiColumn;
    @FXML
    private TableColumn<Palvelu, Integer> palvelunHintaColumn;
    @FXML
    private TableColumn<Palvelu, String> palvelunKuvausColumn;
    @FXML
    private TextField txtToimipisteIDVaraus;
    @FXML
    private TextField txtPalvelunIDVaraus;
    @FXML
    private TextField txtVarausIDVaraus;
    @FXML
    private TableView<ToimipisteidenPalvelut> tblViewVaratutPalvelut;
    @FXML
    private TableView<Varaus> tblViewVaratutToimipisteet;
    @FXML
    private TableColumn<Varaus, Integer> VaratutToimipisteetVarausIDColumn;
    @FXML
    private TableColumn<Varaus, Date> VaratutToimipisteetAloitusPaivaColumn;
    @FXML
    private TableColumn<Varaus, Date> VaratutToimipisteetLopetusPaivaColumn;
    @FXML
    private TableColumn<Varaus, Integer> VaratutToimipisteetAsiakasIDColumn;
    @FXML
    private TableColumn<Varaus, Integer> VaratutToimipisteetToimipisteIDColumn;
    @FXML
    private TableColumn<ToimipisteidenPalvelut, Integer> VaratutPalvelutToimipisteIDColumn;
    @FXML
    private TableColumn<ToimipisteidenPalvelut, Integer> VaratutPalvelutPalveluIDColumn;
    @FXML
    private TableColumn<ToimipisteidenPalvelut, Integer> VaratutPalvelutVarausIDColumn;
    @FXML
    private TextField txtAsiakasIDVaraus;
    @FXML
    private TableColumn<ToimipisteidenPalvelut, Integer> VaratutPalvelutAsiakasIDColumn;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Alustetaan columnit
        palvelunIDColumn.setCellValueFactory(new PropertyValueFactory<>("palvelunID"));
        palvelunNimiColumn.setCellValueFactory(new PropertyValueFactory<>("palvelunNimi"));
        palvelunHintaColumn.setCellValueFactory(new PropertyValueFactory<>("palvelunHinta"));
        palvelunKuvausColumn.setCellValueFactory(new PropertyValueFactory<>("palvelunKuvaus"));
        fillTableViewPalvelu();

        VaratutToimipisteetVarausIDColumn.setCellValueFactory(new PropertyValueFactory<>("varausID"));
        VaratutToimipisteetAloitusPaivaColumn.setCellValueFactory(new PropertyValueFactory<>("aloitusPaiva"));
        VaratutToimipisteetLopetusPaivaColumn.setCellValueFactory(new PropertyValueFactory<>("lopetusPaiva"));
        VaratutToimipisteetAsiakasIDColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
        VaratutToimipisteetToimipisteIDColumn.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));

        fillTableViewVaratutToimipisteet();

        VaratutPalvelutToimipisteIDColumn.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));
        VaratutPalvelutPalveluIDColumn.setCellValueFactory(new PropertyValueFactory<>("palvelunID"));
        VaratutPalvelutVarausIDColumn.setCellValueFactory(new PropertyValueFactory<>("varausID"));
        VaratutPalvelutAsiakasIDColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));

        fillTableViewVaratutPalvelut();
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
        Stage stage = (Stage) tblViewPalvelu.getScene().getWindow();
        stage.close();
    }

    /**
     * Metodi joka tayttaa TableView komponentin tietokannan tiedoilla
     */
    public void fillTableViewPalvelu() {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tietokannasta
            ResultSet palveluInfoResult = vuokratoimistoDatabase.selectPalvelu(conn);

            while (palveluInfoResult.next()) {
                Palvelu palvelu = new Palvelu(palveluInfoResult.getInt("palvelunID"), palveluInfoResult.getString("palvelunNimi"),
                        palveluInfoResult.getInt("palvelunHinta"), palveluInfoResult.getString("palvelunKuvaus"));
                tblViewPalvelu.getItems().add(palvelu);
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
     * Metodi joka tayttaa TableView komponentin tietokannan tiedoilla
     */
    public void fillTableViewVaratutToimipisteet() {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tietokannasta
            ResultSet varausInfoResult = vuokratoimistoDatabase.selectVaraus(conn);

            while (varausInfoResult.next()) {
                Varaus varaus = new Varaus(varausInfoResult.getInt("varausID"), varausInfoResult.getDate("aloitusPaiva"),
                        varausInfoResult.getDate("lopetusPaiva"), varausInfoResult.getInt("asiakasID"), varausInfoResult.getInt("toimipisteID"));
                tblViewVaratutToimipisteet.getItems().add(varaus);
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
     * Metodi joka tayttaa TableView komponentin tietokannan tiedoilla
     */
    public void fillTableViewVaratutPalvelut() {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tietokannasta
            ResultSet varatutPalvelutInfoResult = vuokratoimistoDatabase.selectVaratutPalvelut(conn);

            while (varatutPalvelutInfoResult.next()) {
                ToimipisteidenPalvelut toimipisteidenPalvelut = new ToimipisteidenPalvelut(varatutPalvelutInfoResult.getInt("toimipisteID"),
                        varatutPalvelutInfoResult.getInt("palvelunID"), varatutPalvelutInfoResult.getInt("varausID"), varatutPalvelutInfoResult.getInt("asiakasID"));
                tblViewVaratutPalvelut.getItems().add(toimipisteidenPalvelut);
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
    public void clearTableviewPalvelu() {
        for (int i = 0; i < tblViewPalvelu.getItems().size(); i++) {
            tblViewPalvelu.getItems().clear();
        }
    }

    /**
     * Metodi joka tyhjentaa TableView komponentin
     */
    public void clearTableviewVaratutPalvelut() {
        for (int i = 0; i < tblViewPalvelu.getItems().size(); i++) {
            tblViewVaratutPalvelut.getItems().clear();
        }
    }

    @FXML
    private void menuAddNewServiceClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.addPalvelu(conn, Integer.parseInt(txtPalvelunID.getText()), txtPalvelunNimi.getText(),
                    Integer.parseInt(txtPalvelunHinta.getText()), txtPalvelunKuvaus.getText());

            // Tyhjennetaan TableView komponentti
            clearTableviewPalvelu();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableViewPalvelu();

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
    private void menuAddNewReservationClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.addToimipisteidenPalvelut(conn, Integer.parseInt(txtToimipisteIDVaraus.getText()),
                    Integer.parseInt(txtPalvelunIDVaraus.getText()), Integer.parseInt(txtVarausIDVaraus.getText()), Integer.parseInt(txtAsiakasIDVaraus.getText()));

            // Tyhjennetaan TableView komponentti
            clearTableviewVaratutPalvelut();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableViewVaratutPalvelut();

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
    private void menuEditServiceClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.updatePalvelu(conn, Integer.parseInt(txtPalvelunID.getText()), txtPalvelunNimi.getText(),
                    Integer.parseInt(txtPalvelunHinta.getText()), txtPalvelunKuvaus.getText());

            // Tyhjennetaan TableView komponentti
            clearTableviewPalvelu();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableViewPalvelu();

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
    private void menuDeleteServiceClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.deletePalvelu(conn, Integer.parseInt(txtPalvelunID.getText()));

            // Tyhjennetaan TableView komponentti
            clearTableviewPalvelu();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableViewPalvelu();

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
    private void menuEditReservationClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.updatePalveluVaraus(conn, Integer.parseInt(txtToimipisteIDVaraus.getText()),
                    Integer.parseInt(txtPalvelunIDVaraus.getText()), Integer.parseInt(txtVarausIDVaraus.getText()), Integer.parseInt(txtAsiakasIDVaraus.getText()));

            // Tyhjennetaan TableView komponentti
            clearTableviewVaratutPalvelut();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableViewVaratutPalvelut();

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
    private void menuDeleteReservationClicked(ActionEvent event) {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista
            vuokratoimistoDatabase.deletePalveluVaraus(conn, Integer.parseInt(txtPalvelunIDVaraus.getText()));

            // Tyhjennetaan TableView komponentti
            clearTableviewVaratutPalvelut();

            // Lisataan tiedot TableView komponenttiin uudelleen
            fillTableViewVaratutPalvelut();

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
