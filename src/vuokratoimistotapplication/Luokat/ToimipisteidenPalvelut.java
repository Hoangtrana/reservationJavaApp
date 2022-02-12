/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication.Luokat;

/**
 *
 * @author matti
 */
/**
 * ToimipisteidenPalvelut attribuutit
 */
public class ToimipisteidenPalvelut {

    private int toimipisteID;
    /*int toimipisteID*/
    private int palvelunID;
    /*palvelunID*/
    private int varausID;
    /*varausID*/
    private int asiakasID;

    /*asiakasID*/

    /**
     * Oletuskonstruktori
     */
    public ToimipisteidenPalvelut() {

    }

    /**
     * Konstrukori joka saa parametrin perusteella kaikki toimipisteidenPalvelut
     * tiedot
     *
     * @param toimipisteID toimipisteen ID
     * @param palvelunID palvelun ID
     * @param varausID varauksen ID
     * @param asiakasID asiakkaas ID
     */
    public ToimipisteidenPalvelut(int toimipisteID, int palvelunID, int varausID, int asiakasID) {
        this.toimipisteID = toimipisteID;
        this.palvelunID = palvelunID;
        this.varausID = varausID;
        this.asiakasID = asiakasID;
    }

    //Setterit ja getterit
    /**
     * Metodi joka asettaa toimipisteen ID:n
     *
     * @param toimipisteID toimipisteen ID
     */
    public void setToimipisteID(int toimipisteID) {
        this.toimipisteID = toimipisteID;
    }

    /**
     * Metodi joka asettaa palvelun ID:n
     *
     * @param palvelunID palvelun ID
     */
    public void setpalvelulnID(int palvelunID) {
        this.palvelunID = palvelunID;
    }

    /**
     * Metodi joka asettaa varauksen ID:n
     *
     * @param varausID varauksen ID
     */
    public void setVarausID(int varausID) {
        this.varausID = varausID;
    }

    /**
     * Metodi joka asettaa asiakkaan ID:n
     *
     * @param asiakasID asiakkaan ID
     */
    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    /**
     * Metodi joka palauttaa toimipisteen ID:n
     *
     * @return toimipisteID
     */
    public int getToimipisteID() {
        return toimipisteID;
    }

    /**
     * Metodi joka palauttaa palvelun ID:n
     *
     * @return palvelunID
     */
    public int getPalvelunID() {
        return palvelunID;
    }

    /**
     * Metodi joka palauttaa varauksen ID:n
     *
     * @return varausID
     */
    public int getVarausID() {
        return varausID;
    }

    /**
     * Metodi joka palauttaa asiakkaan ID:n
     *
     * @return asiakasID
     */
    public int getAsiakasID() {
        return asiakasID;
    }

    /**
     * Metodi joka palauttaa kaikki toimipisteidenPalvelut tiedot merkkijonona
     *
     * @return toimipisteID, palvelunID, varausID, asiakasID
     */
    @Override
    public String toString() {
        return (toimipisteID + " " + palvelunID + " " + varausID + " " + asiakasID);
    }

}
