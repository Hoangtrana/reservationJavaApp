
package vuokratoimistotDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;


/**
 * Vuokratoimiston tietokanta sql luominen 
 * @author Hoang Tran
 * @author Matti Hänninen
 * @author Jane Peiponen
 * @since JDK1.3
 * Date 12.4.2021
 */
public class vuokratoimistoDatabase {
    
    /**
     * Avataan tietokanta yhteys
     * @param connString jdbc:mariadb URL
     * @return con tietokanta yhteys 
     * @throws SQLException Tietokantavirhe
     */
    public static Connection openConnection(String connString) throws SQLException {
        Connection con = DriverManager.getConnection(connString);
        System.out.println("\t>> Yhteys ok");
        return con;
    }
    
    /**
     * Suljetaan tietokantayhteys
     * @param c Connection
     * @throws SQLException Tietokantavirhe
     */
    public static void closeConnection(Connection c) throws SQLException {
        if (c != null) {
            c.close();
        }
        System.out.println("\t>> Tietokantayhteys suljettu");
    }
    
    /**
     * Metodi joka poistaa tietokannan, jos se on olemassa, jonka jalkeen luo uuden
     * @param c Driver tietokanta yhteys
     * @param db sql kysely
     * @throws SQLException Tietokantavirhe
     */
    public static void createDatabase(Connection c, String db) throws SQLException {

        Statement stmt = c.createStatement();
        stmt.executeQuery("DROP DATABASE IF EXISTS " + db);
        System.out.println("\t>> Tietokanta " + db + " tuhottu");

        stmt.executeQuery("CREATE DATABASE " + db);
        System.out.println("\t>> Tietokanta " + db + " luotu");

        stmt.executeQuery("USE " + db);
        System.out.println("\t>> Kaytetaan tietokantaa " + db);

    }
    
