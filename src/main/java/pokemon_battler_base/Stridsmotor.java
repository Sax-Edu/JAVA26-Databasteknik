package pokemon_battler_base;

import java.util.Random;
import java.util.Scanner;

public class Stridsmotor {
    private final Random slump = new Random();

    public boolean utförStrid(Pokemon spelare, Pokemon motståndare, Scanner scanner, StridsStatistik stats) {
        System.out.println("\n=== STRIDEN STARTAR ===");
        System.out.println(spelare.getNamn() + " möter vild " + motståndare.getNamn() + "!");

        spelare.återställHälsa();
        motståndare.återställHälsa();

        while (!spelare.ärBesegrad() && !motståndare.ärBesegrad()) {
            System.out.println("\n--- Status ---");
            System.out.printf("%s HP: %d/%d | %s HP: %d/%d\n",
                    spelare.getNamn(), spelare.getNuvarandeHälsa(), spelare.getMaxHälsa(),
                    motståndare.getNamn(), motståndare.getNuvarandeHälsa(), motståndare.getMaxHälsa());

            boolean spelareSnabbare = spelare.getSnabbhet() >= motståndare.getSnabbhet();

            if (spelareSnabbare) {
                utförSpelarTur(spelare, motståndare, scanner, stats);
                if (motståndare.ärBesegrad()) break;
                utförDatorTur(motståndare, spelare);
            } else {
                utförDatorTur(motståndare, spelare);
                if (spelare.ärBesegrad()) break;
                utförSpelarTur(spelare, motståndare, scanner, stats);
            }
        }

        boolean spelareVann = !spelare.ärBesegrad();
        if (spelareVann) {
            System.out.println("\n Grattis! Du vann striden!");
        } else {
            System.out.println("\n Du förlorade striden...");
        }

        stats.registreraStrid(spelare.getNamn(), spelareVann);
        return spelareVann;
    }

    private void utförSpelarTur(Pokemon spelare, Pokemon motståndare, Scanner scanner, StridsStatistik stats) {
        System.out.println("\nVälj din attack:");
        for (int i = 0; i < spelare.getAttacker().size(); i++) {
            Attack a = spelare.getAttacker().get(i);
            System.out.printf("%d. %s (Skada: %d, Typ: %s, Träffsäkerhet: %d%%)\n",
                    (i + 1), a.getNamn(), a.getBasskada(), a.getTyp(), a.getTräffsäkerhet());
        }

        int val = -1;
        while (val < 0 || val >= spelare.getAttacker().size()) {
            System.out.print("Ange val (1-" + spelare.getAttacker().size() + "): ");
            String input = scanner.nextLine();
            try {
                val = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt val, försök igen.");
            }
        }

        Attack valdaAttack = spelare.getAttacker().get(val);
        stats.registreraAttackAnvändning(valdaAttack.getNamn());
        utskada(spelare, motståndare, valdaAttack);
    }

    private void utförDatorTur(Pokemon dator, Pokemon spelare) {
        Attack slumpadAttack = dator.getAttacker().get(slump.nextInt(dator.getAttacker().size()));
        utskada(dator, spelare, slumpadAttack);
    }

    private void utskada(Pokemon anfallare, Pokemon försvarare, Attack attack) {
        System.out.println("\n" + anfallare.getNamn() + " använder " + attack.getNamn() + "!");

        // Träffchans
        if (slump.nextInt(100) + 1 > attack.getTräffsäkerhet()) {
            System.out.println("...men attacken missade!");
            return;
        }

        // Typeffektivitet
        double effektivitet = attack.getTyp().beräknaEffektivitetMot(försvarare.getTyp());
        if (effektivitet > 1.0) System.out.println("Det är super-effektivt!");
        else if (effektivitet < 1.0) System.out.println("Det var inte särskilt effektivt...");

        // Kritiskt hugg (1/16 chanser)
        boolean kritisk = slump.nextInt(16) == 0;
        double kritiskMultiplikator = kritisk ? 2.0 : 1.0;
        if (kritisk) System.out.println("Kritisk träff!");

        // Slumpfaktor (0.85 - 1.15)
        double slumpVariabel = 0.85 + (slump.nextDouble() * 0.30);

        int slutgiltigSkada = (int) Math.max(1, attack.getBasskada() * effektivitet * kritiskMultiplikator * slumpVariabel);
        försvarare.taSkada(slutgiltigSkada);

        System.out.printf("%s tog %d skada!\n", försvarare.getNamn(), slutgiltigSkada);
    }
}
