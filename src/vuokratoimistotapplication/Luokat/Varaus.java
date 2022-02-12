package vuokratoimistotapplication.Luokat;

import java.util.Date;

/**
 * @author janne
 * @author hoang
 * @since JDK1.3
 */
public class Varaus {
     /**
     * Varausten attribuuttit
     */
    private int varausID;
    private Date aloitusPaiva;
    private Date lopetusPaiva;
    private int asiakasID;
    private int toimipisteID;
    
    /**
     * Varausten konstruktori joka saa parametreina aloituspäivä ja lopetus päivä 
     * @param aloitusPaiva aloitus päivämäärä
     * @param lopetusPaiva lopetus päivämäärä
     */
    public Varaus(Date aloitusPaiva, Date lopetusPaiva) {
        this.aloitusPaiva = aloitusPaiva;
        this.lopetusPaiva = lopetusPaiva;
    }
    
    /**
     * Varausten konstruktori joka saa parametreina kaikki varausten tiedot.
     * @param varausID varaus numero
     * @param aloitusPaiva aloitus päivämäärä
     * @param lopetusPaiva lopetus päivämäärä
     * @param asiakasID asiakas numero
     * @param toimipisteID toimipiste numero
     */
    public Varaus(int varausID, Date aloitusPaiva, Date lopetusPaiva, int asiakasID, int toimipisteID) {
        this.varausID = varausID;
        this.aloitusPaiva = aloitusPaiva;
        this.lopetusPaiva = lopetusPaiva;
        this.asiakasID = asiakasID;
        this.toimipisteID = toimipisteID;
    }
    
    /**
     * Tyhjä oletus konstruktori
     */
    public Varaus(){
        
    }

    /**
     * Asettaa varausten numero
     * @param varausID varaus numero
     */
    public void setVarausID(int varausID) {
        this.varausID = varausID;
    }
    
    /**
     * Asettaa varausten numero
     * @param aloitusPaiva aloitus päivämäärä    
     */
    public void setAloitusPaiva(Date aloitusPaiva) {
        this.aloitusPaiva = aloitusPaiva;
    }
    
   /**
     * Asettaa varausten numero
     * @param lopetusPaiva lopetus päivämäärä    
     */ 
    public void setLopetusPaiva(Date lopetusPaiva) {
        this.lopetusPaiva = lopetusPaiva;
    }

    /**
     * Asettaa varausten asiakas numero
     * @param asiakasID asiakas numero
     */
    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    /**
     * Asettaa varausten toimipiste numero
     * @param toimipisteID toimipiste numero
     */
    public void setToimipisteID(int toimipisteID) {
        this.toimipisteID = toimipisteID;
    }
   
    /**
     * Palauttaa varaus numero
     * @return varausID
     */
    public int getVarausID() {
        return varausID;
    }

    /**
     * Palauttaa varausten aloituspäivä
     * @return aloitusPaiva
     */
    public Date getAloitusPaiva() {
        return aloitusPaiva;
    }
    
    /**
     * Palauttaa varausten lopetuspäivä
     * @return lopetusPaiva
     */
    public Date getLopetusPaiva() {
        return lopetusPaiva;
    }
    
    /**
     * Palauttaa varausten asiakas numero
     * @return asiakasID
     */
    public int getAsiakasID() {
        return asiakasID;
    }
    
    /**
     * Palauttaa varausten toimipiste numero
     * @return toimipisteID
     */
    public int getToimipisteID() {
        return toimipisteID;
    }
    
    /**
     * Palauttaa varausten kaikki tiedot
     * @return varausID, aloitusPaiva, lopetusPaiva, asiakasID, toimipisteID 
     */
    @Override
    public String toString() {
        return (varausID + " " + aloitusPaiva + " " + lopetusPaiva + " " + asiakasID + " " + toimipisteID);
    }
    
}
