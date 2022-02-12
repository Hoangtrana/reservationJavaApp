/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication;

import java.io.File;
import java.io.IOException;
import vuokratoimistotapplication.Luokat.Lasku;
import vuokratoimistotDatabase.vuokratoimistoDatabase;

import java.net.URL;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Janne
 * @author Matty
 */
public class LaskutusHallintaViewController implements Initializable {

    @FXML
    private TextField txfLaskuID; /**TextField txfLaskuID*/
    @FXML
    private TextField txfAsiakasID; /**TextField txfAsiakasID*/
    @FXML
    private TextField txfErapaiva; /**TextField txfErapaiva*/
    @FXML
    private TextField txfMaksupaiva; /**TextField txfMaksupaiva*/
    @FXML
    private TextField txfSumma; /**TextField txfSumma*/
    @FXML
    private TextField txfMaksettu; /**TextField txfMaksettu*/
    @FXML
    private TextField txfLaskutusTyyppi; /**TextField txfLaskutusTyyppi*/
    @FXML
    private TableColumn<Lasku, Integer> colLaskuID; /**TableColumn colLaskuID*/
    @FXML
    private TableColumn<Lasku, Integer> colAsiakasID; /**TableColumn colAsiakasID*/
    @FXML
    private TableColumn<Lasku, Date> colErapaiva; /**TableColumn colErapaiva*/
    @FXML
    private TableColumn<Lasku, Date> colMaksupaiva; /**TableColumn colMaksupaiva*/
    @FXML
    private TableColumn<Lasku, Integer> colSumma; /**TableColumn colSumma*/
    @FXML
    private TableColumn<Lasku, Integer> colMaksettu; /**TableColumn colMaksettu*/
    @FXML
    private TableColumn<Lasku, String> colLaskutustyyppi; /**TableColumn colLaskutustyyppi */
    @FXML
    private Button btnLisaa; /**Button btnLisaa*/
    @FXML
    private Button btnMuokkaa; /**Button btnMuokkaa*/
    @FXML
    private Button btnPoista; /**Button btnPoista*/
    @FXML
    private TableView<Lasku> tableLasku; /**TableView tableLasku*/
    @FXML
    private TextField txfEtsiLaskunID; /**TextField txfEtsiLaskunID*/
    @FXML
    private TextField txfEmail; /**TextField txfEmail*/
    @FXML
    private TextField txfOsoite; /**TextField txfOsoite*/
    @FXML
    private TextField txfToimipiste; /**TextField txfToimipiste*/
    @FXML
    private TextField txfPalvelu; /**TextField txfPalvelu*/
    @FXML
    private Button btnTeeLasku; /**Button btnTeeLasku*/
    @FXML
    private Button btnMallilasku; /**Button btnMallilasku*/
    @FXML
    private WebView webView; /**WebView webView*/
    
    private static final String CUSTOMER_NAME = "__CUST_NAME__"; /**String CUSTOMER_NAME*/
    private static final String CUSTOMER_ADDRESS = "__CUST_ADDR__"; /**String CUSTOMER_ADDRESS*/
    private static final String CUSTOMER_EMAIL = "__CUST_EMAIL__"; /**String CUSTOMER_EMAIL*/
    private static final String INVOICE_DATE = "__DATE__"; /**String INVOICE_DATE*/
    private static final String INVOICE_DUE_DATE = "__DUE_DATE__"; /**String INVOICE_DUE_DATE*/
    private static final String TABLE_CONTENT = "__TABLE__"; /**String TABLE_CONTENT*/
    private static final String CUSTOMER_VIITENUMERO = "__CUST_VIITENUMERO__"; /**String CUSTOMER_VIITENUMERO*/

