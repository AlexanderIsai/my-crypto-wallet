package de.telran.mycryptowallet.mapper.operationMapper;

import de.telran.mycryptowallet.dto.operationDTO.OperationAddDTO;
import de.telran.mycryptowallet.entity.Operation;
import org.mapstruct.Mapper;

/**
 * description
 *
 * @author Alexander Isai on 04.03.2024.
 */
@Mapper(componentModel = "spring")
public interface OperationMapper {

    Operation toEntity(OperationAddDTO operationAddDTO);
}
