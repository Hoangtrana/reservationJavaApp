package vuokratoimistotapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import vuokratoimistotDatabase.vuokratoimistoDatabase;
import vuokratoimistotapplication.Luokat.Varaus;
import static vuokratoimistotapplication.PaavalikkoViewController.closeConnection;

/**
 * FXML Controller class
 * Varausten hallinta
 * @author Matti Hänninen
 * @author Hoang Tran
 * @since JDK1.3
 */
public class VaraustenHallintaViewController implements Initializable {

    @FXML
    private MenuItem menuClose;
    @FXML
    private TableView<Varaus> tbvVaraus;
    @FXML
    private TableColumn<Varaus, Integer> colVarausID;
    @FXML
    private TableColumn<Varaus, Date> colAloitusPaiva;
    @FXML
    private TableColumn<Varaus, Date> colLopetusPaiva;
    @FXML
    private TableColumn<Varaus, Integer> colAsiakasID;
    @FXML
    private TableColumn<Varaus, Integer> colToimipisteID;
    @FXML
    private TextField txfAsiakasID;
    @FXML
    private TextField txfToimipisteID;
    @FXML
    private Button btLisaa;
    @FXML
    private Button btMuokka;
    @FXML
    private Button btPoista;
    @FXML
    private TextField txfVarausID;
    @FXML
    private DatePicker DatePickerAloitusPaiva;
    @FXML
    private DatePicker DatePickerLopetusPaiva;
    @FXML
    private TextField txfHakuVarausID;
    
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
        //System.out.println("\t>> Yhteys ok");
        } catch (SQLException e) {
            System.out.println("\t>> Yhteys epäonistu");
         }
    }
    
    /**
     * Sulje varausten hallinta
     * @param event Sulje varausten hallinta 
     * @throws ClassNotFoundException Jos ei löyty vuokratoimistoDatabase luokka
     */
    @FXML
    private void menuClosedClicked(ActionEvent event) throws ClassNotFoundException {
        try {
            //Avataan tietokantayhteys
            Connect();
            //Suljetaan tietokantayhteys
            closeConnection(conn);
        }
        catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PaavalikkoViewController.class.getName()).log(Level.SEVERE, null, ex);
        }     
        // Suljetaan ikkkuna    
        Stage stage = (Stage) tbvVaraus.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Luodaan varausten tietokannasta observable lista
     * @return varausList
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    public ObservableList<Varaus> getVarausList() throws ClassNotFoundException, SQLException{
        ObservableList<Varaus> varausList = FXCollections.observableArrayList();
        //Avataan tietokantayhteys
        Connect();
        //Näytetaan kaikki varausten tiedot sql kysely
        String sql = "SELECT varausID, aloitusPaiva, lopetusPaiva, asiakasID, toimipisteID FROM varaus"; 
        try {
            st = conn.createStatement();
            //Suoritetaan sql kysely
            rs = st.executeQuery(sql);
            while (rs.next()){
               Varaus varaukset = new Varaus();
               varaukset.setVarausID(rs.getInt("varausID"));
               varaukset.setAloitusPaiva(rs.getDate("aloitusPaiva"));
               varaukset.setLopetusPaiva(rs.getDate("lopetusPaiva"));
               varaukset.setAsiakasID(rs.getInt("asiakasID"));
               varaukset.setToimipisteID(rs.getInt("toimipisteID"));
               //Lisätään varausten tulosjoukosta ArrayListille
               varausList.add(varaukset);
               closeConnection(conn);
            }
        } catch (SQLException e){
        }
        return varausList;
    }
    
    /**
     * Näytetään varaus tiedot taululle
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    public void showVaraus() throws ClassNotFoundException, SQLException{
        ObservableList<Varaus> list = getVarausList();
            //Täytetään varausten taulun saraket
            colVarausID.setCellValueFactory(new PropertyValueFactory<>("varausID"));
            colAloitusPaiva.setCellValueFactory(new PropertyValueFactory<>("aloitusPaiva"));
            colLopetusPaiva.setCellValueFactory(new PropertyValueFactory<>("lopetusPaiva"));
            colAsiakasID.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
            colToimipisteID.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));
            //Lisätään varausten ArrayLista taululle
            tbvVaraus.setItems(list);
            closeConnection(conn);
    }
    
    /**
     * Lisätään tietokantaan varausta
     * @param event lisaa button action even
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    @FXML
    private void btLisaaClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        //Avataan tietokantayhteys
        Connect();
        
         try {
             int varausID = Integer.parseInt(txfVarausID.getText());
            Date aloitusPaiva = java.sql.Date.valueOf(DatePickerAloitusPaiva.getValue());
            Date lopetusPaiva = java.sql.Date.valueOf(DatePickerLopetusPaiva.getValue());
            int asiakasID = Integer.parseInt(txfAsiakasID.getText());
            int toimipisteID = Integer.parseInt(txfToimipisteID.getText());
            //Lisätään varausta sql kysely
            pst = conn.prepareStatement(
                    "INSERT INTO varaus (varausID, aloitusPaiva, lopetusPaiva, asiakasID, toimipisteID) "
                    + "VALUES (?, ?, ?, ?, ?)"
                    );
            pst.setInt(1, varausID);
            pst.setDate(2, aloitusPaiva);
            pst.setDate(3, lopetusPaiva);
            pst.setInt(4, asiakasID);
            pst.setInt(5, toimipisteID);
            //Suoritetaan sql kysely
            int status = pst.executeUpdate();
            //Lisää onnistu hälytys
            if (status == 1) {
                Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setTitle ("Onnistu");
                alert.setHeaderText("Varaus");
                alert.setContentText("Varaus lisääminen onnistui");
                alert.showAndWait();
                
                //Päivitetään ja näytetään työntekija taulu
                tbvVaraus.refresh();
                showVaraus();
                
                //Tyhjenetaan textfield
                txfVarausID.setText("");     
                txfAsiakasID.setText("");
                txfToimipisteID.setText("");
                DatePickerAloitusPaiva.setValue(null);
                DatePickerLopetusPaiva.setValue(null);
                
                //Näytetaan lisätty varaus konsolille
                System.out.println("\t>> Lisätty varaus " + varausID);
                //Suljetaan tietokantayhteys
                closeConnection(conn);
            } else {             
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setTitle ("Ei onnistu");
                alert.setHeaderText("Varaus");
                alert.setContentText("Varaus lisääminen ei onnistu");
                alert.showAndWait();             
            }           
        }catch(SQLException e) {    
             Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setTitle ("Ei onnistu");
                alert.setHeaderText("Varaus");
                alert.setContentText("Varaus lisääminen ei onnistu");
                alert.showAndWait();  
        }
    }
    
   /**
     * Muutetaan varausten tiedot tietokantaan
     * @param event muokka button klikkaus
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    @FXML
    private void btMuokkaClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        //Avataan tietokantayhteys
        Connect();
        int varausID = Integer.parseInt(txfVarausID.getText());
        Date aloitusPaiva = java.sql.Date.valueOf(DatePickerAloitusPaiva.getValue());
        Date lopetusPaiva = java.sql.Date.valueOf(DatePickerLopetusPaiva.getValue());
        int asiakasID = Integer.parseInt(txfAsiakasID.getText());
        int toimipisteID = Integer.parseInt(txfToimipisteID.getText());        
        try {
            //Muutetaan varausten sql kysely
            pst = conn.prepareStatement("UPDATE varaus SET aloitusPaiva = ?, "
                    + "lopetusPaiva = ?, asiakasID = ?, toimipisteID = ? WHERE varausID = ?");
            pst.setDate(1, aloitusPaiva);
            pst.setDate(2, lopetusPaiva);
            pst.setInt(3, asiakasID);
            pst.setInt(4, toimipisteID);
            pst.setInt(5, varausID);
            //Suoritetaan sql kysely
            int status =  pst.executeUpdate(); 
            if (status == 1) {
            //Näytetään onnistu hälytys
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setTitle ("Onnistu");
                alert.setHeaderText("Varaus");
                alert.setContentText("Varaus muokaaminen onnistu");
                alert.showAndWait();  
                //Päivitetään ja näytetään työntekija taulu
                tbvVaraus.refresh();
                showVaraus();
                //Tyhjenetaan textfield
                txfVarausID.setText("");
                DatePickerAloitusPaiva.setValue(null);
                DatePickerLopetusPaiva.setValue(null);
                txfAsiakasID.setText("");
                txfToimipisteID.setText("");
                //Näytetaan muokattu varaus konsolille
                System.out.println("\t>> Muokattu varaus " + varausID);
                //Suljetaan tietokantayhteys
                closeConnection(conn);     
           } else {             
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setTitle ("Ei onnistu");
                alert.setHeaderText("Varaus");
                alert.setContentText("Varaus muokaaminen ei onnistu");
                alert.showAndWait();      
            }            
        }catch(SQLException e) {   
            throw e;
        }
    }
    
    /**
     * Poistetaan varausten tietokannasta
     * @param event poista button klikkaus
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     * @throws Exception Muut virheet
     */
    @FXML
    private void btPoistaClicked(ActionEvent event) throws ClassNotFoundException, Exception {
        //Avataan tietokantayhteys
        Connect();
        int  varausID = Integer.parseInt(txfVarausID.getText());        
        try {
            //Poistetaan varausta sql kysely
            pst = conn.prepareStatement("DELETE FROM varaus WHERE varausID=?");           
            pst.setInt(1, varausID);   
            //Suoritetaan sql kysely
            rs = pst.executeQuery();
            //Päivitetään ja näytetään 
            tbvVaraus.refresh();
            showVaraus();
            //Tyhjennetaan textfield    
            txfVarausID.setText("");
            DatePickerAloitusPaiva.setValue(null);
            DatePickerLopetusPaiva.setValue(null);
            txfAsiakasID.setText("");
            txfToimipisteID.setText("");
            //Poistetaan onnistu hälytys
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
                 alert.setTitle("Onnistu");
                alert.setHeaderText("Varaus");
                alert.setContentText("Varaus poistaminen onnistu");
                alert.showAndWait(); 
            //Näytetaan muokattu varaus konsolille
            System.out.println("\t>> Poistu varaus " + varausID); 
            //Suljetaan tietokantayhteys
            closeConnection(conn);  
 
            if (rs == null) { 
                throw new Exception ("Varaus poistaminen ei onnistu");
            }
        } catch (SQLException e) {
                throw e;
            }      
    }

    /** Haku varausID:lla, asiakasID:lla, toimipisteID:lla
     * @param event poista button klikkaus
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */ 
    @FXML
    private void hakuVarausLista(KeyEvent event) throws ClassNotFoundException, SQLException{
        //Avataan tietokantayhteys
        Connect();         
        //Luoda varaus lista
         ObservableList<Varaus> List = FXCollections.observableArrayList();
         if(txfHakuVarausID.getText().equals("")){
                showVaraus();       
         } else {
            //Luodaan haku sql kysely
            pst = conn.prepareStatement(
                    " SELECT * FROM varaus WHERE varausID LIKE '%" + txfHakuVarausID.getText()+ "%'"    
                    + "UNION SELECT * FROM varaus WHERE asiakasID LIKE '%" + txfHakuVarausID.getText()+ "%' "
                    + "UNION SELECT * FROM varaus WHERE toimipisteID LIKE '%" + txfHakuVarausID.getText()+ "%' "
            );
            //Suoritetaan sql kysely
            rs = pst.executeQuery();
            //Halu tulos joukko
            while(rs.next()){
                Varaus varaukset = new Varaus();
                varaukset.setVarausID(rs.getInt("varausID"));
                varaukset.setAloitusPaiva(rs.getDate("aloitusPaiva"));
                varaukset.setLopetusPaiva(rs.getDate("lopetusPaiva"));
                varaukset.setAsiakasID(rs.getInt("asiakasID"));
                varaukset.setToimipisteID(rs.getInt("toimipisteID"));
                //Lisätään varausta ArrayListalle
                List.add(varaukset);
                //Päivitetaan taulun saraket
                colVarausID.setCellValueFactory(new PropertyValueFactory<>("varausID"));
                colAloitusPaiva.setCellValueFactory(new PropertyValueFactory<>("aloitusPaiva"));
                colLopetusPaiva.setCellValueFactory(new PropertyValueFactory<>("lopetusPaiva"));
                colAsiakasID.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
                colToimipisteID.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));
                //Päivitetaan taulun
                tbvVaraus.setItems(List);
                //Suljetaan tietokantayhteys
                closeConnection(conn);   
            }  
        }           
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
            //Näytetään varaus talu
            showVaraus();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(VaraustenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
