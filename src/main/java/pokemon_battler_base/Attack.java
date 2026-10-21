package pokemon_battler_base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Attack {
    private String namn;
    private int basskada;
    private int träffsäkerhet; // 1-100
    private Typ typ;

    @JsonCreator
    public Attack(
            @JsonProperty("namn") String namn,
            @JsonProperty("basskada") int basskada,
            @JsonProperty("träffsäkerhet") int träffsäkerhet,
            @JsonProperty("typ") Typ typ) {
        validera(namn, basskada, träffsäkerhet, typ);
        this.namn = namn;
        this.basskada = basskada;
        this.träffsäkerhet = träffsäkerhet;
        this.typ = typ;
    }

    private void validera(String namn, int basskada, int träffsäkerhet, Typ typ) {
        if (namn == null || namn.trim().isEmpty()) {
            throw new OgiltigDataUndantag("Attacknamn får inte vara tomt.");
        }
        if (basskada < 0) {
            throw new OgiltigDataUndantag("Basskada får inte vara negativ.");
        }
        if (träffsäkerhet < 1 || träffsäkerhet > 100) {
            throw new OgiltigDataUndantag("Träffsäkerhet måste vara mellan 1 och 100.");
        }
        if (typ == null) {
            throw new OgiltigDataUndantag("Typ måste anges.");
        }
    }

    public String getNamn() { return namn; }
    public int getBasskada() { return basskada; }
    public int getTräffsäkerhet() { return träffsäkerhet; }
    public Typ getTyp() { return typ; }
}
