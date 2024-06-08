package it.einjojo.akani.crates.crate.content;

public class CrateGiveRewardException extends Exception {
    private final String reason;

    public CrateGiveRewardException(Throwable throwable) {
        this.reason = "Fehler: " + throwable.fillInStackTrace();
    }

    public CrateGiveRewardException(String reason) {
        this.reason = reason;
    }
}
