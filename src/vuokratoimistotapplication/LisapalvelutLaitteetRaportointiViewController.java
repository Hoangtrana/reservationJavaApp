/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import vuokratoimistotDatabase.vuokratoimistoDatabase;
import vuokratoimistotapplication.Luokat.Palvelu;
import vuokratoimistotapplication.Luokat.Varaus;
import static vuokratoimistotapplication.PaavalikkoViewController.closeConnection;

/**
 * FXML Controller class
 *
 * @author matti
 */
public class LisapalvelutLaitteetRaportointiViewController implements Initializable {

    @FXML
    private DatePicker txtAloitusPaivaDate;
    @FXML
    private DatePicker txtLopetusPaivaDate;
    @FXML
    private ComboBox<Integer> cboValitseToimipiste;
    @FXML
    private TableView<Palvelu> tblViewPalvelu;
    @FXML
    private TableColumn<Palvelu, Integer> palvelunIDColumn;
    @FXML
    private TableColumn<Palvelu, String> palvelunNimiColumn;
    @FXML
    private TableColumn<Palvelu, Integer> palvelunHintaColumn;
    @FXML
    private TableColumn<Palvelu, String> palvelunKuvausColumn;
    @FXML
    private TableView<Varaus> tblViewVaraus;
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

    java.sql.Date sqlAloitusPaiva;
    java.sql.Date sqlLopetusPaiva;

    private static final String TOIMIPISTEID = "__TOIMIPISTEID__";
    private static final String TOIMIPISTENIMI = "__TOIMIPISTENIMI__";
    private static final String ALOITUSPAIVA = "__ALOITUSPAIVA__";
    private static final String LOPETUSPAIVA = "__LOPETUSPAIVA__";
    private static final String TABLE_CONTENT = "__TABLE__";
    @FXML
    private WebView webView;

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

