package net.ubn.td.package_web.util;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

/** Utility for generating UUID v7 identifiers. */
public final class UuidUtils {
    private UuidUtils() {}

    /**
     * Generates a new UUID v7 as defined by the uuid-creator library.
     */
    public static UUID newUuid() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