    /**
     * Initializes the controller class.
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        updateTableviewLasku();
        
        txfLaskuID.setFocusTraversable(false);
        txfAsiakasID.setFocusTraversable(false);
        txfErapaiva.setFocusTraversable(false);
        txfMaksupaiva.setFocusTraversable(false);
        txfSumma.setFocusTraversable(false);
        txfMaksettu.setFocusTraversable(false);
        txfLaskutusTyyppi.setFocusTraversable(false);
    }    
    

    /**
     *tableview syöttää tauluun tiedot
     */
    public void updateTableviewLasku(){
        
        colLaskuID.setCellValueFactory(new PropertyValueFactory<>("laskuID"));
        colAsiakasID.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
        colErapaiva.setCellValueFactory(new PropertyValueFactory<>("erapaiva"));
        colMaksupaiva.setCellValueFactory(new PropertyValueFactory<>("maksupaiva"));
        colSumma.setCellValueFactory(new PropertyValueFactory<>("summa"));
        colMaksettu.setCellValueFactory(new PropertyValueFactory<>("maksettu"));
        colLaskutustyyppi.setCellValueFactory(new PropertyValueFactory<>("laskutusTyyppi"));
        
        try {
          
            Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
             
            vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
            
            ResultSet namesResult = selectLasku(conn);
            
            while (namesResult.next()) {
                Lasku person = new Lasku(namesResult.getInt("laskuID"), namesResult.getInt("asiakasID"), namesResult.getDate("erapaiva"),
                        namesResult.getDate("maksupaiva"), namesResult.getInt("summa"), namesResult.getInt("maksettu"), namesResult.getString("laskutusTyyppi"));
                tableLasku.getItems().add(person);
            }
            
            vuokratoimistoDatabase.closeConnection(conn);
            
        } catch (SQLException ex) {
            Logger.getLogger(AsiakkaidenHallintaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /** Tyhjennetään asiakas tableview*/
    public void clearTableviewLasku(){
        for (int i=0;i<tableLasku.getItems().size();i++){
            tableLasku.getItems().clear();
        }
    }
    
    /**
     *Poistaa laskun tiedot tietokannasta poista buttonia klikkaamalla
     * @param c yhteys tietokantaan
     * @param laskuID poistettavan laskun yksilöivä setteri laskuID
     * @throws SQLException virhe
     */
    public static void deleteLasku(Connection c, int laskuID) throws SQLException {
        PreparedStatement ps = c.prepareStatement( 
        ("DELETE FROM lasku WHERE laskuID=?")
                
    );
    
        ps.setInt(1, laskuID);
    
        ps.execute();
        
        System.out.println("\t>> poistettu opiskelija_id " + laskuID);
   
    }
    
    /**
     *muokataan opiskelijaa klikkaamalla muokkaa buttonia
     * @param c yhteys
     * @param laskuID laskunID
     * @param asiakasID asiakasID
     * @param erapaiva laskun eräpäivä
     * @param maksupaiva laskun maksupäivän
     * @param summa laskun summa
     * @param maksettu laskusta on maksttu
     * @param laskutusTyyppi millainen lasku email-ja/tai paperilasku
     * @throws SQLException virhe
     */
    public static void editLasku(Connection c, int laskuID, int asiakasID, String erapaiva, String maksupaiva,
            int summa, int maksettu, String laskutusTyyppi) throws SQLException {
        
         PreparedStatement ps = c.prepareStatement(
        ("UPDATE lasku SET asiakasID=?, erapaiva=STR_TO_DATE(?, '%d.%m.%Y'), maksupaiva=STR_TO_DATE(?, '%d.%m.%Y'), summa=?, maksettu=?, laskutusTyyppi=? WHERE laskuID=?")
        );
   
        ps.setInt(1,asiakasID);
        ps.setString(2,erapaiva);
        ps.setString(3,maksupaiva);
        ps.setInt(4,summa);
        ps.setInt(5,maksettu);
        ps.setString(6,laskutusTyyppi);
        ps.setInt(7,laskuID); 
   
       try {
        ps.execute();
        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Laskun tietojen muokkaaminen");
            alert.setHeaderText("Virhe");
            alert.setContentText("Laskun muokkaaminen epäonnistui");
            alert.showAndWait();
        }
    
        System.out.println("\t>> Päivitetty laskuID tiedot: " + laskuID);
        
    }
    
    /**
     *valitsee asiakkaiden tiedot tableview:n täyttämistä varten
     * @param c yhteys tietokantaan
     * @return rs
     * @throws SQLException virhe
     */
     public static ResultSet selectLasku(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT laskuID, asiakasID, erapaiva, maksupaiva, summa, maksettu, laskutusTyyppi FROM lasku ORDER BY laskuID"
        );
        
        return rs;
        
    }

    /**
     * lisää laskun tietokantaan
     * @param event klikataan btn lisää
     * @throws SQLException virhe
     */
    @FXML
    private void btnLisaaClicked(ActionEvent event) throws SQLException {
       
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
             
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
        vuokratoimistoDatabase.addLasku(conn, Integer.parseInt(txfLaskuID.getText()), Integer.parseInt(txfAsiakasID.getText()), txfErapaiva.getText(),
                txfMaksupaiva.getText(), Integer.parseInt(txfSumma.getText()), Integer.parseInt(txfMaksettu.getText()), txfLaskutusTyyppi.getText());
                                 
        clearTableviewLasku();
        
        updateTableviewLasku();
        
        vuokratoimistoDatabase.closeConnection(conn);
    }

    /**
     * Muokkaa laskun tietoa klikkaamalla buttonia
     * @param event btn muokkaa
     * @throws SQLException virhe
     */
    @FXML
    private void btnMuokkaaClicked(ActionEvent event) throws SQLException {
   
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
               
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
        editLasku(conn, Integer.parseInt(txfLaskuID.getText()), Integer.parseInt(txfAsiakasID.getText()), txfErapaiva.getText(),
                txfMaksupaiva.getText(), Integer.parseInt(txfSumma.getText()), Integer.parseInt(txfMaksettu.getText()), txfLaskutusTyyppi.getText());
        
        clearTableviewLasku();
        
        updateTableviewLasku();
        
        vuokratoimistoDatabase.closeConnection(conn);
    }
    
    /**
     * poistaa laskun tiedot tietokannasta, kun klikataan btn poista
     * @param event klikkaa hiirellä btn poista
     * @throws SQLException virhe
     */
    @FXML
    private void btnPoistaClicked(ActionEvent event) throws SQLException {
        
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
               
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
        deleteLasku(conn, Integer.parseInt(txfLaskuID.getText()));
        
        clearTableviewLasku();
        
        updateTableviewLasku();
        
        vuokratoimistoDatabase.closeConnection(conn);
      
    }

    /**
     * TableViewta klikatessa hiirellä kopioituvat tiedot textfieldeihin
     * @param event Lasku-taulukkoa klikataan
     * @throws SQLException 
     */
    @FXML
    private void tblLaskuClickedFillTextfield(MouseEvent event) throws SQLException {
        
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
             
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
   
        Lasku user = tableLasku.getSelectionModel().getSelectedItem();
          
        PreparedStatement pst = conn.prepareStatement(
        ("SELECT * from lasku where laskuID =?")
        );
        
        pst.setInt(1, user.getLaskuID());
        ResultSet rs = pst.executeQuery();
           
           while(rs.next()){
               txfLaskuID.setText(rs.getString("laskuID"));
               txfAsiakasID.setText(rs.getString("asiakasID"));
               txfErapaiva.setText(rs.getString("erapaiva"));
              // txfErapaiva.setText(rs.getString("erapaiva")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
               txfMaksupaiva.setText(rs.getString("maksupaiva"));
               txfSumma.setText(rs.getString("summa"));
               txfMaksettu.setText(rs.getString("maksettu"));
               txfLaskutusTyyppi.setText(rs.getString("laskutusTyyppi"));
           }

        vuokratoimistoDatabase.closeConnection(conn);
    }

    /**
     * etsii laskun tiedot tableviewsta laskuID perusteella
     * @param event näppäimistön painike vapautetaan
     * @throws SQLException virhe
     */
    @FXML
    private void etsiLaskunidKeyReleased(KeyEvent event) throws SQLException {
        
        Connection conn = vuokratoimistoDatabase.openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");
             
        vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
            if(txfEtsiLaskunID.getText().equals("")){
                clearTableviewLasku();
                updateTableviewLasku();
            }
            else{
                clearTableviewLasku();
                Statement stmt = conn.createStatement();
                ResultSet rset = stmt.executeQuery(
               "SELECT * FROM lasku where laskuID LIKE '%"+txfEtsiLaskunID.getText()+"%'");
                    
                  while(rset.next()){
                      Lasku person = new Lasku(rset.getInt("laskuID"), rset.getInt("asiakasID"), rset.getDate("erapaiva"), rset.getDate("maksupaiva"),
                              rset.getInt("summa"), rset.getInt("maksettu"),rset.getString("laskutusTyyppi"));
                        tableLasku.getItems().add(person);
                        
                        colLaskuID.setCellValueFactory(new PropertyValueFactory<>("laskuID"));
                        colAsiakasID.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
                        colErapaiva.setCellValueFactory(new PropertyValueFactory<>("erapaiva"));
                        colMaksupaiva.setCellValueFactory(new PropertyValueFactory<>("maksupaiva"));
                        colSumma.setCellValueFactory(new PropertyValueFactory<>("summa"));
                        colMaksettu.setCellValueFactory(new PropertyValueFactory<>("maksettu"));
                        colLaskutustyyppi.setCellValueFactory(new PropertyValueFactory<>("laskutusTyyppi"));
            }
           
            vuokratoimistoDatabase.closeConnection(conn);
                     
            }
    }
    
    /**
     * sulkee lasku ikkunan
     * @param event klikataan hiirellä file
     */
    @FXML
    private void closeLaskuWindow(ActionEvent event) {
        Stage stage = (Stage) tableLasku.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Tyhjentää textfieldin tiedot, jotka otettu lasku tableviewstä
     * @param event klikataan hiirellä edit menubaria
     */
    @FXML
    private void editClearTextfield(ActionEvent event) {
        txfLaskuID.setText("");
        txfAsiakasID.setText("");
        txfErapaiva.setText("");
        txfMaksupaiva.setText("");
        txfSumma.setText("");
        txfMaksettu.setText("");
        txfLaskutusTyyppi.setText("");
    }

    /**
     * tekee laskun 
     * @param event klikataan hiirellä tee lasku buttonia
     * @throws IOException virhe
     */
    @FXML
    private void btnTeeLaskuClicked(ActionEvent event) throws IOException {
        
        File source = new File("./src/vuokratoimistotapplication/laskujenHTML/mallilasku.html");
        File dest = new File("./src/vuokratoimistotapplication/laskujenHTML/teeLasku.html");
        Files.copy(source.toPath(), dest.toPath(), REPLACE_EXISTING);

        String content = new String(Files.readAllBytes(dest.toPath()), UTF_8);
       
        content = content.replaceAll(CUSTOMER_NAME, txfAsiakasID.getText());
      
        content = content.replaceAll(INVOICE_DUE_DATE, txfErapaiva.getText());
        
        content = content.replaceAll(CUSTOMER_EMAIL, txfEmail.getText());
        
        content = content.replaceAll(CUSTOMER_ADDRESS, txfOsoite.getText());
        
        content = content.replaceAll(INVOICE_DATE, txfMaksupaiva.getText());
        
        content = content.replaceAll(CUSTOMER_VIITENUMERO, txfLaskuID.getText());
        
        content = content.replaceAll(TABLE_CONTENT, createRow(txfToimipiste.getText(), txfPalvelu.getText(), txfSumma.getText() ));
        
        Files.write(dest.toPath(), content.getBytes(UTF_8));
        
        loadWebPage(dest.toPath().toString());
    }
    
    /**
     * tekee rivin html-laskuun
     * @param service toimintapiste
     * @param desc palvelu
     * @param total summa
     * @return service desc total
     */
    private String createRow(String service, String desc, String total) {
         return "<tr>" +
                "<td class=\"service\">" + service + "</td>" + 
                "<td class=\"desc\">" + desc + "</td>" +
                "<td class=\"total\">" + total + "€</td>"
                ;
    }
    

    /**
     * Näyttää mallilaskun
     * @param event klikataan mallilasku buttonia
     */
    @FXML
    private void btnMallilaskuClicked(ActionEvent event) {
        loadWebPage("./src/vuokratoimistotapplication/laskujenHTML/mallilasku.html");
    }
    
    /**
     * lataa webpagen
     * @param path string missä html-tiedosto on
     */
    private void loadWebPage(String path){
        WebEngine engine = webView.getEngine();
        File f = new File(path);
        engine.load(f.toURI().toString());
    }
    
}
