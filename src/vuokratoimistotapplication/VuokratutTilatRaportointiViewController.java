package vuokratoimistotapplication;

import java.io.File;
import java.io.IOException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import vuokratoimistotDatabase.vuokratoimistoDatabase;
import vuokratoimistotapplication.Luokat.VuokrattutTilatTaulu;
import static vuokratoimistotapplication.PaavalikkoViewController.closeConnection;

/**
 * FXML Controller class
 * Vuokratut Tilat Raportointi hallinta
 * @author Matti Hänninen
 * @author Hoang Tran
 * @since JDK1.3
 */
public class VuokratutTilatRaportointiViewController implements Initializable {
    @FXML
    private ComboBox<Integer> comboBoxToimipiste;
    @FXML
    private MenuItem menuItemClose;
    @FXML
    private DatePicker datePickerAloitusPaiva;
    @FXML
    private DatePicker datePickerLopetusPaiva;
    @FXML
    private Button btEtsi;
    @FXML
    private VBox txfHakuTulos;
    @FXML
    private TableView<VuokrattutTilatTaulu> tbvVuokratutTaulut;
    @FXML
    private TableColumn<VuokrattutTilatTaulu, String> colToimipste;
    @FXML
    private TableColumn<VuokrattutTilatTaulu, String> colYritys;
    @FXML
    private TableColumn<VuokrattutTilatTaulu, Date> colAloitusPvm;
    @FXML
    private TableColumn<VuokrattutTilatTaulu, Date> colLopetusPvm;
    @FXML
    private TableColumn<VuokrattutTilatTaulu, String> colEtunimi;
    @FXML
    private TableColumn<VuokrattutTilatTaulu, String> colSukunimi;
    @FXML
    private TableColumn<VuokrattutTilatTaulu, Integer> colSumma;
    @FXML
    private WebView webView;
    @FXML
    private MenuItem menuItemTemplete;
    @FXML
    private MenuItem menuItemReport;
  
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    Statement st;
    
    //HTML otsiikon tiedot
    private static final String TOIMIPISTE = "__TOIMIPISTE__";
    private static final String PVM = "__DATE__";
    private static final String ALOITUSPVM = "__ALOITUSPVM__";
    private static final String LOPETUSPVM = "__LOPETUSPVM__";
    
