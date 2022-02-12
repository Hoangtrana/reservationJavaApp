/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication;

import java.io.IOException;
import vuokratoimistotapplication.Luokat.Asiakas;
import vuokratoimistotDatabase.vuokratoimistoDatabase;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.openConnection;

/**
 * FXML Controller class
 *
 * @author matty
 * @author janne
 */
public class AsiakkaidenHallintaViewController implements Initializable {

    @FXML
    private Label lblAsiakkaat; /**Label lblAsiakkaat*/
    @FXML
    private TextField txtId; /**TextField txtId*/
    @FXML
    private TextField txtEtunimi; /**TextField txtEtunimi*/
    @FXML
    private TextField txtSukunimi; /**TextField txtSukunimi*/
    @FXML
    private TextField txtYritys; /**TextField txtYritys*/
    @FXML
    private HBox btnMuokkaa; /**HBox btnMuokkaa*/
    @FXML
    private Button btnLisaa; /**Button btnLisaa*/
    @FXML
    private Button btnPoista; /**Button btnPoista*/
    @FXML
    private TableView<Asiakas> tableAsiakas; /**TableView tableAsiakas*/
    
    @FXML
    private TableColumn<Asiakas, String> colEtunimi; /**TableColumn colEtunimi*/
    @FXML
    private TableColumn<Asiakas, String> colSukunimi; /**TableColumn colSukunimi*/
    @FXML
    private TableColumn<Asiakas, String> colYritys; /**TableColumn colYritys*/
    @FXML
    private TableColumn<Asiakas, Integer> colId; /**TableColumn colId*/
    
  
    vuokratoimistoDatabase dataOlio = new vuokratoimistoDatabase();
    
    @FXML
    private TextField txfEtsiAsikasID; /**TextField txfEtsiAsikasID*/
    

