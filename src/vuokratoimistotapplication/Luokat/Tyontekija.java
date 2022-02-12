package vuokratoimistotapplication.Luokat;

/**
 * Työntekija luokka
 * @author janne
 * @author hoang
 * @since JDK1.3
 */
public class Tyontekija {
    /**
     * Työntekija attribuuttit
     */
    private int tyontekijaID;
    private String etunimi;
    private String sukunimi;
    
    /**
     * Tyhjä oletus konstruktori
     */
    public Tyontekija(){
        
    }

    /**
     * Asettaa työntekijan numero
     * @param tyontekijaID Työntekija numero
     */
    public void setTyontekijaID(int tyontekijaID) {
        this.tyontekijaID = tyontekijaID;
    }

    /**
     * Asettaa työntekijan etunimi
     * @param etunimi Työntekija etunimi
     */
    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }
    
    /**
     * Asettaa työntekijan sukunimi
     * @param sukunimi Työntekija sukunimi
     */
    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }
    
    /**
     * Palauttaa työntekijan numero
     * @return tyontekijaID
     */
    public int getTyontekijaID() {
        return tyontekijaID;
    }

    /**
     * Palauttaa työntekijan etunimi
     * @return etunimi
     */
    public String getEtunimi() {
        return etunimi;
    }
    
    /**
     * Palauttaa työntekijan sukunimi
     * @return sukunimi
     */
    public String getSukunimi() {
        return sukunimi;
    }
    
    
    /**
     *  Palauttaa työntekijan kaikki tiedot
     * @return tyontekijaID, etunimi,  sukunimi
     */
    @Override
    public String toString() {
        return (tyontekijaID + " " + etunimi + " " + sukunimi);
    }
    
}
