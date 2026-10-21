package pokemon_battler_base;

import java.util.*;

public class Huvudprogram {
    private final List<Pokemon> pokemons = new ArrayList<>();
    private final FilHanterare filHanterare = new FilHanterare();
    private final Stridsmotor stridsmotor = new Stridsmotor();
    private StridsStatistik statistik = new StridsStatistik();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Huvudprogram().starta();
    }

    public void starta() {
        laddaData();
        if (pokemons.isEmpty()) {
            återställTillStandarddata();
        }

        boolean igång = true;
        while (igång) {
            visaMeny();
            String val = scanner.nextLine().trim();

            switch (val) {
                case "1" -> visaAllaPokemon();
                case "2" -> läggTillPokemon();
                case "3" -> redigeraPokemon();
                case "4" -> taBortPokemon();
                case "5" -> startaStrid();
                case "6" -> visaStatistik();
                case "7" -> sparaData();
                case "8" -> återställTillStandarddata();
                case "0" -> {
                    sparaData();
                    System.out.println("Programmet avslutas och data har sparats. Välkommen åter!");
                    igång = false;
                }
                default -> System.out.println("Ogiltigt menyval. Försök igen.");
            }
        }
    }

    private void visaMeny() {
        System.out.println("\n========== POKÉMON BATTLER ==========");
        System.out.println("1. Visa alla Pokémons");
        System.out.println("2. Lägg till ny Pokémon");
        System.out.println("3. Redigera Pokémon");
        System.out.println("4. Ta bort Pokémon");
        System.out.println("5. Starta strid");
        System.out.println("6. Visa stridsstatistik (VG)");
        System.out.println("7. Spara manuellt");
        System.out.println("8. Återställ till standarddata (Seeding)");
        System.out.println("0. Avsluta");
        System.out.print("Välj ett alternativ: ");
    }

    private void visaAllaPokemon() {
        if (pokemons.isEmpty()) {
            System.out.println("Inga Pokémons finns registrerade.");
            return;
        }
        System.out.println("\n--- Pokédex Samling ---");
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon v = pokemons.get(i);
            System.out.printf("[%d] %s (Typ: %s, Max HP: %d, Snabbhet: %d, Attacker: %d st)\n",
                    (i + 1), v.getNamn(), v.getTyp(), v.getMaxHälsa(), v.getSnabbhet(), v.getAttacker().size());
        }
    }

    private void läggTillPokemon() {
        try {
            System.out.print("Ange namn: ");
            String namn = scanner.nextLine();

            System.out.println("Välj typ (ELD, VATTEN, GRÄS, ELEKTRICITET, NORMAL): ");
            Typ typ = Typ.valueOf(scanner.nextLine().toUpperCase().trim());

            System.out.print("Ange Max HP: ");
            int maxHp = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Ange Snabbhet: ");
            int snabbhet = Integer.parseInt(scanner.nextLine().trim());

            List<Attack> attacker = new ArrayList<>();
            attacker.add(skapaAttackViaKonsol());

            Pokemon ny = new Pokemon(namn, typ, maxHp, maxHp, snabbhet, attacker);
            pokemons.add(ny);
            System.out.println("Pokémon lades till framgångsrikt!");
        } catch (IllegalArgumentException e) {
            System.out.println("Felaktig typ eller sifferinmatning.");
        } catch (OgiltigDataUndantag e) {
            System.out.println("Valideringsfel: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Gick inte att skapa Pokémon på grund av ogiltig indata.");
        }
    }

    private Attack skapaAttackViaKonsol() {
        System.out.print("Ange attacknamn: ");
        String aNamn = scanner.nextLine();
        System.out.print("Ange basskada: ");
        int skada = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Ange träffsäkerhet (1-100): ");
        int träff = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Ange typ för attacken: ");
        Typ aTyp = Typ.valueOf(scanner.nextLine().toUpperCase().trim());

        return new Attack(aNamn, skada, träff, aTyp);
    }

    private void redigeraPokemon() {
        visaAllaPokemon();
        if (pokemons.isEmpty()) return;

        try {
            System.out.print("Välj index att redigera: ");
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= pokemons.size()) throw new SaknadResursUndantag("Index finns inte.");

            Pokemon v = pokemons.get(index);
            System.out.print("Nytt namn (lämna tomt för att behålla '" + v.getNamn() + "'): ");
            String nyttNamn = scanner.nextLine();
            if (!nyttNamn.trim().isEmpty()) v.setNamn(nyttNamn);

            System.out.print("Ny Max HP (0 för att behålla " + v.getMaxHälsa() + "): ");
            String hpInput = scanner.nextLine().trim();
            if (!hpInput.isEmpty() && Integer.parseInt(hpInput) > 0) {
                v.setMaxHälsa(Integer.parseInt(hpInput));
            }

            System.out.println("Uppdatering genomförd!");
        } catch (Exception e) {
            System.out.println("Fel vid redigering: " + e.getMessage());
        }
    }

    private void taBortPokemon() {
        visaAllaPokemon();
        if (pokemons.isEmpty()) return;

        try {
            System.out.print("Välj index att ta bort: ");
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= pokemons.size()) throw new SaknadResursUndantag("Finns ingen Pokemon med det indexet.");

            Pokemon borttagen = pokemons.remove(index);
            System.out.println(borttagen.getNamn() + " raderades.");
        } catch (Exception e) {
            System.out.println("Kunde inte ta bort: " + e.getMessage());
        }
    }

    private void startaStrid() {
        if (pokemons.isEmpty()) {
            System.out.println("Du måste äga minst en Pokémon för att kunna strida!");
            return;
        }

        try {
            visaAllaPokemon();
            System.out.print("Välj din Pokémon för striden: ");
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (index < 0 || index >= pokemons.size()) {
                System.out.println("Ogiltigt Pokémon-val.");
                return;
            }

            Pokemon spelare = pokemons.get(index);

            if (spelare.getAttacker().isEmpty()) {
                System.out.println("Denna Pokémon saknar attacker och kan inte strida!");
                return;
            }

            // Slumpa en vild motståndare från listan
            Random r = new Random();
            Pokemon mall = pokemons.get(r.nextInt(pokemons.size()));
            Pokemon motståndare = new Pokemon(
                    "Vild " + mall.getNamn(),
                    mall.getTyp(),
                    mall.getMaxHälsa(),
                    mall.getMaxHälsa(),
                    mall.getSnabbhet(),
                    mall.getAttacker()
            );

            stridsmotor.utförStrid(spelare, motståndare, scanner, statistik);
            sparaData();
        } catch (Exception e) {
            System.out.println("Striden avbröts på grund av fel: " + e.getMessage());
        }
    }

    private void visaStatistik() {
        System.out.println("\n=== PERSISTENT STRIDSSTATISTIK ===");
        System.out.println("Totala vinster: " + statistik.getTotalaVinster());
        System.out.println("Totala förluster: " + statistik.getTotalaFörluster());

        // Mest använda Pokémon
        String mestAnvändPokemon = statistik.getPokemonAnvändning().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Ingen");
        System.out.println("Mest använda Pokémon: " + mestAnvändPokemon);

        // Mest använda attack
        String mestAnvändAttack = statistik.getAttackAnvändning().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Ingen");
        System.out.println("Mest använda attack: " + mestAnvändAttack);
    }

    private void sparaData() {
        filHanterare.sparaPokemon(pokemons);
        filHanterare.sparaStatistik(statistik);
        System.out.println("Data sparades till disk.");
    }

    private void laddaData() {
        pokemons.clear();
        pokemons.addAll(filHanterare.laddaPokemon());
        statistik = filHanterare.laddaStatistik();
    }

    private void återställTillStandarddata() {
        pokemons.clear();

        List<Attack> eldBolt = List.of(
                new Attack("Flamkastare", 40, 90, Typ.ELD),
                new Attack("Tackling", 20, 100, Typ.NORMAL)
        );
        List<Attack> vattenBolt = List.of(
                new Attack("Vattenkanon", 35, 95, Typ.VATTEN),
                new Attack("Bett", 22, 100, Typ.NORMAL)
        );
        List<Attack> gräsBolt = List.of(
                new Attack("Piskrappe", 38, 90, Typ.GRÄS),
                new Attack("Tackling", 20, 100, Typ.NORMAL)
        );
        List<Attack> elBolt = List.of(
                new Attack("Blixtstöt", 45, 85, Typ.ELEKTRICITET),
                new Attack("Snabbattack", 18, 100, Typ.NORMAL)
        );

        pokemons.add(new Pokemon("Elddrake", Typ.ELD, 100, 100, 12, eldBolt));
        pokemons.add(new Pokemon("Vattensköldpadda", Typ.VATTEN, 110, 110, 8, vattenBolt));
        pokemons.add(new Pokemon("Gräsgroda", Typ.GRÄS, 105, 105, 9, gräsBolt));
        pokemons.add(new Pokemon("Elmus", Typ.ELEKTRICITET, 90, 90, 15, elBolt));
        pokemons.add(new Pokemon("Stenelemental", Typ.NORMAL, 130, 130, 5, eldBolt));
        pokemons.add(new Pokemon("Vindfågel", Typ.NORMAL, 85, 85, 14, elBolt));

        sparaData();
        System.out.println("Standarddata har återställts och seedats med 6 Pokemon!");
    }
}