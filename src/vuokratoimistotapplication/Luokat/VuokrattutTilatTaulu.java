package vuokratoimistotapplication.Luokat;

import java.util.Date;

/**
 * VuokrattutTilat luokka
 * @author hoang
 */
public class VuokrattutTilatTaulu {
    /**
     * Vuokrattut tilat attribuuttit
     */
    private String toimipisteNimi; /** Toimipiste nimi */
    private int toimipisteID; /** Toimipiste numero*/
    private Date aloitusPaiva;/**Aloituspäivä*/
    private Date lopetusPaiva; /** Lopetuspäivä*/
    private String etunimi; /** Asiakas etunimi*/
    private String sukunimi; /**Asiakas sukunimi*/
    private String yritys; /**Asiakas yrityksen nimi*/
    private int summa;

    
/** Lasku summa*/
    
    /**
     * Tyhjä oletus konstruktori
     */
    public VuokrattutTilatTaulu() {
    }
    
    /**
     * Vuokrattut tilat konstruktori joka saa parametreina kaikki varausten tiedot.
     * @param toimipisteNimi Toimipiste nimi
     * @param toimipisteID Toimipiste numero
     * @param sukunimi Asiakkaan sukunimi
     * @param yritys Asiakkaan yrityksen nimi
     * @param etunimi  Asiakkaan etunimi
     * @param aloitusPaiva Aloituspäivä
     * @param lopetusPaiva Lopetuspäivä
     * @param summa  Lasku summa
     */
    public VuokrattutTilatTaulu(String toimipisteNimi, int toimipisteID, Date aloitusPaiva, Date lopetusPaiva, String etunimi, String sukunimi, String yritys, int summa) {
        this.toimipisteNimi = toimipisteNimi;
        this.toimipisteID = toimipisteID;
        this.aloitusPaiva = aloitusPaiva;
        this.lopetusPaiva = lopetusPaiva;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.yritys = yritys;
        this.summa = summa;
    }
   
    /**
     * Palauttaa toimipiste nimi
     * @return toimipisteNimi
     */
    public String getToimipisteNimi() {
        return toimipisteNimi;
    }
    
    /**
     * Asettaa toimipiste nimi
     * @param toimipisteNimi Toimipiste nimi
     */
    public void setToimipisteNimi(String toimipisteNimi) {
        this.toimipisteNimi = toimipisteNimi;
    }
    
    /**
     * Palauttaa toimipiste numero
     * @return toimipisteID
     */
    public int getToimipisteID() {
        return toimipisteID;
    }

    /**
     * Asettaa toimipiste numero
     * @param toimipisteID Toimipiste numero
     */
    public void setToimipisteID(int toimipisteID) {
        this.toimipisteID = toimipisteID;
    }

    /**
     * Palauttaa aloituspäivä
     * @return aloitusPaiva
     */
    public Date getAloitusPaiva() {
        return aloitusPaiva;
    }

    /**
     * Asettaa aloituspäivä
     * @param aloitusPaiva Aloituspäivä
     */
    public void setAloitusPaiva(Date aloitusPaiva) {
        this.aloitusPaiva = aloitusPaiva;
    }
    
    /**
     * Palauttaa lopetuspäivä
     * @return lopetusPaiva 
     */
    public Date getLopetusPaiva() {
        return lopetusPaiva;
    }
    
    /**
     * Asettaa lopetuspäivä
     * @param lopetusPaiva  Lopetuspäivä
     */
    public void setLopetusPaiva(Date lopetusPaiva) {
        this.lopetusPaiva = lopetusPaiva;
    }
    
    /**
     * Palauttaa asiakkaan etunimi
     * @return etunimi
     */
    public String getEtunimi() {
        return etunimi;
    }
    
    /**
     * Asettaa asiakkaan etunimi
     * @param etunimi Etunimi
     */
    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }
    
    /**
     * Palauttaa asiakkaan sukunimi
     * @return sukunimi
     */
    public String getSukunimi() {
        return sukunimi;
    }
    
    /**
     * Asettaa asiakkaan sukunimi
     * @param sukunimi Sukunimi
     */
    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }
    
    /**
     * Palauttaa asiakkaan yrityksen nimi
     * @return yritys
     */
    public String getYritys() {
        return yritys;
    }
    
    /**
     * Asettaa asiakkaan yrityksen nimi
     * @param yritys Yrityksen nimi
     */
    public void setYritys(String yritys) {
        this.yritys = yritys;
    }

    /**
     * Palauttaa laskun summa
     * @return summa laskun summa
     */
    public int getSumma() {
        return summa;
    }
    
    /**
     * Asettaa laskusumma
     * @param summa laksu summa
     */
    public void setSumma(int summa){
    this.summa = summa;
    }
    
    @Override
    public String toString() {
        return "VuokrattutTilatTaulu {" + " toimipisteNimi=" + toimipisteNimi + ", toimipisteID=" 
                + toimipisteID + ",  aloitusPaiva=" + aloitusPaiva + ", lopetusPaiva=" 
                + lopetusPaiva + ", etunimi=" + etunimi + ", sukunimi=" + sukunimi + ", yritys=" 
                + yritys + ", summa=" + summa + '}';
    }

}
