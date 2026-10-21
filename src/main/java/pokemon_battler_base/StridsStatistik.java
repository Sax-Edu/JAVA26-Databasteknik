package pokemon_battler_base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class StridsStatistik {
    private int totalaVinster;
    private int totalaFörluster;
    private Map<String, Integer> PokemonVinster = new HashMap<>();
    private Map<String, Integer> PokemonFörluster = new HashMap<>();
    private Map<String, Integer> attackAnvändning = new HashMap<>();
    private Map<String, Integer> PokemonAnvändning = new HashMap<>();

    @JsonCreator
    public StridsStatistik(
            @JsonProperty("totalaVinster") int totalaVinster,
            @JsonProperty("totalaFörluster") int totalaFörluster,
            @JsonProperty("PokemonVinster") Map<String, Integer> PokemonVinster,
            @JsonProperty("PokemonFörluster") Map<String, Integer> PokemonFörluster,
            @JsonProperty("attackAnvändning") Map<String, Integer> attackAnvändning,
            @JsonProperty("PokemonAnvändning") Map<String, Integer> PokemonAnvändning) {
        this.totalaVinster = totalaVinster;
        this.totalaFörluster = totalaFörluster;
        if (PokemonVinster != null) this.PokemonVinster = PokemonVinster;
        if (PokemonFörluster != null) this.PokemonFörluster = PokemonFörluster;
        if (attackAnvändning != null) this.attackAnvändning = attackAnvändning;
        if (PokemonAnvändning != null) this.PokemonAnvändning = PokemonAnvändning;
    }

    public StridsStatistik() {}

    public void registreraStrid(String PokemonNamn, boolean vinst) {
        PokemonAnvändning.put(PokemonNamn, PokemonAnvändning.getOrDefault(PokemonNamn, 0) + 1);
        if (vinst) {
            totalaVinster++;
            PokemonVinster.put(PokemonNamn, PokemonVinster.getOrDefault(PokemonNamn, 0) + 1);
        } else {
            totalaFörluster++;
            PokemonFörluster.put(PokemonNamn, PokemonFörluster.getOrDefault(PokemonNamn, 0) + 1);
        }
    }

    public void registreraAttackAnvändning(String attackNamn) {
        attackAnvändning.put(attackNamn, attackAnvändning.getOrDefault(attackNamn, 0) + 1);
    }

    // Getters
    public int getTotalaVinster() { return totalaVinster; }
    public int getTotalaFörluster() { return totalaFörluster; }
    public Map<String, Integer> getPokemonVinster() { return PokemonVinster; }
    public Map<String, Integer> getPokemonFörluster() { return PokemonFörluster; }
    public Map<String, Integer> getAttackAnvändning() { return attackAnvändning; }
    public Map<String, Integer> getPokemonAnvändning() { return PokemonAnvändning; }
}
