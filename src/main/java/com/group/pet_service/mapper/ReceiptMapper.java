package com.group.pet_service.mapper;

import com.group.pet_service.dto.staff.StaffResponse;
import com.group.pet_service.dto.receipt.ReceiptResponse;
import com.group.pet_service.dto.user.UserResponse;
import com.group.pet_service.model.Receipt;
import com.group.pet_service.model.PetServiceItem;
import com.group.pet_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    @Mapping(source = "jobPosition.name", target = "jobPosition")
    UserResponse toUserResponse(User user);

    @Mapping(source = "jobPosition.name", target = "jobPosition")
    StaffResponse toStaffResponse(User user);

    ReceiptResponse.PetServiceItemDTO toServiceItemDTO(PetServiceItem petServiceItem);

    ReceiptResponse toReceiptResponse(Receipt receipt);

    default Page<ReceiptResponse> toPetServiceResponsePage(Page<Receipt> receiptPage) {
        List<ReceiptResponse> receiptResponses = receiptPage.getContent().stream()
                .map(this::toReceiptResponse).toList();
        return new PageImpl<>(receiptResponses, receiptPage.getPageable(), receiptPage.getTotalPages());
    }

    List<ReceiptResponse> toListReceiptResponse(List<Receipt> receipts);
}
