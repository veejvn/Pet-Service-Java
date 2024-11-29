package com.group.pet_service.mapper;

import com.group.pet_service.dto.pet_service.PetServiceResponse;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.model.PetService;
import com.group.pet_service.model.Receipt;
import com.group.pet_service.model.ServiceItem;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    ReceiptResponse.ServiceItemDTO toServiceItemDTO(ServiceItem serviceItem);

    ReceiptResponse toReceiptResponse(Receipt receipt);

    default Page<ReceiptResponse> toPetServiceResponsePage(Page<Receipt> receiptPage) {
        List<ReceiptResponse> receiptResponses = receiptPage.getContent().stream()
                .map(this::toReceiptResponse).toList();
        return new PageImpl<>(receiptResponses, receiptPage.getPageable(), receiptPage.getTotalPages());
    }
}
