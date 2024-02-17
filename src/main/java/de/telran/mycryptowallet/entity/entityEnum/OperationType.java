package de.telran.mycryptowallet.entity.entityEnum;

/**
 * Enumerates the types of operations that can be performed within the financial or trading system.
 * These operations represent the various actions that users or automated systems can initiate
 * regarding assets, currencies, or account balances. Each type specifies a distinct kind of
 * transaction or adjustment that affects the state or composition of a user's holdings or account.
 *
 * <ul>
 *   <li>{@code BUY} - Represents a purchase transaction where assets or currencies are acquired
 *       in exchange for payment. This operation increases the quantity of the specified asset
 *       or currency in the user's portfolio.</li>
 *   <li>{@code SELL} - Indicates a sale transaction where assets or currencies are sold in exchange
 *       for payment. This operation decreases the quantity of the specified asset or currency
 *       in the user's portfolio.</li>
 *   <li>{@code DEPOSIT} - Denotes adding funds to an account, increasing its balance. Deposits
 *       can be in the form of cash or other assets transferred into the account.</li>
 *   <li>{@code WITHDRAW} - Refers to the removal of funds from an account, decreasing its balance.
 *       Withdrawals can be in the form of cash or other assets taken out of the account.</li>
 *   <li>{@code ORDER} - Specifies an instruction to buy or sell that is placed within the system but
 *       not immediately executed. Orders can be set to trigger under specific conditions or at
 *       future dates/times.</li>
 * </ul>
 * @author Alexander Isai
 * @version 18.01.2024
 */
public enum OperationType {
    /**
     * Operation to buy or acquire assets or currencies.
     */
    BUY,

    /**
     * Operation to sell or dispose of assets or currencies.
     */
    SELL,

    /**
     * Operation to add funds or assets to an account.
     */
    DEPOSIT,

    /**
     * Operation to remove funds or assets from an account.
     */
    WITHDRAW,

    /**
     * A pending instruction for a buy or sell operation.
     */
    ORDER
}