/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vuokratoimistotDatabase.vuokratoimistoDatabase;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addAsiakas;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addLasku;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addLaskunKasittelija;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addPalvelu;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addToimipiste;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addToimipisteidenPalvelut;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addTyontekija;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addVarauksenKasittelija;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addVaraus;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.addlaskunMaksaja;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.createDatabase;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.createTable;
import static vuokratoimistotDatabase.vuokratoimistoDatabase.openConnection;

/**
 *
 * @author matty
 */
public class VuokraToimistotApplication extends Application {
         
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("PaavalikkoView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Paavalikko");
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException Tietokanta virhe
     */
    public static void main(String[] args) throws SQLException {
        
        // Luodaan Connection String olemassa olevaan tietokantaan
        Connection conn = openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        // Luodaan tietokanta
        createDatabase(conn, "karelia_vuokratoimistot_R01");

        // Otetaan tietokanta kayttoon
       vuokratoimistoDatabase.useDatabase(conn, "karelia_vuokratoimistot_R01");
        
        
        //Asiakas taulu luonti
        createTable(conn, 
                "CREATE TABLE asiakas ("
                        + "asiakasID INT NOT NULL PRIMARY KEY," 
                        + "etunimi VARCHAR (20) NOT NULL,"
                        + "sukunimi VARCHAR (30)NOT NULL,"
                        + "yritys VARCHAR (30)NOT NULL)"   ,
                "asiakas"
                );
        //Lisätä asiakas tauluun
        addAsiakas(conn, 1100, "Aava", "Pesonen", "Aava Oy");
        addAsiakas(conn, 1101, "Milla", "Saarinen", "Salo Oy");
        addAsiakas(conn, 1102, "Helena", "Korhonen", "Kaiva Oy");
        
        //Tyontekija taulu luonti
        createTable(conn, 
                "CREATE TABLE tyontekija ("
                        + "tyontekijaID INT NOT NULL PRIMARY KEY," 
                        + "etunimi VARCHAR (20) NOT NULL,"
                        + "sukunimi VARCHAR (30)NOT NULL)"
                        ,
                "tyontekija"
                );
        //Lisätä tyontekija tauluun
        addTyontekija(conn, 2200, "Ansa", "Niemi");
        addTyontekija(conn, 2201, "Merja", "Mäkelä");
        
        //Toimipiste taulu luonti
        createTable(conn, 
                "CREATE TABLE toimipiste ("
                        + "toimipisteID INT NOT NULL PRIMARY KEY," 
                        + "toimipisteNimi VARCHAR (30) NOT NULL,"
                        + "vuorokausiHinta INT NOT NULL,"
                        + "toimipisteKoko INT NOT NULL)"                   
                        ,
                "toimipiste"
                );
        
        addToimipiste(conn, 60160, "Linnunlahti", 1200, 65);
        addToimipiste(conn, 60100, "Rantakylä", 800, 70);
        addToimipiste(conn, 60111, "Keskusta", 1500, 80);
        
        /**
        //Lasku taulu luonti
        createTable(conn, 
                "CREATE TABLE lasku ("
                        + "laskuID INT NOT NULL PRIMARY KEY," 
                        + "erapaiva DATE NOT NULL,"
                        + "maksupaiva DATE NOT NULL,"
                        + "summa INT NOT NULL,"       
                        + "viitenumero INT NOT NULL,"
                        + "laskutusTyyppi VARCHAR(30) NOT NULL)"
                        ,
                "lasku"
                );
        addLasku(conn, 55025, "12.3.2021", "10.3.2021", 599, 123789, "paperilasku");
        addLasku(conn, 56030, "28.1.2021", "2.1.2020", 1010, 345234, "sähköpostilasku");
        */
        createTable(conn, 
                "CREATE TABLE lasku ("
                        + "laskuID INT NOT NULL PRIMARY KEY," 
                        + "asiakasID INT NOT NULL,"
                        + "erapaiva DATE NOT NULL,"
                        + "maksupaiva DATE NOT NULL,"
                        + "summa INT NOT NULL," 
                        + "maksettu INT(7),"
                        + "laskutusTyyppi VARCHAR(30) NOT NULL,"
                        + "FOREIGN KEY (asiakasID) REFERENCES asiakas(asiakasID) ON DELETE CASCADE)"
                        ,
                "lasku"
                );
         
       addLasku(conn, 55025, 1100, "12.3.2021", "10.3.2021", 599, 0, "paperilasku");
       addLasku(conn, 56030, 1101, "12.3.2021", "12.3.2021", 599, 300, "sähköpostilasku");
       addLasku(conn, 560, 1101, "15.2.2021", "14.2.2021", 400, 400, "sähköpostilasku");
        
        
        //Palvelu taulu luonti
        createTable(conn, 
                "CREATE TABLE palvelu ("
                        + "palvelunID INT NOT NULL PRIMARY KEY," 
                        + "palvelunNimi VARCHAR (30) NOT NULL,"
                        + "palvelunHinta INT NOT NULL,"
                        + "palvelunKuvaus VARCHAR (50) NOT NULL)"
                        ,
                "palvelu"
                );
        addPalvelu(conn, 1, "Väritulostin" , 20, "Väritulostin asiakkaan vapaassa käytössä");
        addPalvelu(conn, 2, "Mustavalkotulostin", 10, "Mustavalkotulostin asiakkaan vapaassa käytössä");
        addPalvelu(conn, 3, "Faksi", 10, "Faksi asiakkaan vapaassa käytössä");
        addPalvelu(conn, 4, "Ilmastointi", 10, "Huone sisältää ilmastointilaitteen");

        //Varaus taulu luonti
        createTable(conn, 
                "CREATE TABLE varaus ("
                        + "varausID INT NOT NULL PRIMARY KEY," 
                        + "aloitusPaiva DATE NOT NULL,"
                        + "lopetusPaiva DATE NOT NULL,"
                        + "asiakasID INT NOT NULL," 
                        + "toimipisteID INT NOT NULL,"
                        + "FOREIGN KEY (asiakasID) REFERENCES asiakas(asiakasID) ON DELETE CASCADE,"
                        + "FOREIGN KEY (toimipisteID) REFERENCES toimipiste(toimipisteID) ON DELETE CASCADE)"
                        ,
                "varaus"
                );

        addVaraus(conn, 3000, "10.3.2021", "12.3.2021", 1100, 60160);
        addVaraus(conn, 3001, "12.4.2021", "15.4.2021", 1101, 60100);

        
        
        
        //ToimipistePalvelu taulu luonti
        createTable(conn, 
                "CREATE TABLE toimipisteidenPalvelut ("
                        + "toimipisteID INT NOT NULL," 
                        + "palvelunID INT NOT NULL,"
                        + "varausID INT NOT NULL,"
                        + "asiakasID INT NOT NULL,"
                        + "PRIMARY KEY (toimipisteID, palvelunID, varausID, asiakasID),"                  
                        + "FOREIGN KEY (toimipisteID) REFERENCES toimipiste(toimipisteID) ON DELETE CASCADE,"
                        + "FOREIGN KEY (palvelunID) REFERENCES palvelu(palvelunID) ON DELETE CASCADE,"
                        + "FOREIGN KEY (varausID) REFERENCES varaus(varausID) ON DELETE CASCADE,"
                        + "FOREIGN KEY (asiakasID) REFERENCES asiakas(asiakasID) ON DELETE CASCADE)"
                        ,
                "toimipisteidenPalvelut"
                );
        addToimipisteidenPalvelut(conn, 60160, 1, 3000, 1100);
        addToimipisteidenPalvelut(conn, 60100, 2, 3001, 1101);
        addToimipisteidenPalvelut(conn, 60100, 4, 3001, 1101);

      
        
        //LaskunMaksaja taulu luonti
        createTable(conn, 
                "CREATE TABLE laskunMaksaja ("
                        + "asiakasID INT NOT NULL," 
                        + "laskuID INT NOT NULL,"
                        + "PRIMARY KEY (asiakasID, laskuID),"                      
                        + "FOREIGN KEY (asiakasID) REFERENCES asiakas(asiakasID) ON DELETE CASCADE,"
                        + "FOREIGN KEY (laskuID) REFERENCES lasku(laskuID) ON DELETE CASCADE)"
                        ,
                "laskunMaksaja"
                );
        addlaskunMaksaja(conn, 1100, 55025);
        addlaskunMaksaja(conn, 1101, 56030);
        
        //LaskunKasittelija taulu luonti
        createTable(conn, 
                "CREATE TABLE laskunKasittelija ("
                        + "tyontekijaID INT NOT NULL," 
                        + "laskuID INT NOT NULL,"
                        + "PRIMARY KEY (tyontekijaID, laskuID),"                      
                        + "FOREIGN KEY (tyontekijaID) REFERENCES tyontekija(tyontekijaID) ON DELETE CASCADE,"
                        + "FOREIGN KEY (laskuID) REFERENCES lasku(laskuID) ON DELETE CASCADE)"
                        ,
                "laskunKasittelija"
                );
        addLaskunKasittelija(conn, 2200, 55025);
        addLaskunKasittelija(conn, 2201, 56030);
        
        
        //VarauksenKasittelija taulu luonti
        createTable(conn, 
                "CREATE TABLE varauksenKasittelija ("
                        + "tyontekijaID INT NOT NULL," 
                        + "varausID INT NOT NULL,"
                        + "PRIMARY KEY (tyontekijaID, varausID),"                      
                        + "FOREIGN KEY (tyontekijaID) REFERENCES tyontekija(tyontekijaID) ON DELETE CASCADE,"
                        + "FOREIGN KEY (varausID) REFERENCES varaus(varausID) ON DELETE CASCADE)"
                        ,
                "varauksenKasittelija"
                );
        addVarauksenKasittelija(conn, 2200, 3000);
        addVarauksenKasittelija(conn, 2201, 3001);
        
       //vuokratoimistoDatabase.varattuList(conn);
        
        vuokratoimistoDatabase.closeConnection(conn);
        
        launch(args);
    }
    
}
