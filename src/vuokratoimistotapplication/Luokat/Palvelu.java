/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication.Luokat;

/**
 * @author hoang
 * @author janne 
 * @author matti
 */

/*
* Palveluiden attribuutit
**/
public class Palvelu {

    private int palvelunID;
    /*int palvelunID*/
    private String palvelunNimi;
    /*String palvelunNimi*/
    private int palvelunHinta;
    /*int palvelunHinta*/
    private String palvelunKuvaus;

    /*String palveluKuvaus*/

    /**
     * Oletuskonstruktori
     */
    public Palvelu() {

    }

    /**
     * Konstrukori joka saa parametrin perusteella kaikki toimipisteen tiedot
     * @param palvelunID palvelun ID
     * @param palvelunNimi palvelun nimi
     * @param palvelunHinta palvelun hinta
     * @param palvelunKuvaus palvelun kuvaus
     */
    public Palvelu(int palvelunID, String palvelunNimi, int palvelunHinta, String palvelunKuvaus) {
        this.palvelunID = palvelunID;
        this.palvelunNimi = palvelunNimi;
        this.palvelunHinta = palvelunHinta;
        this.palvelunKuvaus = palvelunKuvaus;
    }

    //Setterit ja getterit
    
    /**
     * Metodi joka asettaa palvelun ID:n
     * @param palvelunID palvelun ID
     */
    public void setPalvelunID(int palvelunID) {
        this.palvelunID = palvelunID;
    }

    /**
     * Metodi joka asettaa palvelun nimen
     * @param palvelunNimi palvelun nimi
     */
    public void setPalvelunNimi(String palvelunNimi) {
        this.palvelunNimi = palvelunNimi;
    }

    /**
     * Metodi joka asettaa palvelun hinnan
     * @param palvelunHinta palvelun hinta
     */
    public void setPalvelunHinta(int palvelunHinta) {
        this.palvelunHinta = palvelunHinta;
    }

    /**
     * Metodi joka asettaa palvelun kuvauksen
     * @param palvelunKuvaus palvelun kuvaus
     */
    public void setPalvelunKuvaus(String palvelunKuvaus) {
        this.palvelunKuvaus = palvelunKuvaus;
    }

    /**
     * Metodi joka palauttaa palvelun ID:n
     * @return palvelunID
     */
    public int getPalvelunID() {
        return palvelunID;
    }

    /**
     * Metodi joka palauttaa palvelun nimen
     * @return palvelunNimi
     */
    public String getPalvelunNimi() {
        return palvelunNimi;
    }

    /**
     * Metodi joka palauttaa palvelun hinnan
     * @return palvelunHinta
     */
    public int getPalvelunHinta() {
        return palvelunHinta;
    }

    /**
     * Metodi joka palauttaa palvelun kuvauksen
     * @return palvelunKuvaus
     */
    public String getPalvelunKuvaus() {
        return palvelunKuvaus;
    }

    /**
     * Metodi joka palauttaa kaikki palvelun tiedot merkkijonona
     * @return palvelunID, palvelunNimi, palvelunHinta, palvelunKuvaus
     */
    @Override
    public String toString() {
        return (palvelunID + " " + palvelunNimi + " " + palvelunHinta + " " + palvelunKuvaus);
    }

}