    /**
     * Initializes the controller class.
     * @param url url
     * @param rb resourcebundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     updateTableviewAsiakas();
       
     //prompt text ei näy ekassa textfielissä, koska se valitaan ekaksi controlliksi, siksi setFocusTraversable(false) metodi.
     txtId.setFocusTraversable(false);
     txtEtunimi.setFocusTraversable(false);
     txtSukunimi.setFocusTraversable(false);
     txtYritys.setFocusTraversable(false);
     
    }   


    /**
     *tableview syöttää tauluun tiedot
     */
    public void updateTableviewAsiakas(){
        
        colId.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
        colEtunimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
        colSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        colYritys.setCellValueFactory(new PropertyValueFactory<>("yritys"));
        
        try {
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
            
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
            
            ResultSet namesResult = vuokratoimistoDatabase.selectAsiakas(conn);
            
            while (namesResult.next()) {
                Asiakas person = new Asiakas(namesResult.getInt("asiakasID"), namesResult.getString("etunimi"), namesResult.getString("sukunimi"), namesResult.getString("yritys"));
                tableAsiakas.getItems().add(person);

            }
            
            vuokratoimistoDatabase.closeConnection(conn);
        } catch (SQLException ex) {
            Logger.getLogger(AsiakkaidenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Tyhjennetään asiakas tableview*/
    public void clearTableviewAsiakas(){
        for (int i=0;i<tableAsiakas.getItems().size();i++){
            tableAsiakas.getItems().clear();
        }
    }
    
    /**
     *Poistaa aiakkaan tiedot tietokannasta
     * @param c mariaDB etäyhteys
     * @param asiakasID asettaa asiakkaan yksilöivän ID:n
     * @throws SQLException SQL virheet kerää
     */
    public static void deleteAsiakas(Connection c, int asiakasID) throws SQLException {
        PreparedStatement ps = c.prepareStatement( 
        ("DELETE FROM asiakas WHERE asiakasID=?")
                
    );
    
        ps.setInt(1, asiakasID);
    
        ps.execute();
        System.out.println("\t>> poistettu opiskelija_id " + asiakasID);
   
    }
    

    /**
     *muokataan opiskelijaa
     * @param c yhteys tietokantaan
     * @param asiakasID asikaan id
     * @param etunimi asiakkaan etunimi
     * @param sukunimi asiakkaan sukunimi
     * @param yritys asikaan yrityksen nimi
     * @throws SQLException SQL virheet 
     */
    public static void editAsiakas(Connection c, int asiakasID, String etunimi, String sukunimi, String yritys) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
        ("UPDATE asiakas SET etunimi=?, sukunimi=?, yritys=? WHERE asiakasID=?")
        );

        ps.setString(1,etunimi);
        ps.setString(2,sukunimi);
        ps.setString(3,yritys);
        ps.setInt(4,asiakasID); 
   
        try {
        ps.execute();
        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Asiakkaan tietojen muokkaaminen");
            alert.setHeaderText("Virhe");
            alert.setContentText("Asiakkaan muokkaaminen epäonnistui");
            alert.showAndWait();
        }
    
        System.out.println("\t>> Päivitetty asiakasID tiedot: " + asiakasID);
        
    }
    /**
    * Lisää asiakkaan tietokantaan
    * @param event klikataan lisää buttonia
    * @throws SQLException  SQL virhe
    */
    @FXML
    private void LisaaAsiakas(ActionEvent event) throws SQLException {
        
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
             
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
        vuokratoimistoDatabase.addAsiakas(conn, Integer.parseInt(txtId.getText()), txtEtunimi.getText(), txtSukunimi.getText(), txtYritys.getText());
     
        clearTableviewAsiakas();
        
        updateTableviewAsiakas();
        
        vuokratoimistoDatabase.closeConnection(conn);
    }
    /**
     * Muokkaa asiakkaan tietoja tietokannasta
     * @param event klikataan muokkaa buttonia
     * @throws SQLException  SQL virhe
     */
    @FXML
    private void MuokkaaAsiakas(ActionEvent event) throws SQLException {
       
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
               
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
        editAsiakas(conn, Integer.parseInt(txtId.getText()), txtEtunimi.getText(),
                txtSukunimi.getText(), txtYritys.getText());
        
        clearTableviewAsiakas();
        
        updateTableviewAsiakas();
        
        vuokratoimistoDatabase.closeConnection(conn);
    }
    /**
     * Poistaa asikkaan tiedot tietokannasta 
     * @param event klikataan poista buttonia
     * @throws SQLException SQL virheet
     */
    @FXML
    private void PoistaAsiakas(ActionEvent event) throws SQLException {
      
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
               
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
        deleteAsiakas(conn, Integer.parseInt(txtId.getText()));
        
        clearTableviewAsiakas();
        
        updateTableviewAsiakas();
        
        vuokratoimistoDatabase.closeConnection(conn);
    }
    
    /**
     * Textfieldit täyttyvät, kun klikataan tableviewta
     * @param event klikataan tablevieta
     * @throws SQLException SQL virheet
     */
    @FXML
    private void tblViewAsiakasClickedFilllTextfield(MouseEvent event) throws SQLException {
  
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
             
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
   
        Asiakas user = tableAsiakas.getSelectionModel().getSelectedItem();
          
        PreparedStatement pst = conn.prepareStatement(
        ("SELECT * from asiakas where asiakasID =?")
        );
        
        pst.setInt(1, user.getAsiakasID());
        ResultSet rs = pst.executeQuery();
           
           while(rs.next()){
               txtId.setText(rs.getString("asiakasID"));
               txtEtunimi.setText(rs.getString("etunimi"));
               txtSukunimi.setText(rs.getString("sukunimi"));
               txtYritys.setText(rs.getString("yritys"));
           }
    
        vuokratoimistoDatabase.closeConnection(conn);
    }

    /**
     * Sulkee asiakas ikkunan
     * @param event klikataan hiirellä
     */
    @FXML
    private void closeWindow(ActionEvent event) {    
        Stage stage = (Stage) tableAsiakas.getScene().getWindow();
        stage.close();
    }

    /**
     * aukaisee palveluiden ikkunan
     * @param event hiirellä klikataan
     * @throws IOException virhe
     */
    @FXML
    private void openPalvelutWindow(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PalveluidenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Palveluiden hallinta");
        stage.setScene(scene);
        stage.show();
        
    }
    /**
     * asukaisee toimipisteet ikkunan
     * @param event hiirellä klikataan
     * @throws IOException virhe
     */
    @FXML
    private void openToimipisteWindow(ActionEvent event) throws IOException {
       
        Parent root = FXMLLoader.load(getClass().getResource("ToimipisteidenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Toimipisteiden hallinta");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * aukaisee varaukset ikkunan
     * @param event hiirellä
     * @throws IOException 
     */
    @FXML
    private void openVarausWindow(ActionEvent event) throws IOException {
       
        Parent root = FXMLLoader.load(getClass().getResource("VaraustenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Varausten hallinta");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Aukaistaan Laskutus hallinta ikkuna
     * @param event hiirellä
     * @throws IOException 
     */
    @FXML
    private void openLaskuWindow(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("LaskutusHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Laskujen hallinta");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Aukaistaan Työntekijöiden hallinta ikkuna
     * @param event
     * @throws IOException 
     */
    @FXML
    private void openTyontekijatWindow(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("TyontekijoidenHallintaView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Työntekijöiden hallinta");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Aukaistaan Vuokrattujen tilojen raportointi ikkuna
     * @param event hiirellä
     * @throws IOException virhe
     */
    @FXML
    private void openVuokratutWindow(ActionEvent event) throws IOException {
       
        Parent root = FXMLLoader.load(getClass().getResource("VuokratutTilatRaportointiView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Vuokrattujen tilojen raportointi");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Aukaistaan Lisäpalveluiden ja laitteiden raportointi ikkuna
     * @param event hiirellä
     * @throws IOException virhe
     */
    @FXML
    private void openLisapalvelutWindow(ActionEvent event) throws IOException {
         
        Parent root = FXMLLoader.load(getClass().getResource("LisapalvelutLaitteetRaportointiView.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Lisäpalveluiden ja laitteiden raportointi");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * tyhjentää laskusta tulevan tableviewn tiedot textfieldistä
     * @param event hiirellä
     */
    @FXML
    private void editClearTextfield(ActionEvent event) {
     txtId.setText("");
     txtEtunimi.setText("");
     txtSukunimi.setText("");
     txtYritys.setText("");
        
    }
    /**
     * etsii asikkaan asiakasID tiedot tableviewstä
     * @param event näppäimistön painalluksella
     * @throws SQLException SQL virhe
     */
    @FXML
    private void etsiAsiakasIDKeyReleased(KeyEvent event) throws SQLException {
       
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
             
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
            if(txfEtsiAsikasID.getText().equals("")){
                clearTableviewAsiakas();
                updateTableviewAsiakas();
            }
            else{
                clearTableviewAsiakas();
                Statement stmt = conn.createStatement();
                ResultSet rset = stmt.executeQuery(
               "SELECT * FROM asiakas where asiakasID LIKE '%"+txfEtsiAsikasID.getText()+"%'");
                    
                  while(rset.next()){
                      Asiakas person = new Asiakas(rset.getInt("asiakasID"), rset.getString("etunimi"), rset.getString("sukunimi"), rset.getString("yritys"));
                             
                        tableAsiakas.getItems().add(person);
                        
                        colId.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
                        colEtunimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
                        colSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
                        colYritys.setCellValueFactory(new PropertyValueFactory<>("yritys"));
            }
           
            vuokratoimistoDatabase.closeConnection(conn);
                     
            }
    }
    
}
