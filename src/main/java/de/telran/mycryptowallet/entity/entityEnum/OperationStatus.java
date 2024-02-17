package de.telran.mycryptowallet.entity.entityEnum;

/**
 * Enumerates the possible statuses of financial or trading operations within the system.
 * These statuses reflect the current state of an operation, such as a trade, deposit, or withdrawal,
 * and are crucial for tracking the progress and outcome of each operation. Understanding the status
 * of an operation helps users and the system itself to make informed decisions based on the operation's
 * completion and success.
 *
 * <ul>
 *   <li>{@code IN_PROCESS} - Indicates that the operation is currently underway. This status is applied
 *       to operations that have been initiated but have not yet reached completion. Operations in this
 *       state are actively being processed by the system.</li>
 *   <li>{@code DONE} - Signifies that the operation has been successfully completed. Operations marked
 *       as DONE have been fully executed according to the specified parameters and have resulted in a
 *       change to the account or portfolio as intended.</li>
 *   <li>{@code CANCELLED} - Denotes that the operation has been cancelled before completion. Cancelled
 *       operations may be terminated by the user or the system due to various reasons, such as changing
 *       market conditions, user request, or failure to meet certain criteria. No changes associated with
 *       the operation are applied to the account or portfolio.</li>
 * </ul>
 * @author Alexander Isai
 * @version 25.01.2024
 */
public enum OperationStatus {
    /**
     * Operation is currently being processed.
     */
    IN_PROCESS,

    /**
     * Operation has been successfully completed.
     */
    DONE,

    /**
     * Operation has been cancelled prior to completion.
     */
    CANCELLED
}
