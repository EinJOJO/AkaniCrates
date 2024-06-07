package it.einjojo.akani.crates.crate.content;

/**
 * Crate content that gives the player a certain amount of economy value
 */
public interface EconomyCrateContent extends CrateContent {
    int economyAmount();

    String currencyName();
}
