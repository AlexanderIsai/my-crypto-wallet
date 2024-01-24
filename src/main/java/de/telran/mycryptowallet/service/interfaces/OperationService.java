package de.telran.mycryptowallet.service.interfaces;

import de.telran.mycryptowallet.dto.OperationAddDTO;
import de.telran.mycryptowallet.entity.Operation;

/**
 * description
 *
 * @author Alexander Isai on 24.01.2024.
 */
public interface OperationService {

    void addOperation(OperationAddDTO operationAddDTO);


}
