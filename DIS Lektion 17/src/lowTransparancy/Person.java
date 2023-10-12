package lowTransparancy;

public class Person {
    String Fornavn;
    String Navn;
    String Koen;
    int Loen;
    String Postnr;
    /**

     */
    public String getFornavn() {
        return Fornavn;
    }
    /**

     */
    public void setFornavn(String fornavn){
        Fornavn = fornavn;
    }
    /**
     * @return Returns the loen.
     */
    public int getLoen() {
        return Loen;
    }
    /**
     * @param loen The loen to set.
     */
    public void setLoen(int loen) {
        Loen = loen;
    }
    /**
     * @return Returns the navn.
     */
    public String getNavn() {
        return Navn;
    }
    /**
     * @param navn The navn to set.
     */
    public void setNavn(String navn) {
        Navn = navn;
    }
    /**
     */
    public String getKoen() {
        return Koen;
    }
    /**

     */
    public void setKoen(String koen) {
        Koen = koen;
    }
    public String getPostnr() {
        return Postnr;
    }
    /**

     */
    public void setPostnr(String postnr){
        Postnr = postnr;
    }

}