    /**
     * Uuden taulun luominen
     * @param c Driver tietokanta yhteys
     * @param sql sql kysely
     * @param tauluNimi taulun nimi
     * @throws SQLException Tietokantavirhe
     */
    public static void createTable(Connection c, String sql, String tauluNimi) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeQuery(sql);
        System.out.println("\n" + "\t>> Taulu " + tauluNimi + " luotu");
    }
    
    /**
     * Metodi joka asettaa tietokannan valituksi
     * @param c Driver tietokanta yhteys
     * @param db sql kysely
     * @throws SQLException Tietokantavirhe
     */
    public static void useDatabase(Connection c, String db) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE " + db);
        System.out.println("\t>> Käytetään tietokantaa " + db);
    }
    
    /**
     * Valitsee asiakkaiden tiedot tableview:n täyttämistä varten
     * @param c Driver tietokanta yhteys
     * @return rs asiakas tulos joukko
     * @throws SQLException Tietokantavirhe
     */
     public static ResultSet selectAsiakas(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT asiakasID, etunimi, sukunimi, yritys FROM asiakas ORDER BY etunimi"
        );    
        return rs;       
    }
     
     /**
      * Metodi joka palauttaa opiskelijan tiedot tietokannasta
      * @param c Driver tietokanta yhteys
      * @return rs toimipiste tulosjoukko
      * @throws SQLException Tietokantavirhe
      */
     public static ResultSet selectToimipiste(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT toimipisteID, toimipisteNimi, vuorokausiHinta, toimipisteKoko FROM toimipiste ORDER BY toimipisteID"
        );        
        return rs;     
    }
      
     /**
      * Metodi joka palauttaa opiskelijan tiedot tietokannasta
      * @param c Driver tietokanta yhteys
      * @return rs palvelun tulosjoukko
      * @throws SQLException Tietokantavirhe
      */
     public static ResultSet selectPalvelu(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT palvelunID, palvelunNimi, palvelunHinta, palvelunKuvaus FROM palvelu ORDER BY palvelunID"
        );
        return rs;
    }
     
     /**
      * Metodi joka palauttaa opiskelijan tiedot tietokannasta
      * @param c Driver tietokanta yhteys
      * @return rs varausten tulosjoukko
      * @throws SQLException Tietokantavirhe
      */
     public static ResultSet selectVaraus(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT varausID, aloitusPaiva, lopetusPaiva, asiakasID, toimipisteID FROM varaus ORDER BY varausID"
        );    
        return rs;  
    }
      
     /**
      * Metodi joka palauttaa varattujen palveluiden tiedot tietokannasta
      * @param c Driver tietokanta yhteys
      * @return rs VaratutPalvelut tulosjoukko
      * @throws SQLException Tietokantavirhe
      */
     public static ResultSet selectVaratutPalvelut(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT toimipisteID, palvelunID, varausID, asiakasID FROM toimipisteidenPalvelut ORDER BY toimipisteID"
        ); 
        return rs;  
    }
    
     /**
      * Metodi joka lisätä tietokannan asiakaita
      * @param c Driver tietokanta yhteys
      * @param asiakasID Asiakas numero
      * @param etunimi Asiakas etunimi
      * @param sukunimi Asiakas sukunimi
      * @param yritys Yritys nimi
      * @throws SQLException  Tietokantavirhe
      */
    public static void addAsiakas(Connection c, int asiakasID, String etunimi, String sukunimi, 
                                    String yritys)throws SQLException {           
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO asiakas (asiakasID, etunimi, sukunimi, yritys)"
         + "VALUES(?, ?, ?, ?)"
        );
        
        ps.setInt(1, asiakasID);
        ps.setString(2, etunimi);
        ps.setString(3, sukunimi);
        ps.setString(4, yritys);
         try {
        ps.execute();
    
        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Asiakkaan lisääminen");
            alert.setHeaderText("Virhe");
            alert.setContentText("Asiakkaan lisääminen epäonnistui");
            alert.showAndWait();
    }             
        System.out.println("\t>> Lisätty asiakas: " + etunimi +" "+ sukunimi);     
    }
    
    /**
     * Metodi joka lisätä tietokannan tyontekijat
     * @param c Driver tietokanta yhteys
     * @param tyontekijaID Työntekija numero
     * @param etunimi Työntekja etunimi
     * @param sukunimi Työntekija sukunimi
     * @throws SQLException Tietokantavirhe
     */
    public static void addTyontekija(Connection c, int tyontekijaID, String etunimi, String sukunimi)throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO tyontekija (tyontekijaID, etunimi, sukunimi)"
         + "VALUES(?, ?, ?)"
        );
        
        ps.setInt(1, tyontekijaID);
        ps.setString(2, etunimi);
        ps.setString(3, sukunimi);
        
        ps.execute();       
        System.out.println("\t>> Lisätty tyontekija: " + etunimi +" "+ sukunimi);
    }
    
    /**
     * Metodi joka lisätä tietokannan tyontekijat
     * @param c Driver tietokanta yhteys
     * @param toimipisteID Toimipiste numero
     * @param toimipisteNimi Toimipiste nimi
     * @param vuorokausiHinta Vuorokausi hinta
     * @param toimipisteKoko Toimipiste koko
     * @throws SQLException  Tietokantavirhe
     */
    public static void addToimipiste(Connection c, int toimipisteID, String toimipisteNimi, 
                                    int vuorokausiHinta, int toimipisteKoko )throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO toimipiste (toimipisteID, toimipisteNimi, vuorokausiHinta, toimipisteKoko)"
         + "VALUES(?, ?, ?, ?)"
        );
        
        ps.setInt(1, toimipisteID);
        ps.setString(2, toimipisteNimi);
        ps.setInt(3, vuorokausiHinta);
        ps.setInt(4, toimipisteKoko);
        
        ps.execute();       
        System.out.println("\t>> Lisätty toimipiste: " + toimipisteNimi);
        
    }
    
    /**
     * Metodi joka lisätä tietokannan varaukset
     * @param c Driver tietokanta yhteys
     * @param varausID Varaus numero
     * @param aloitusPaiva Aloitus päivämäärä
     * @param lopetusPaiva Lopetus Pävämäärä
     * @param asiakasID Asiakas numero
     * @param toimipisteID Toimipiste numero
     * @throws SQLException Tietokantavirhe
     */
    public static void addVaraus(Connection c, int varausID, String aloitusPaiva,String lopetusPaiva,
                                    int asiakasID, int toimipisteID )throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO varaus (varausID, aloitusPaiva, lopetusPaiva, asiakasID, toimipisteID)"
         + "VALUES(?, STR_TO_DATE(?, '%d.%m.%Y'), STR_TO_DATE(?, '%d.%m.%Y'), ?, ?)"
        );
        
        ps.setInt(1, varausID);
        ps.setString(2, aloitusPaiva);
        ps.setString(3, lopetusPaiva);
        ps.setInt(4, asiakasID);
        ps.setInt(5, toimipisteID);
            
        ps.execute();       
        System.out.println("\t>> Lisätty varaus: " + varausID );   
    }
   
    /**
     * Metodi joka lisätä tietokannan laskut
     * @param c Driver tietokanta yhteys
     * @param laskuID Laksun numero
     * @param asiakasID Asiakas numero
     * @param erapaiva Era päivämäärä
     * @param maksupaiva Maksu päivämäärä
     * @param summa Summa
     * @param maksettu Maksettu
     * @param laskutusTyyppi Laskustus tyyppi
     * @throws SQLException Tietokantavirhe
     */
     public static void addLasku(Connection c, int laskuID, int asiakasID, String erapaiva, String maksupaiva,
                                    int summa, int maksettu, String laskutusTyyppi)throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO lasku (laskuID, asiakasID, erapaiva, maksupaiva, summa, maksettu,laskutusTyyppi)"
         + "VALUES(?, ?, STR_TO_DATE(?, '%d.%m.%Y'), STR_TO_DATE(?, '%d.%m.%Y'), ?, ?, ?)"
        );
        
        ps.setInt(1, laskuID);
        ps.setInt(2, asiakasID);
        ps.setString(3, erapaiva);
        ps.setString(4, maksupaiva);
        ps.setInt(5, summa);
        ps.setInt(6, maksettu);
        ps.setString(7, laskutusTyyppi);
            
        try {
        ps.execute();
    
        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Laskun lisääminen");
            alert.setHeaderText("Virhe");
            alert.setContentText("Laskun lisääminen epäonnistui");
            alert.showAndWait();
      
    }      
        System.out.println("\t>> Lisätty lasku: " + laskuID + " " + laskutusTyyppi);
        
    }
    
    /**
     * Metodi joka lisätä tietokannan varaukset
     * @param c Driver tietokanta yhteys
     * @param palvelunID Palvelun numero
     * @param palvelunNimi Palvelun nimi
     * @param palvelunHinta Palvelun hinta
     * @param palvelunKuvaus Palvelun kuvaus
     * @throws SQLException Tietokantavirhe
     */
    public static void addPalvelu(Connection c, int palvelunID, String palvelunNimi, int palvelunHinta, String palvelunKuvaus) throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO palvelu (palvelunID, palvelunNimi, palvelunHinta, palvelunKuvaus)"
         + "VALUES(?, ?, ?, ?)"
        );
        
        ps.setInt(1, palvelunID);
        ps.setString(2, palvelunNimi);
        ps.setInt(3, palvelunHinta);
        ps.setString(4, palvelunKuvaus);
            
        ps.execute();       
        System.out.println("\t>> Lisätty palvelu: " + palvelunID + " " + palvelunNimi);
        
    }
    
    /**
     * Metodi joka lisätä tietokannan varaukset
     * @param c Driver tietokanta yhteys
     * @param toimipisteID Toimipiste numero
     * @param palvelunID Palvelun numero
     * @param varausID Varaus numero
     * @param asiakasID Asiakas numero
     * @throws SQLException Tietokantavirhe
     */
    public static void addToimipisteidenPalvelut(Connection c, int toimipisteID, int palvelunID, int varausID, int asiakasID) throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO toimipisteidenPalvelut(toimipisteID, palvelunID, varausID, asiakasID)"
         + "VALUES(?, ?, ?, ?)"
        );
        
        ps.setInt(1, toimipisteID);
        ps.setInt(2, palvelunID);
        ps.setInt(3, varausID);
        ps.setInt(4, asiakasID);
                  
        ps.execute();       
        System.out.println("\t>> Lisätty toimipisteidenPalvelut: " + palvelunID );       
    }
    
    /**
     * Metodi joka lisätä tietokannan laskun maksajat 
     * @param c Driver tietokanta yhteys
     * @param asiakasID Asiakas numero
     * @param laskuID Laskun numero
     * @throws SQLException Tietokantavirhe
     */
    public static void addlaskunMaksaja(Connection c, int asiakasID, int laskuID)throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO laskunMaksaja(asiakasID, laskuID)"
         + "VALUES(?, ?)"
        );
        
        ps.setInt(1, asiakasID);
        ps.setInt(2, laskuID);
                  
        ps.execute();       
        System.out.println("\t>> Lisätty laskunMaksaja taulu: " + asiakasID + " " + laskuID );       
    }
    
    /**
     * Metodi joka lisätä tietokannan laskun maksajat 
     * @param c Driver tietokanta yhteys
     * @param tyontekijaID Työntekija numero
     * @param laskuID Lasku numero
     * @throws SQLException Tietokantavirhe
     */
    public static void addLaskunKasittelija(Connection c, int tyontekijaID, int laskuID)throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO laskunKasittelija(tyontekijaID, laskuID)"
         + "VALUES(?, ?)"
        );
        
        ps.setInt(1, tyontekijaID);
        ps.setInt(2, laskuID);
                  
        ps.execute();       
        System.out.println("\t>> Lisätty laskunMaksaja taulu: " + tyontekijaID + " " + laskuID );       
    }
    
    /**
     * Metodi joka lisätä tietokannan laskun maksajat
     * @param c Driver tietokanta yhteys
     * @param tyontekijaID tyontekija numero
     * @param varausID varaus numero
     * @throws SQLException Tietokantavirhe
     */
    public static void addVarauksenKasittelija(Connection c, int tyontekijaID, int varausID)throws SQLException {   
        
        PreparedStatement ps = c.prepareStatement
        (
        "INSERT INTO varauksenKasittelija(tyontekijaID, varausID)"
         + "VALUES(?, ?)"
        );
        
        ps.setInt(1, tyontekijaID);
        ps.setInt(2, varausID);
                  
        ps.execute();       
        System.out.println("\t>> Lisätty laskunMaksaja taulu: " + tyontekijaID + " " + varausID );       
    }
    
    /**
     * Metodi joka muokkaa toimipisteen tietoja
     * @param c Connection
     * @param toimipisteID toimipiste numero 
     * @param toimipisteNimi toimipiste nimi
     * @param vuorokausiHinta hinta
     * @param toimipisteKoko toimipiste koko 
     * @throws SQLException Tietokantavirhe
     */
    public static void updateToimipiste(Connection c, int toimipisteID, String toimipisteNimi, int vuorokausiHinta, int toimipisteKoko) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement(
        ("UPDATE toimipiste SET toimipisteNimi = ?, vuorokausiHinta = ?, toimipisteKoko = ? WHERE toimipisteID = ?")
        );

        // Syotetaan tiedot parametreilla
        ps.setString(1, toimipisteNimi);
        ps.setInt(2, vuorokausiHinta);
        ps.setInt(3, toimipisteKoko);
        ps.setInt(4, toimipisteID);
   
   
        // Toteutetaan muutokset
        ps.execute();
    
    System.out.println("\t>> Päivitetty toimipisteID tiedot: " + toimipisteID);
    
    }
    
    /**
     * Metodi joka muokkaa palvelun tietoja
     * @param c Driver tietokanta yhteys
     * @param palvelunID Palvelun numero
     * @param palvelunNimi Palvelun nimi
     * @param palvelunHinta Palvelun hinta
     * @param palvelunKuvaus Palvelun kuvaus
     * @throws SQLException Tietokantavirhe
     */
    public static void updatePalvelu(Connection c, int palvelunID, String palvelunNimi, int palvelunHinta, String palvelunKuvaus) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement(
        ("UPDATE palvelu SET palvelunNimi = ?, palvelunHinta = ?, palvelunKuvaus = ? WHERE palvelunID = ?")
        );

        // Syotetaan tiedot parametreilla
        ps.setString(1, palvelunNimi);
        ps.setInt(2, palvelunHinta);
        ps.setString(3, palvelunKuvaus);
        ps.setInt(4, palvelunID);
   
   
        // Toteutetaan muutokset
        ps.execute();
    
    System.out.println("\t>> Päivitetty palvelunID tiedot: " + palvelunID);
    
    }
    
    /**
     * Metodi joka muokkaa palvelun tietoja 
     * @param c Driver tietokanta yhteys
     * @param varausID Varaus numero
     * @param aloitusPaiva Aloitus päivämäärä
     * @param lopetusPaiva Lopetus päivä
     * @param asiakasID Asiakas numero
     * @param toimipisteID Toimipiste numero
     * @throws SQLException Tietokantavirhe
     */
    public static void updateVaraus(Connection c, int varausID, String aloitusPaiva, String lopetusPaiva,
                                    int asiakasID, int toimipisteID) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement(
        ("UPDATE varaus SET aloitusPaiva = STR_TO_DATE(?, '%d.%m.%Y'), lopetusPaiva = STR_TO_DATE(?, '%d.%m.%Y'), asiakasID = ?, toimipisteID= ? WHERE varausID = ?")
        );

        // Syotetaan tiedot parametreilla
        ps.setString(1, aloitusPaiva);
        ps.setString(2, lopetusPaiva);
        ps.setInt(3, asiakasID);
        ps.setInt(4, toimipisteID);

        // Toteutetaan muutokset
        ps.executeUpdate();
    
    System.out.println("\t>> Päivitetty varaus tiedot: " + varausID);
    
    }
    
    /**
     * Metodi joka muokkaa palveluiden varausten tietoja
     * @param c Driver tietokanta yhteys
     * @param toimipisteID Toimipiste numero
     * @param palvelunID Palvelun numero
     * @param varausID Varaus numero
     * @param asiakasID Asiakas numero
     * @throws SQLException Tietokantavirhe
     */
    public static void updatePalveluVaraus(Connection c, int toimipisteID, int palvelunID, int varausID, int asiakasID) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement(
        ("UPDATE toimipisteidenPalvelut SET toimipisteID = ?, palvelunID = ?, varausID = ?, asiakasID = ? WHERE toimipisteID = ?")
        );

        // Syotetaan tiedot parametreilla
        ps.setInt(1, toimipisteID);
        ps.setInt(2, palvelunID);
        ps.setInt(3, varausID);
        ps.setInt(4, asiakasID);
        ps.setInt(5, toimipisteID);
    
        // Toteutetaan muutokset
        ps.execute();
    
    System.out.println("\t>> Päivitetty palvelunID tiedot: " + palvelunID);
    
    }

    /**
     * Metodi joka poistaa toimipisteen tiedot
     * @param c Driver tietokanta yhteys
     * @param toimipisteID Toimipiste numero
     * @throws SQLException Tietokantavirhe
     */
    public static void deleteToimipiste(Connection c, int toimipisteID) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement( 
        ("DELETE FROM toimipiste WHERE toimipisteID = ?")           
        );
        
        // Syotetaan tiedot paremetreilla
        ps.setInt(1, toimipisteID);
    
        // Suoritetaan poisto
        ps.execute();
        System.out.println("\t>> poistettu Toimipiste, jonka ID on " + toimipisteID);
   
    }
     
    /**
     * Metodi joka poistaa palvelun tiedot
     * @param c Driver tietokanta yhteys
     * @param palvelunID Palvelun numero
     * @throws SQLException Tietokantavirhe
     */
    public static void deletePalvelu(Connection c, int palvelunID) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement( 
        ("DELETE FROM palvelu WHERE palvelunID = ?")           
        );
        
        // Syotetaan tiedot paremetreilla
        ps.setInt(1, palvelunID);
    
        // Suoritetaan poisto
        ps.execute();
        System.out.println("\t>> poistettu palvelu, jonka ID on " + palvelunID);
   
    }

    /**
     * Metodi joka poistaa varaus
     * @param c Driver tietokanta yhteys
     * @param varausID Varaus numero
     * @throws SQLException Tietokantavirhe
     */
    public static void deleteVaraus(Connection c, int varausID) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement( 
        ("DELETE FROM varaus WHERE varausID = ?")           
        );
        
        // Syotetaan tiedot paremetreilla
        ps.setInt(1, varausID);
    
        // Suoritetaan poisto
        ps.execute();
        System.out.println("\t>> poistettu varaus, jonka ID on " + varausID);
   
    }

    /**
     * Metodi joka poistaa palvelun varauksen tiedot
     * @param c Driver tietokanta yhteys
     * @param palvelunID Palvelun numero
     * @throws SQLException Tietokantavirhe
     */
    public static void deletePalveluVaraus(Connection c, int palvelunID) throws SQLException {
    
        PreparedStatement ps = c.prepareStatement( 
        ("DELETE FROM toimipisteidenPalvelut WHERE palvelunID = ?")           
        );
        
        // Syotetaan tiedot paremetreilla
        ps.setInt(1, palvelunID);
    
        // Suoritetaan poisto
        ps.execute();
        System.out.println("\t>> poistettu palvelu, jonka ID on " + palvelunID);
   
    }


}
