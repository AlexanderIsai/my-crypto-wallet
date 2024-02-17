package de.telran.mycryptowallet.entity.entityEnum;

/**
 * Defines the various statuses that an order can have within the system. Each status reflects a different
 * stage in the lifecycle of an order, from creation to completion or cancellation, enabling efficient order
 * tracking and management.
 *
 * <ul>
 *   <li>{@code ACTIVE} - Indicates that the order is currently active and pending execution. This status
 *       is assigned to orders that have been placed but not yet fulfilled or cancelled.</li>
 *   <li>{@code CANCELLED} - Represents an order that has been cancelled by the user or the system before
 *       it could be executed. Cancelled orders are removed from the queue of active orders.</li>
 *   <li>{@code DONE} - Denotes an order that has been fully executed and completed. This status is
 *       assigned to orders for which all specified transactions have been successfully carried out.</li>
 *   <li>{@code AUTO} - Specifies an order that was automatically  executed by the system.</li>
 * </ul>
 * @author Alexander Isai
 * @version 18.01.2024
 */
public enum OrderStatus {
    /**
     * Order is currently active and awaiting execution.
     */
    ACTIVE,

    /**
     * Order has been cancelled prior to execution.
     */
    CANCELLED,

    /**
     * Order has been fully executed and completed.
     */
    DONE,

    /**
     * Order was automatically generated and executed by the system.
     */
    AUTO
}
