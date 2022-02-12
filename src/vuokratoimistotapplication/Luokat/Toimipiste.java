/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistotapplication.Luokat;

/**
 *
 * @author hoang
 * @author janne
 * @author matti
 */
/*
* Toimipisteiden attribuutit
 */
public class Toimipiste {

    private int toimipisteID;
    /*int toimipisteID*/
    private String toimipisteNimi;
    /*String toimipisteNimi*/
    private int vuorokausiHinta;
    /*int vuorokausiHinta*/
    private int toimipisteKoko;

    /*toimipisteKoko*/

    /**
     * Konstruktori joka luo vai toimipisteen nimen
     *
     * @param toimipisteNimi toimipisteen nimi
     */
    public Toimipiste(String toimipisteNimi) {
        this.toimipisteNimi = toimipisteNimi;
    }

    /**
     * Oletuskonstruktori
     */
    public Toimipiste() {

    }

    /**
     * Konstruktori joka luo vain toimipisteen ID
     * @param toimipisteID toimipisteen ID
     */
    public Toimipiste(int toimipisteID) {
        this.toimipisteID = toimipisteID;
    }

    /**
     * Konstrukori joka saa parametrin perusteella kaikki toimipisteen tiedot
     * @param toimipisteID toimpisteen ID
     * @param toimipisteNimi toimipisteen nimi
     * @param vuorokausiHinta toimipisteen vuorokausihinta
     * @param toimipisteKoko toimpisteen koko
     */
    public Toimipiste(int toimipisteID, String toimipisteNimi, int vuorokausiHinta, int toimipisteKoko) {
        this.toimipisteID = toimipisteID;
        this.toimipisteNimi = toimipisteNimi;
        this.vuorokausiHinta = vuorokausiHinta;
        this.toimipisteKoko = toimipisteKoko;
    }

    /**
     * Konstruktori joka luo vain toimipisteen ID ja nimen
     * @param toimipisteID toimipisteen ID
     * @param toimipisteNimi toimipisteen nimi
     */
    public Toimipiste(int toimipisteID, String toimipisteNimi) {
        this.toimipisteID = toimipisteID;
        this.toimipisteNimi = toimipisteNimi;
    }

    //Setterit ja getterit
    /**
     * Metodi joka asettaa toimipisteen ID:n
     * @param toimipisteID toimipisteen ID
     */
    public void setToimipisteID(int toimipisteID) {
        this.toimipisteID = toimipisteID;
    }

    /**
     * Metodi joka asettaa toimipisteen nimen
     * @param toimipisteNimi toimipisteen nimi
     */
    public void setToimipisteNimi(String toimipisteNimi) {
        this.toimipisteNimi = toimipisteNimi;
    }

    /**
     * Metodi joka asettaa toimipisteen vuorokausihinnan
     * @param vuorokausiHinta toimipisteen vuorokausihinta
     */
    public void setVuorokausiHinta(int vuorokausiHinta) {
        this.vuorokausiHinta = vuorokausiHinta;
    }

    /**
     * Metodi joka asettaa toimipisteen koon
     * @param toimipisteKoko toimipisteen koko
     */
    public void setToimipisteKoko(int toimipisteKoko) {
        this.toimipisteKoko = toimipisteKoko;
    }

    /**
     * Metodi joka palauttaa toimipisteen ID:n
     * @return toimipisteID
     */
    public int getToimipisteID() {
        return toimipisteID;
    }

    /**
     * Metodi joka palauttaa toimipisteen nimen
     * @return toimipisteNimi
     */
    public String getToimipisteNimi() {
        return toimipisteNimi;
    }

    /**
     * Metodi joka palauttaa toimipisteen vuorokausihinnan
     * @return vuorokausiHinta
     */
    public int getVuorokausiHinta() {
        return vuorokausiHinta;
    }

    /**
     * Metodi joka palauttaa toimipisteen koon
     * @return toimipisteKoko
     */
    public int getToimipisteKoko() {
        return toimipisteKoko;
    }

    /**
     * Metodi joka palauttaa kaikki toimipisteen tiedot merkkijonona
     * @return toimipisteID, toimipisteNimi, vuorokausiHinta, toimipisteKoko
     */
    @Override
    public String toString() {
        return (toimipisteID + " " + toimipisteNimi + " " + vuorokausiHinta + " " + toimipisteKoko);
    }
}
