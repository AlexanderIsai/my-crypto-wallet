package de.telran.mycryptowallet.entity.entityEnum;

/**
 * Defines the roles available for users within the system. These roles are used to control access
 * and permissions across different parts of the application, ensuring that users can only perform
 * actions appropriate to their assigned role.
 * <ul>
 *   <li>{@code ROLE_USER} - Standard user role with access to basic user functionalities.</li>
 *   <li>{@code ROLE_ADMIN} - Administrator role with extended permissions to manage the system.</li>
 * </ul>
 * The use of roles helps in implementing a robust security model within the application, allowing
 * for granular access control and ensuring that sensitive operations are protected from unauthorized access.
 * @author Alexander Isai
 * @version 18.01.2024
 */
public enum UserRole {
    /**
     * Represents a standard user with access to basic functionalities.
     */
    ROLE_USER,

    /**
     * Represents an administrator with extended permissions to perform system-wide operations.
     */
    ROLE_ADMIN
}
