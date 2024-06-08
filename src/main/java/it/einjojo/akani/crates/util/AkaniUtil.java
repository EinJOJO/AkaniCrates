package it.einjojo.akani.crates.util;

import it.einjojo.akani.core.api.AkaniCoreProvider;

public class AkaniUtil {
    public static boolean isAvailable() {
        try {
            Class.forName("it.einjojo.akani.core.api.AkaniCoreProvider");
            AkaniCoreProvider.get();
            return true;
        } catch (NoClassDefFoundError | Exception e) {
            return false;
        }
    }


}
