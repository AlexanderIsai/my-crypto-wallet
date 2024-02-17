package de.telran.mycryptowallet.entity.entityEnum;

/**
 * Represents the possible statuses of users within the system. Each status indicates a different state
 * of user interaction or system-imposed restrictions, facilitating user management and system monitoring.
 * <ul>
 *   <li>{@code ONLINE} - Indicates that the user is currently active and connected to the system.</li>
 *   <li>{@code BLOCKED} - Represents a user who has been temporarily or permanently restricted from accessing
 *   the system due to policy violations or other reasons.</li>
 *   <li>{@code OFFLINE} - Denotes that the user is not currently active or connected to the system.</li>
 * </ul>
 * @author Alexander Isai
 * @version 16.01.2024
 */
public enum UserStatus {
    /**
     * User is currently active and connected to the system.
     */
    ONLINE,

    /**
     * User has been restricted from accessing the system, either temporarily or permanently.
     */
    BLOCKED,

    /**
     * User is not currently active or connected to the system.
     */
    OFFLINE
}