    private static final String TABLE_CONTENT = "__TABLE__";
  
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
        } catch (SQLException e) {
            System.out.println("\t>> Yhteys epäonistu");
         }
    }
    
     /**
     * Sulje vuokratut tilat raportointi hallinta
     * @param event Sulje varausten hallinta 
     * @throws ClassNotFoundException Jos ei löyty vuokratoimistoDatabase luokka
     */
    @FXML
    private void menuItemCloseClicked(ActionEvent event) throws ClassNotFoundException {       
        try {
            //Avataan tietokantayhteys
            Connect();
            closeConnection(conn);
        }
        catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PaavalikkoViewController.class.getName()).log(Level.SEVERE, null, ex);
        }     
        // Suljetaan ikkkuna    
        Stage stage = (Stage) tbvVuokratutTaulut.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Täytetään toimipiste comboBox:ille
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
     private void fillComboBoxToimipiste() throws ClassNotFoundException, SQLException {
        try{ 
            //Avataan tietokantayhteys
            Connect();
            // Toimipsite tiedot tietokannasta
            rs = vuokratoimistoDatabase.selectToimipiste(conn);
            while (rs.next()) {            
                comboBoxToimipiste.getItems().addAll(rs.getInt(1));
            } 
            closeConnection(conn);
           }catch (SQLException ex) {
            java.util.logging.Logger.getLogger(LisapalvelutLaitteetRaportointiViewController.class.getName()).log(Level.SEVERE, null, ex);
           }
    }

     /**
     * Luodaan VuokrattutTilat tietokannasta observable lista
     * @return vuokrattutTilatList ArrayLista
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    public ObservableList<VuokrattutTilatTaulu> getVuokrattutTilatList() throws ClassNotFoundException, SQLException {
        ObservableList<VuokrattutTilatTaulu> vuokrattutTilatList = FXCollections.observableArrayList();
        //Avataan tietokantayhteys
        Connect();
        //Näytetään VuokrattutTilatTaulu sql kysely
        String sql = "SELECT toimipiste.toimipisteNimi, asiakas.yritys, asiakas.etunimi,asiakas.sukunimi, varaus.asiakasID, varaus.aloitusPaiva, varaus.lopetusPaiva, lasku.summa"
                        + " FROM varaus, toimipiste, asiakas,lasku"
                        + " WHERE varaus.toimipisteID = toimipiste.toimipisteID AND varaus.asiakasID = asiakas.asiakasID AND lasku.asiakasID = asiakas.asiakasID"
                ; 
        try {
            st = conn.createStatement();
            //Suoritetaan sql kysely
            rs = st.executeQuery(sql);
            while (rs.next()){
                //Näytetään vuokrattut tilat tulosjouko
                VuokrattutTilatTaulu vuokrattut = new VuokrattutTilatTaulu();
                vuokrattut.setToimipisteNimi(rs.getString("toimipisteNimi"));
                vuokrattut.setYritys(rs.getString("yritys"));
                vuokrattut.setEtunimi(rs.getString("etunimi"));
                vuokrattut.setSukunimi(rs.getString("sukunimi"));
                vuokrattut.setAloitusPaiva(rs.getDate("aloitusPaiva"));
                vuokrattut.setLopetusPaiva(rs.getDate("lopetusPaiva"));
                vuokrattut.setSumma(rs.getInt("summa"));
                //lisätään vuokrattut tilat tulosjoukosta ArrayListille
                vuokrattutTilatList.add(vuokrattut);
                closeConnection(conn);
            }
        } catch (SQLException e){
            }
             return vuokrattutTilatList;
    }
    
     /**
     * Näytetään vuokrattutTilat tiedot taululle
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */
    public void showVarattu() throws ClassNotFoundException, SQLException{
        ObservableList<VuokrattutTilatTaulu> Tilattulist =  getVuokrattutTilatList();
        //Asennaan VuokrattutTilatTaulu ArrayListasta
        tbvVuokratutTaulut.setItems(Tilattulist);
        closeConnection(conn);
    }
    
    /** Etsi ostettujen lisäpalvelujen ja vuokrattujen
     * aikajaksolla valituissa toimipisteissä
     * @param event poista button klikkaus
     * @throws ClassNotFoundException Luokka ei löyty
     * @throws SQLException Tietokantavirhe
     */ 
    @FXML        
    private void btEtsiClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        //Avataan tietokantayhteys
        Connect();
        //Luoda vuokrattutTilat lista
        ObservableList<VuokrattutTilatTaulu> HakuList = FXCollections.observableArrayList();
        //Jos kenttä on tyhjä
        if(comboBoxToimipiste.getValue().equals("") && datePickerAloitusPaiva.getValue().equals("") && datePickerLopetusPaiva.getValue().equals("")){
                showVarattu();   
        } 
        else {
            //Etsi sql kysely
            pst = conn.prepareStatement(
                    "SELECT toimipiste.toimipisteNimi, asiakas.yritys, asiakas.etunimi, asiakas.sukunimi, varaus.asiakasID, varaus.aloitusPaiva, varaus.lopetusPaiva, lasku.summa "
                        + " FROM varaus, toimipiste, asiakas, lasku "
                        + " WHERE varaus.toimipisteID = toimipiste.toimipisteID AND varaus.asiakasID = asiakas.asiakasID AND lasku.asiakasID = asiakas.asiakasID "
                        + " AND toimipiste.toimipisteID = ?"
                        + " AND ((aloitusPaiva BETWEEN ? AND ?)" 
                        + " OR (lopetusPaiva BETWEEN ? AND ?))"
                         );
                    pst.setInt(1, comboBoxToimipiste.getValue());
                    pst.setDate(2,java.sql.Date.valueOf(datePickerAloitusPaiva.getValue()));
                    pst.setDate(3,java.sql.Date.valueOf(datePickerLopetusPaiva.getValue()));
                    pst.setDate(4,java.sql.Date.valueOf(datePickerAloitusPaiva.getValue()));
                    pst.setDate(5,java.sql.Date.valueOf(datePickerLopetusPaiva.getValue()));   
                    //Suoritetaan sql kysely
                    rs = pst.executeQuery(); 
            //Näytetaan tulosjouko
            while(rs.next()){
               VuokrattutTilatTaulu vuokrattut = new VuokrattutTilatTaulu();
               vuokrattut.setToimipisteNimi(rs.getString("toimipisteNimi"));
               vuokrattut.setYritys(rs.getString("yritys"));
               vuokrattut.setEtunimi(rs.getString("etunimi"));
               vuokrattut.setSukunimi(rs.getString("sukunimi"));               
               vuokrattut.setAloitusPaiva(rs.getDate("aloitusPaiva"));
               vuokrattut.setLopetusPaiva(rs.getDate("lopetusPaiva"));
               vuokrattut.setSumma(rs.getInt("summa"));
               //lisätään vuokrattut tilat tulosjoukosta ArrayListille
               HakuList.add(vuokrattut);
               //Asennaan VuokrattutTilatTaulu ArrayListasta
               tbvVuokratutTaulut.setItems(HakuList); 
               //Näytetään hälytys jos lista on tyhjä
               }if (HakuList.isEmpty()) {
                   Alert alert = new Alert(Alert.AlertType.INFORMATION);
                   alert.setTitle("Haun tulokset:");
                   alert.setHeaderText("Ei tietoja");
                   alert.setContentText("Ei ole varauksia valitulla aikavälillä.");
                   alert.showAndWait();
            }  
            closeConnection(conn);
        }
    }

    /**
     * Raporttin template valikkokohtassa 
     * @param event raportti template 
     */
    @FXML
    private void menuItemTempleteClicked(ActionEvent event) {
        loadWebPage("./Report/vuokratuttilat.html");
    }
    
    /**
     * Raporttin template valikkokohtassa  valikkokohtassa 
     * @param event varaus report
     * @throws IOException tiedosto virhe
     */
    @FXML
    private void menuItemReportClicked(ActionEvent event) throws IOException {
        // 1. kopioi pohja
        File source = new File("./Report/vuokratuttilat.html");
        File dest = new File("./Report/vuokratuttilatRaporti.html");

        Files.copy(source.toPath(), dest.toPath(), REPLACE_EXISTING);

        // 2. Korvaa pohjassa olevat paikat tiedoilla
        
        String content = new String(Files.readAllBytes(dest.toPath()), UTF_8);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
   
        content = content.replaceAll(TOIMIPISTE,colToimipste.getCellData(0) );
        content = content.replaceAll(PVM, LocalDateTime.now().toString());
        content = content.replaceAll(ALOITUSPVM, datePickerAloitusPaiva.getValue().toString());
        content = content.replaceAll(LOPETUSPVM, datePickerLopetusPaiva.getValue().toString());
        
        //Täytetään talut saraket
        content = content.replaceAll(TABLE_CONTENT, createRow(colToimipste.getCellData(0), colYritys.getCellData(0),colAloitusPvm.getCellData(0), colLopetusPvm.getCellData(0), colSumma.getCellData(0)));

        // 3. Lataa muodostettu lasku
        Files.write(dest.toPath(), content.getBytes(UTF_8));
        
        // 4. Näytä muodostettu lasku
        loadWebPage(dest.toPath().toString());
    }
    /**
     * Muodostetaan laskun yksi rivi html muodossa
     * @param service Toimipiste
     * @param desc Yritys nimi
     * @param aloitusPvm Aloitus päivämäärä
     * @param lopetusPvm Lopetus päivämäärä
     * @param summa Varaus summa
     * @return toimipiste,Yritys nimi, aloitus päivämäärä, lopetus päivämäräsumma
     */
    private String createRow(String service, String desc, Date aloitusPvm, Date lopetusPvm, int summa) {
        return "<tr>" +
                "<td class=\"service\">" +  service + "</td>" + 
                "<td class=\"desc\">" +  desc + "</td>" +
                "<td class=\"desc\">" + aloitusPvm+ "</td>" +
                "<td class=\"desc\">" + lopetusPvm+ "</td>" +
                "<td class=\"total\">" + summa + "€</td>"
                ;
    }
    /**
     * Ladataam html path
     * @param path raportti html
     */
     private void loadWebPage(String path){
        WebEngine engine = webView.getEngine();
        File f = new File(path);
        engine.load(f.toURI().toString());
    }

    
    /**
     * Initializes the controller class.
     * @param url URL
     * @param rb RB
     */
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Vaihta datepicker muoto YYYY-MM-dd
        String pattern = "YYYY-MM-dd";
        StringConverter converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

        datePickerAloitusPaiva.setConverter(converter);
        datePickerLopetusPaiva.setConverter(converter);
        
        //Päivitetaan VuokrattutTilatTaulun sarake
        colToimipste.setCellValueFactory(new PropertyValueFactory<>("toimipisteNimi"));
        colYritys.setCellValueFactory(new PropertyValueFactory<>("yritys"));
        colEtunimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
        colSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        colAloitusPvm.setCellValueFactory(new PropertyValueFactory<>("aloitusPaiva"));
        colLopetusPvm.setCellValueFactory(new PropertyValueFactory<>("lopetusPaiva"));
        colSumma.setCellValueFactory(new PropertyValueFactory<>("summa"));
        
        try {
            //Avataan tietokantayhteys
            Connect();
            //Näytetään varaus talu
            showVarattu();
            //Tätetään comboBox
            fillComboBoxToimipiste();                   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(VuokratutTilatRaportointiViewController.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
}

       

