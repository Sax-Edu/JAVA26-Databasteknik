package pokemon_battler_base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pokemon {
    private String namn;
    private Typ typ;
    private int maxHälsa;
    private int nuvarandeHälsa;
    private int snabbhet;
    private List<Attack> attacker;

    @JsonCreator
    public Pokemon(
            @JsonProperty("namn") String namn,
            @JsonProperty("typ") Typ typ,
            @JsonProperty("maxHälsa") int maxHälsa,
            @JsonProperty("nuvarandeHälsa") int nuvarandeHälsa,
            @JsonProperty("snabbhet") int snabbhet,
            @JsonProperty("attacker") List<Attack> attacker) {

        List<Attack> säkradLista = attacker != null ? attacker : new ArrayList<>();
        validera(namn, typ, maxHälsa, säkradLista);

        this.namn = namn;
        this.typ = typ;
        this.maxHälsa = maxHälsa;
        this.nuvarandeHälsa = Math.min(Math.max(0, nuvarandeHälsa), maxHälsa);
        this.snabbhet = snabbhet <= 0 ? 10 : snabbhet;
        this.attacker = new ArrayList<>(säkradLista);
    }

    private void validera(String namn, Typ typ, int maxHälsa, List<Attack> attacker) {
        if (namn == null || namn.trim().isEmpty()) {
            throw new OgiltigDataUndantag("Namn får inte vara tomt.");
        }
        if (typ == null) {
            throw new OgiltigDataUndantag("Typ måste anges.");
        }
        if (maxHälsa <= 0) {
            throw new OgiltigDataUndantag("Maxhälsa måste vara större än 0.");
        }
        if (attacker.size() < 1 || attacker.size() > 4) {
            throw new OgiltigDataUndantag("En Pokemon måste ha mellan 1 och 4 attacker.");
        }
    }

    public void taSkada(int skada) {
        this.nuvarandeHälsa = Math.max(0, this.nuvarandeHälsa - skada);
    }

    public void återställHälsa() {
        this.nuvarandeHälsa = this.maxHälsa;
    }

    public boolean ärBesegrad() {
        return this.nuvarandeHälsa <= 0;
    }

    // Getters & Setters
    public String getNamn() { return namn; }
    public void setNamn(String namn) { this.namn = namn; }
    public Typ getTyp() { return typ; }
    public void setTyp(Typ typ) { this.typ = typ; }
    public int getMaxHälsa() { return maxHälsa; }
    public void setMaxHälsa(int maxHälsa) { this.maxHälsa = maxHälsa; }
    public int getNuvarandeHälsa() { return nuvarandeHälsa; }
    public int getSnabbhet() { return snabbhet; }
    public List<Attack> getAttacker() { return attacker; }
    public void setAttacker(List<Attack> attacker) {
        if (attacker.size() >= 1 && attacker.size() <= 4) {
            this.attacker = attacker;
        }
    }
}
