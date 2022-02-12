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

import java.sql.Date;

/**
 *Lasku-luokan atribuutit
 * 
 */
public class Lasku {
    
    private int laskuID; /**int laskuID*/
    private int asiakasID; /**int asiakasID*/
    private Date erapaiva; /**Date erapaiva*/
    private Date maksupaiva; /**Date maksupaiva*/
    private int summa; /**int summa*/
    private int maksettu; /**int maksettu*/
    private String laskutusTyyppi; /**String laskutusTyyppi*/
    
    /**
     *Tyhjä laskun oletuskonstuktori 
     */
    public Lasku(){
        
    }
    
    /**
     *Lasku konstruktori
     * @param laskuID asettaa laskulle yksivöivä tunnisteen
     * @param asiakasID asettaa laskun asiakkaalle yksivöivä tunnisteen
     * @param erapaiva asettaa  laskulle eräpäivän
     * @param maksupaiva asettaa laskulle maksupäivän
     * @param summa asettaa laskulle summan paljon on maksettava
     * @param maksettu asettaa kuinka paljon laskusta on maksettu
     * @param laskutusTyyppi asettaa onko lasku esim. email- tai paperilasku
     */
    public Lasku (int laskuID, int asiakasID, Date erapaiva, Date maksupaiva, int summa, int maksettu, String laskutusTyyppi){
        this.laskuID = laskuID;
        this.asiakasID = asiakasID;
        this.erapaiva = erapaiva;
        this.maksupaiva = maksupaiva;
        this.summa = summa;
        this.maksettu = maksettu;
        this.laskutusTyyppi = laskutusTyyppi;
    }
    
    //Getterit ja setterit

    /**
     *palauttaa laskun yksilöivän laskuID:n
     * @return laskuID
     */
    public int getLaskuID() {
        return laskuID;
    }
    
    /**
     *palauttaa laskutettavan asiakkaan yksilöivän asiakasID:n
     * @return asiakasID
     */
    public int getAsiakasID() {
        return asiakasID;
    }

    /**
     *palauttaa laskun eräpäivän
     * @return erapaiva
     */
    public Date getErapaiva() {
        return erapaiva;
    }

    /**
     *Palauttaa laskun maksupäivän
     * @return maksupaiva
     */
    public Date getMaksupaiva() {
        return maksupaiva;
    }

    /**
     *palauttaa paljonko laskusta tulee maksaa
     * @return summa
     */
    public int getSumma() {
        return summa;
    }

    /**
     *palauttaa paljonko asiakas on maksanut laskusta
     * @return maksettu
     */
    public int getMaksettu() {
        return maksettu;
    }

    /**
     *palauttaa minkälainen laskutusyyppi laskulla on esim. email-ja/tai paperilasku
     * @return laskutusTyyppi
     */
    public String getLaskutusTyyppi() {
        return laskutusTyyppi;
    }
    
    /**
     *asettaa laskulle laskuID:n
     * @param laskuID asettaa laskulle yksilöivän laskuID:n
     */
    public void setLaskuID(int laskuID) {
        this.laskuID = laskuID;
    }
    
    /**
     *asettaa asikkaan ykslöivän asiakasID:n
     * @param asiakasID laskutettavan asiakkaan yksilöivä asiakasID
     */
    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    /**
     *asettaa laskulle eräpäivän
     * @param erapaiva laskun eräpäivä
     */
    public void setErapaiva(Date erapaiva) {
        this.erapaiva = erapaiva;
    }

    /**
     *asettaa laskulle maksupäivän
     * @param maksupaiva laskun maksupäivä
     */
    public void setMaksupaiva(Date maksupaiva) {
        this.maksupaiva = maksupaiva;
    }

    /**
     *asettaa laskulle summan, paljonko pitää maksaa
     * @param summa laskutettavan maksettava summa
     */
    public void setSumma(int summa) {
        this.summa = summa;
    }

    /**
     *asettaa paljonko asiakkas on jo maksanut
     * @param maksettu paljonko asiakas maksanut
     */
    public void setMaksettu(int maksettu) {
        this.maksettu = maksettu;
    }

    /**
     *asettaa millä tavalla asiakas haluaa maksaa, email ja/tai paperilasku
     * @param laskutusTyyppi laskutettava maksaa email-ja/tai paperilaskulla
     */
    public void setLaskutusTyyppi(String laskutusTyyppi) {
        this.laskutusTyyppi = laskutusTyyppi;
    }
    
    @Override
    public String toString() {
        return (laskuID + " " + asiakasID + " " + erapaiva + " " + maksupaiva + " " + summa + " " + maksettu + " " + laskutusTyyppi);
    }
    
}
