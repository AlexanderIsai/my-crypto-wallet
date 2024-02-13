package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.exceptions.UserIsBlockedException;

/**
 * description
 *
 * @author Alexander Isai on 22.01.2024.
 */
public interface ActiveUserService {

    User getActiveUser();
}
