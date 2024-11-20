package com.group.pet_service.mapper;

import com.group.pet_service.dto.request.ReceiptCreateRequest;
import com.group.pet_service.dto.response.ReceiptResponse;
import com.group.pet_service.model.Receipt;
import com.group.pet_service.model.ServiceItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    ReceiptResponse.ServiceItemDTO toServiceItemDTO(ServiceItem serviceItem);

    ReceiptResponse toReceiptResponse(Receipt receipt);
}
