package pokemon_battler_base;

public enum Typ {
    ELD,
    VATTEN,
    GRÄS,
    ELEKTRICITET,
    NORMAL;

    public double beräknaEffektivitetMot(Typ försvarande) {
        if (this == ELD && försvarande == GRÄS) return 2.0;
        if (this == VATTEN && försvarande == ELD) return 2.0;
        if (this == GRÄS && försvarande == VATTEN) return 2.0;
        if (this == ELEKTRICITET && försvarande == VATTEN) return 2.0;

        if (this == ELD && försvarande == VATTEN) return 0.5;
        if (this == VATTEN && försvarande == GRÄS) return 0.5;
        if (this == GRÄS && försvarande == ELD) return 0.5;

        return 1.0; // Normal eller standard
    }
}
