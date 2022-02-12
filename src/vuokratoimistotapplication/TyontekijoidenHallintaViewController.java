package vuokratoimistotapplication;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vuokratoimistotapplication.Luokat.Tyontekija;
import static vuokratoimistotapplication.PaavalikkoViewController.closeConnection;
/**
 * Työntekija hallinta
 * FXML Controller class
 * @author Matti Hänninen
 * @author Hoang Tran
 * @since JDK1.3
 */
public class TyontekijoidenHallintaViewController implements Initializable {

    @FXML
    private MenuItem menuClose;
    @FXML
    private TextField txfID;
    @FXML
    private TextField txfEtunimi;
    @FXML
    private TextField txfSukunimi;
    @FXML
    private Button btLisaa;
    @FXML
    private Button btMuokka;
    @FXML
    private Button btPoista;
    @FXML
    private TableView<Tyontekija> tbvTyontekijat;
    @FXML
    private TableColumn<Tyontekija, Integer> colID;
    @FXML
    private TableColumn<Tyontekija, String> colEtunimi;
    @FXML
    private TableColumn<Tyontekija, String> colSukunimi;

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    Statement st;
    
    /**
     * Avataan tietokantayhteys
     * @throws ClassNotFoundException Jos ei löyty vuokratoimistoDatabase luokka
     * @throws SQLException Tietokantavirhe
     */
    public void Connect() throws ClassNotFoundException, SQLException{
        try {
            conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        }catch (SQLException e) {
            //tietokantaan ei saada yhteyttä
            System.out.println("\t>> Yhteys epäonistu");
        }
    }
    /**
     * Sulje työntekija hallinta
     * @param event Sulje työntekija hallinta Action event
     * @throws ClassNotFoundException Jos ei löyty vuokratoimistoDatabase luokka
     */
    @FXML
    private void menuCloseClicked(ActionEvent event) throws ClassNotFoundException {
        try {
            //Avataan tietokantayhteys
            Connect();
            closeConnection(conn);
        }catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PaavalikkoViewController.class.getName()).log(Level.SEVERE, null, ex);
        }     
        // Suljetaan ikkkuna    
        Stage stage = (Stage) tbvTyontekijat.getScene().getWindow();
        stage.close();
    }
    /**
     * Luodaan työntekija tietokannasta observable lista
     * @return tyontekijaList
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    public ObservableList<Tyontekija> getTyontekijaList() throws ClassNotFoundException, SQLException{
        ObservableList<Tyontekija> tyontekijaList = FXCollections.observableArrayList();
        //Avataan tietokantayhteys
        Connect();
        String sql = "SELECT tyontekijaID,etunimi,sukunimi FROM tyontekija"; 
        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()){
               Tyontekija tekijat = new Tyontekija();
               tekijat.setTyontekijaID(rs.getInt("tyontekijaID"));
               tekijat.setEtunimi(rs.getString("etunimi"));
               tekijat.setSukunimi(rs.getString("sukunimi"));
               // Lisätään työntekija tulosjoukosta ArrayListille
               tyontekijaList.add(tekijat);
               //Suljetaan tietokantayhteys
               closeConnection(conn);
            }
        } catch (SQLException e){
        }
        return tyontekijaList;
    } 
    /**
     * Näytetään työntekija tiedot taululle
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    public void showTyontekijat() throws ClassNotFoundException, SQLException{
        ObservableList<Tyontekija> list = getTyontekijaList();
            //Täytetään työntekijan taulun saraket
            colID.setCellValueFactory(new PropertyValueFactory<>("tyontekijaID"));
            colEtunimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
            colSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));           
            tbvTyontekijat.setItems(list);
            vuokratoimistoDatabase.closeConnection(conn);
    }
    /**
     * Lisätään tietokantaan työntekijoita
     * @param event lisaa button action even
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    @FXML
    private void btLisaaClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        //Avataan tietokantayhteys
        Connect();
        int tyontekijaID = Integer.parseInt(txfID.getText());
        String etunimi = txfEtunimi.getText();
        String sukunimi = txfSukunimi.getText();
        if (tyontekijaID < 0 ){
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setTitle ("Ei Onnistu");
                alert.setHeaderText("Työntekija");
                alert.setContentText("Työntekija lisääminen ei onnistui, tarkistaa työntekija numero");
                alert.showAndWait(); 
        } else {
         try {
            //Lisätään työntekijoita sql kysely
            pst = conn.prepareStatement("INSERT INTO tyontekija (tyontekijaID, etunimi, sukunimi) VALUES (?, ?, ?)");
            pst.setInt(1, tyontekijaID);
            pst.setString(2, etunimi);
            pst.setString(3, sukunimi);
            //Suorita sql kysely
            int status = pst.executeUpdate();
            //Onnistu hälytys 
            if (status == 1) {
                Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setTitle ("Onnistu");
                alert.setHeaderText("Työntekija");
                alert.setContentText("Työntekija lisääminen onnistui");
                alert.showAndWait();
                
                //Päivitetään ja näytetään työntekija taulu
                tbvTyontekijat.refresh();
                showTyontekijat();
                
                //Tyhjenetaan textfield
                txfID.setText("");
                txfEtunimi.setText("");
                txfSukunimi.setText("");
                //Näytetaan lisätty tyontekija konsolille
                System.out.println("\t>> Lisätty työntekija " + tyontekijaID);
                //Suljetaan tietokantayhteys
                closeConnection(conn);
            } else {             
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setTitle ("Ei onnistu");
                alert.setHeaderText("Työntekija");
                alert.setContentText("Työntekija lisääminen ei onnistu");
                alert.showAndWait();    
            }           
        }catch(SQLException e) {       
        }
        }
    }
    /**
     * Muutetaan työntekijan tiedot tietokantaan
     * @param event muokka button klikkaus
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    @FXML
    private void btMuokkaClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        //Avataan tietokantayhteys
        Connect();
        int tyontekijaID = Integer.parseInt(txfID.getText());
        String etunimi = txfEtunimi.getText();
        String sukunimi = txfSukunimi.getText(); 
        try { 
            //Muutetaan työntekijoita sql kysely
            pst = conn.prepareStatement("UPDATE tyontekija SET etunimi=?, sukunimi=? WHERE tyontekijaID=?");
            pst.setString(1, etunimi);
            pst.setString(2, sukunimi);
            pst.setInt(3, tyontekijaID);
            //Suorita sql kysely
            int status =  pst.executeUpdate(); 
            if (status == 1) {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setTitle ("Onnistu");
                alert.setHeaderText("Työntekija");
                alert.setContentText("Työntekija muokaaminen onnistu");
                alert.showAndWait();  
            //Päivitetään ja näytetään työntekija taulu
            tbvTyontekijat.refresh();
            showTyontekijat(); 
            //Tyhjenetaan textfield
            txfID.setText("");
            txfEtunimi.setText("");
            txfSukunimi.setText("");
            //Näytetaan muokattu tyontekija konsolille
            System.out.println("\t>> Muokattu työntekija " + tyontekijaID);
            vuokratoimistoDatabase.closeConnection(conn);               
           } else {             
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setTitle ("Ei onnistu");
                alert.setHeaderText("Työntekija");
                alert.setContentText("Työntekija lisääminen ei onnistu");
                alert.showAndWait();             
            }                     
        }catch(SQLException e) { 
            System.out.println("Tietokanta virhe");
            throw e;
        }                
    }
    /**
     * Poistetaan työntekija tietokannasta
     * @param event poista button klikkaus
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     * @throws Exception Muut virheet
     */
    @FXML
    private void btPoistaClicked(ActionEvent event) throws ClassNotFoundException, SQLException, Exception {
        //Avataan tietokantayhteys
        Connect();
        int  tyontekijaID = Integer.parseInt(txfID.getText());
        try {
            //Poistetaan työntekijoita sql kysely
            pst = conn.prepareStatement("DELETE FROM tyontekija WHERE tyontekijaID=?");           
            pst.setInt(1, tyontekijaID);   
            //Suorita sql kysely
            rs = pst.executeQuery();
            //Päivitetään ja näytetään 
            tbvTyontekijat.refresh();
            showTyontekijat(); 
            //Tyhjennetaan textfield
            txfID.setText("");
            txfEtunimi.setText("");
            txfSukunimi.setText("");
            //Poistetaan onnistu hälytys
             Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setTitle ("Onnistu");
                alert.setHeaderText("Työntekija");
                alert.setContentText("Työntekija poistaminen onnistu");
                alert.showAndWait();  
            //Näytetaan poistetty tyontekijaID konsolille
            System.out.println("\t>> Poistu työntekija " + tyontekijaID);    
            vuokratoimistoDatabase.closeConnection(conn);  
            if (rs == null) { 
                throw new Exception ("Työtekija poistaminen ei onnistu");
            }
         } catch (SQLException e) {throw e;}       
    }
    
    /**
     * Initializes the controller class.
     * @param url URL
     * @param rb RB
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Avataan tietokantayhteys
            Connect();
            //Näytetään työntekijat talu
            showTyontekijat();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TyontekijoidenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }         
    }    
}