        VaratutToimipisteetVarausIDColumn.setCellValueFactory(new PropertyValueFactory<>("varausID"));
        VaratutToimipisteetAloitusPaivaColumn.setCellValueFactory(new PropertyValueFactory<>("aloitusPaiva"));
        VaratutToimipisteetLopetusPaivaColumn.setCellValueFactory(new PropertyValueFactory<>("lopetusPaiva"));
        VaratutToimipisteetAsiakasIDColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
        VaratutToimipisteetToimipisteIDColumn.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));

        // Alustetaan ComboBox Toimipisteiden tiedoilla
        updateComboBoxToimipiste();
    }

    @FXML
    private void btnEtsiButtonPressed(ActionEvent event) {

        fillTableViews();
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

    /*
    * Metodi joka täyttää combobox komponentin toimipisteiden ID:llä
     */
    private void updateComboBoxToimipiste() {

        try {

            cboValitseToimipiste.getItems().clear();

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tietokannasta
            ResultSet toimipisteInfoResult = vuokratoimistoDatabase.selectToimipiste(conn);

            while (toimipisteInfoResult.next()) {
                cboValitseToimipiste.getItems().addAll(toimipisteInfoResult.getInt(1));
                //cboValitseToimipiste.setValue(toimipisteInfoResult.getString(2));
            }

            // Suljetaan tietokantayhteys
            vuokratoimistoDatabase.closeConnection(conn);

        }
        // Napataan kiinni SQL mahdolliset poikkeukset
        catch (SQLException ex) {
            System.out.println("Catchiin meni");
            java.util.logging.Logger.getLogger(LisapalvelutLaitteetRaportointiViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodi joka tyhjentää tableView komponentin varauksista
     */
    public void clearTableviewVaraus() {
        for (int i = 0; i < tblViewVaraus.getItems().size(); i++) {
            tblViewVaraus.getItems().clear();
        }
    }

    /**
     * Metodi joka tyhjentää tableView komponentin palveluista
     */
    public void clearTableviewPalvelu() {
        for (int i = 0; i < tblViewPalvelu.getItems().size(); i++) {
            tblViewPalvelu.getItems().clear();
        }
    }

    /**
     * Metodi joka täyttää tableView komponentit
     */
    public void fillTableViews() {

        try {

            // Tyhjennetään taulut
            clearTableviewPalvelu();
            clearTableviewVaraus();

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista               
            sqlAloitusPaiva = java.sql.Date.valueOf(txtAloitusPaivaDate.getValue());
            sqlLopetusPaiva = java.sql.Date.valueOf(txtLopetusPaivaDate.getValue());

            // Haetaan tiedot kolmesta eri taulusta
            ResultSet allInfoFromThreeTables = selectAllInfoFromThreeTables(conn, cboValitseToimipiste.getValue(), sqlAloitusPaiva, sqlLopetusPaiva);

            while (allInfoFromThreeTables.next()) {

                Varaus varaus = new Varaus(allInfoFromThreeTables.getInt("varausID"), allInfoFromThreeTables.getDate("aloitusPaiva"),
                        allInfoFromThreeTables.getDate("lopetusPaiva"), allInfoFromThreeTables.getInt("asiakasID"), allInfoFromThreeTables.getInt("toimipisteID"));
                tblViewVaraus.getItems().add(varaus);

                Palvelu palvelu = new Palvelu(allInfoFromThreeTables.getInt("palvelunID"), allInfoFromThreeTables.getString("palvelunNimi"),
                        allInfoFromThreeTables.getInt("palvelunHinta"), allInfoFromThreeTables.getString("palvelunKuvaus"));
                tblViewPalvelu.getItems().add(palvelu);
            }

            // Jos tietoja ei löydy, ilmoitetaan siitä käyttäjälle
            if (tblViewVaraus.getItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Haun tulokset:");
                alert.setHeaderText("Ei tietoja");
                alert.setContentText("Valitulle toimipisteelle ei ole palveluvarauksia valitulla aikavälillä.");
                alert.showAndWait();
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
     * Metodi joka hakee tietoa tietokannasta kolmesta eri taulusta
     *
     * @param c connection
     * @param toimipisteID toimipisteen ID
     * @param aloitusPaiva aloituspäivä
     * @param lopetusPaiva lopetuspäivä
     * @return resultset
     * @throws SQLException Tietokantavirhe
     */
    public static ResultSet selectAllInfoFromThreeTables(Connection c, int toimipisteID, Date aloitusPaiva, Date lopetusPaiva) throws SQLException {

        PreparedStatement ps = c.prepareStatement(
                "SELECT varaus.varausID, varaus.aloitusPaiva, varaus.lopetusPaiva, varaus.asiakasID, varaus.toimipisteID,"
                + " toimipisteidenPalvelut.toimipisteID, toimipisteidenPalvelut.palvelunID, toimipisteidenPalvelut.varausID, toimipisteidenPalvelut.asiakasID,"
                + " palvelu.palvelunID, palvelu.palvelunNimi, palvelu.palvelunHinta, palvelu.palvelunKuvaus"
                + " FROM varaus, toimipisteidenPalvelut, palvelu"
                + " WHERE toimipisteidenPalvelut.toimipisteID = ?"
                + " AND toimipisteidenPalvelut.varausID = varaus.varausID"
                + " AND toimipisteidenPalvelut.palvelunID = palvelu.palvelunID"
                + " AND ((aloitusPaiva BETWEEN ?"
                + " AND ?)"
                + " OR (lopetusPaiva BETWEEN ? AND ?))"
        );

        // Syötetään tiedot parametreilla
        ps.setInt(1, toimipisteID);
        ps.setDate(2, aloitusPaiva);
        ps.setDate(3, lopetusPaiva);
        ps.setDate(4, aloitusPaiva);
        ps.setDate(5, lopetusPaiva);

        ResultSet rs = ps.executeQuery();

        return rs;
    }

    @FXML
    private void menuItemSaveClicked(ActionEvent event) throws IOException {

        try {

            // Aukaistaan tietokantayhteys
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            // Asetetaan oikea tietokanta yhteydessa valituksi
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");

            // Haetaan tiedot tekstikentista               
            sqlAloitusPaiva = java.sql.Date.valueOf(txtAloitusPaivaDate.getValue());
            sqlLopetusPaiva = java.sql.Date.valueOf(txtLopetusPaivaDate.getValue());

            // Haetaan tiedot kolmesta eri taulusta
            ResultSet allInfoFromThreeTables = selectAllInfoFromThreeTables(conn, cboValitseToimipiste.getValue(), sqlAloitusPaiva, sqlLopetusPaiva);

            while (allInfoFromThreeTables.next()) {

                // 1. kopioi pohja
                File source = new File("./Report/VuokratutPalvelutRaporttiTemplate.html");
                File dest = new File("./Report/VuokratutPalvelutTallennaRaportti.html");

                Files.copy(source.toPath(), dest.toPath(), REPLACE_EXISTING);

                // 2. Korvaa pohjassa olevat paikat tiedoilla
                String content = new String(Files.readAllBytes(dest.toPath()), UTF_8);
                //content = content.replaceAll(TOIMIPISTEID, Integer.toString(allInfoFromThreeTables.getInt("toimipisteID")));
                content = content.replaceAll(TOIMIPISTEID, cboValitseToimipiste.getValue().toString());

                Statement stmt = conn.createStatement();
                ResultSet toimipisteJaIDInfoResult = stmt.executeQuery(
                        "SELECT toimipisteNimi FROM toimipiste WHERE toimipisteID LIKE '%" + cboValitseToimipiste.getValue() + "%'");

                while (toimipisteJaIDInfoResult.next()) {
                    content = content.replaceAll(TOIMIPISTENIMI, toimipisteJaIDInfoResult.getString("toimipisteNimi"));
                }

                //content = content.replaceAll(ALOITUSPAIVA, allInfoFromThreeTables.getDate("aloitusPaiva").toString());
                content = content.replaceAll(ALOITUSPAIVA, txtAloitusPaivaDate.getValue().toString());
                content = content.replaceAll(LOPETUSPAIVA, txtLopetusPaivaDate.getValue().toString());

                content = content.replaceAll(TABLE_CONTENT, createRow(allInfoFromThreeTables.getInt("palvelunID"),
                        allInfoFromThreeTables.getString("palvelunNimi"), allInfoFromThreeTables.getInt("palvelunHinta"), allInfoFromThreeTables.getString("palvelunKuvaus"),
                        allInfoFromThreeTables.getInt("varausID"), allInfoFromThreeTables.getDate("aloitusPaiva"), allInfoFromThreeTables.getDate("lopetusPaiva"),
                        allInfoFromThreeTables.getInt("asiakasID"), allInfoFromThreeTables.getInt("toimipisteID")));

                // 3. Lataa muodostettu lasku
                Files.write(dest.toPath(), content.getBytes(UTF_8));

                // 4. Näytä muodostettu lasku
                loadWebPage(dest.toPath().toString());

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

    @FXML
    private void menuItemPreviewClicked(ActionEvent event) {

        loadWebPage("./Report/VuokratutPalvelutRaporttiTemplate.html");
    }

    /*
    * Metodi joka lataa websivun
     */
    private void loadWebPage(String path) {
        WebEngine engine = webView.getEngine();
        File f = new File(path);
        engine.load(f.toURI().toString());
    }

    /*
    * Metodi joka muodostaan yhden rivin html muodossa
     */
    private String createRow(int palvelunID, String palvelunNimi, int palvelunHinta, String palvelunKuvaus, int varausID, Date aloitusPaiva, Date lopetusPaiva, int asiakasID, int toimipisteID) {

        return "<tr>"
                + "<td class=\"palvelunID\">" + palvelunID + "</td>"
                + "<td class=\"palvelunNimi\">" + palvelunNimi + "</td>"
                + "<td class=\"palvelunHinta\">" + palvelunHinta + "€</td>"
                + "<td class=\"palvelunKuvaus\">" + palvelunKuvaus + "</td>"
                + "<td class=\"varausID\">" + varausID + "</td>"
                + "<td class=\"aloitusPaiva\">" + aloitusPaiva + "</td>"
                + "<td class=\"lopetusPaiva\">" + lopetusPaiva + "</td>"
                + "<td class=\"asiakasID\">" + asiakasID + "</td>"
                + "<td class=\"toimipisteID\">" + toimipisteID + "</td>";

    }

}
