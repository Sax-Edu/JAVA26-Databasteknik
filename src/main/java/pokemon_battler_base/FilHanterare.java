package pokemon_battler_base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilHanterare {
    private static final String POKEMON_FIL = "Pokemon.json";
    private static final String STATS_FIL = "statistik.json";
    private final ObjectMapper mapper;

    public FilHanterare() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void sparaPokemon(List<Pokemon> lista) {
        try {
            mapper.writeValue(new File(POKEMON_FIL), lista);
        } catch (IOException e) {
            System.err.println("Fel vid sparande av Pokemon: " + e.getMessage());
        }
    }

    public List<Pokemon> laddaPokemon() {
        File fil = new File(POKEMON_FIL);
        if (!fil.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(fil, mapper.getTypeFactory().constructCollectionType(List.class, Pokemon.class));
        } catch (Exception e) {
            System.err.println("Korrupt eller ogiltig fil. Startar med tom/standard samling.");
            return new ArrayList<>();
        }
    }

    public void sparaStatistik(StridsStatistik stats) {
        try {
            mapper.writeValue(new File(STATS_FIL), stats);
        } catch (IOException e) {
            System.err.println("Fel vid sparande av statistik: " + e.getMessage());
        }
    }

    public StridsStatistik laddaStatistik() {
        File fil = new File(STATS_FIL);
        if (!fil.exists()) return new StridsStatistik();
        try {
            return mapper.readValue(fil, StridsStatistik.class);
        } catch (Exception e) {
            return new StridsStatistik();
        }
    }
}
