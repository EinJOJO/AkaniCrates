package it.einjojo.akani.crates.util;

import it.einjojo.akani.core.api.AkaniCoreProvider;

public class AkaniUtil {
    public static boolean isAvailable() {
        try {
            AkaniCoreProvider.get();
            return true;
        } catch (NoClassDefFoundError | AkaniCoreProvider.NotLoadedException e) {
            return false;
        }
    }



}
