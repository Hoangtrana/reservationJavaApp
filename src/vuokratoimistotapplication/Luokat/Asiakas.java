/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hoang & janne
 */
package vuokratoimistotapplication.Luokat;

/**
 *Asiakas luokan atribuutit
 */

public class Asiakas {
    private int asiakasID; /**int asiakasID*/
    private String etunimi; /**String etunimi*/
    private String sukunimi; /**String sukunimi*/
    private String yritys; /**String yritys*/
    
    /**
     *Tyhjä oletus konstruktori
     */
    public Asiakas(){
        
    }
    
    /**
     *Konstruktori, jolle voi antaa Asiakas-luokan atribuutin tiedot
     * @param asiakasID asiakkaan yksilöivä numero
     * @param etunimi asiakkaan etunimi
     * @param sukunimi asiakkaan sukunimi
     * @param yritys asiakkaan yrityksen nimi
     */
    public Asiakas(int asiakasID, String etunimi, String sukunimi, String yritys){
        this.asiakasID=asiakasID;
        this.etunimi=etunimi;
        this.sukunimi=sukunimi;
        this.yritys=yritys;
    }
    


    //Getterit ja setterit

    /**
     *Asettaa asiakkaalle asiakasID:n
     * @param asiakasID asettaa asikkaan yksilöivän ID:n
     */
    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    /**
     *Asettaa asiakkaalle etunimen
     * @param etunimi asettaa asiakkaalle etunimen
     */
    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    /**
     *Asettaa asiakkaalle sukunimen
     * @param sukunimi asettaa asiakkaalle sukunimen
     */
    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    /**
     *Asettaa asiakkaalle yrityksen
     * @param yritys asettaa asiakkaalle yrityksen nimen
     */
    public void setYritys(String yritys) {
        this.yritys = yritys;
    }

    /**
     *Palauttaa asiakkaan yksilöivän asiakasID:n
     * @return asiakasID
     */
    public int getAsiakasID() {
        return asiakasID;
    }

    /**
     *Palauttaa asiakkaan etunimen
     * @return etunimi
     */
    public String getEtunimi() {
        return etunimi;
    }

    /**
     *Palauttaa asiakkaan sukunimen
     * @return sukunimi
     */
    public String getSukunimi() {
        return sukunimi;
    }

    /**
     *Palauttaa yrityksen nimen
     * @return yritys
     */
    public String getYritys() {
        return yritys;
    }

    /**
     *Asettaa asiakkaalle etunimen, sukunimen, ja yrityksen
     * @param etunimi asettaa asiakkaalle etunimen
     * @param sukunimi asettaa asiakkaalle sukunimen
     * @param yritys asettaa asiakkaalle yrityksen nimen
     */
    public void setAsiakas(String etunimi, String sukunimi, String yritys){
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.yritys = yritys;
    }

    /**
     *Palauuttaa asiakkaa etunimen, sukunimen ja yrityksen
     * @return etunimi sukunimi yritys
     */
    public String getAsiakas() {
        return (etunimi+" "+sukunimi+" "+yritys);
    }
    
    @Override
    public String toString() {
        return (asiakasID+" "+etunimi+" "+sukunimi+" "+yritys);
    }
            
    
}