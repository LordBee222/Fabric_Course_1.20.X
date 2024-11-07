package net.mac.mccourse.util;

import java.util.UUID;

public interface ThrownByEntity {
    UUID getThrowerId();
    void setThrowerId(UUID throwerId);
}
