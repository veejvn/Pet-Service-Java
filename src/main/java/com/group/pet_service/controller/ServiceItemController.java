package com.group.pet_service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group.pet_service.dto.serviceItemDto.ServiceItemDTO;
import com.group.pet_service.service.ServiceItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/service-items")
public class ServiceItemController {
	private final ServiceItemService serviceItemService;

	@Autowired
	public ServiceItemController(ServiceItemService serviceItemService) {
		this.serviceItemService = serviceItemService;
	}

	@PostMapping
	public ResponseEntity<ServiceItemDTO> createServiceItem(@Valid @RequestBody ServiceItemDTO serviceItemDTO) {
		ServiceItemDTO createdItem = serviceItemService.createServiceItem(serviceItemDTO);
		return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ServiceItemDTO> getServiceItemById(@PathVariable String id) {
		ServiceItemDTO serviceItem = serviceItemService.getServiceItemById(id);
		return serviceItem != null ? ResponseEntity.ok(serviceItem) : ResponseEntity.notFound().build();
	}

	/*
	 * Lấy trang đầu tiên với 10 mục: /api/v1/service-items?page=0&size=10 Lấy trang
	 * thứ hai với 20 mục và sắp xếp theo id giảm dần:
	 * /api/v1/service-items?page=1&size=20&sort=id,desc 
	 * Sắp xếp theo nhiều trường: api/v1/service-items?page=0&size=10&sort=name,asc&sort=price,desc
	 */
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllServiceItems(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id,asc") String[] sort) {

		try {
			List<Sort.Order> orders = new ArrayList<Sort.Order>();

			if (sort[0].contains(",")) {
				// will sort more than 2 fields
				// sortOrder="field, direction"
				for (String sortOrder : sort) {
					String[] _sort = sortOrder.split(",");
					orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
				}
			} else {
				// sort=[field, direction]
				orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
			}

			Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

			Page<ServiceItemDTO> pageServiceItems = serviceItemService.getAllServiceItems(pageable);

			List<ServiceItemDTO> serviceItems = pageServiceItems.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("serviceItems", serviceItems);
			response.put("currentPage", pageServiceItems.getNumber());
			response.put("totalItems", pageServiceItems.getTotalElements());
			response.put("totalPages", pageServiceItems.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		}

		return Sort.Direction.ASC;
	}

	@PutMapping("/{id}")
	public ResponseEntity<ServiceItemDTO> updateServiceItem(@PathVariable String id,
			@Valid @RequestBody ServiceItemDTO serviceItemDTO) {
		ServiceItemDTO updatedItem = serviceItemService.updateServiceItem(id, serviceItemDTO);
		return updatedItem != null ? ResponseEntity.ok(updatedItem) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteServiceItem(@PathVariable String id) {
		serviceItemService.deleteServiceItem(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/receipt/{receiptId}")
	public ResponseEntity<Page<ServiceItemDTO>> getServiceItemsByReceiptId(@PathVariable String receiptId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ServiceItemDTO> serviceItems = serviceItemService.getServiceItemsByReceiptId(receiptId, pageable);
		return ResponseEntity.ok(serviceItems);
	}

	@GetMapping("/staff/{staffId}")
	public ResponseEntity<Page<ServiceItemDTO>> getServiceItemsByStaffId(@PathVariable String staffId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ServiceItemDTO> serviceItems = serviceItemService.getServiceItemsByStaffId(staffId, pageable);
		return ResponseEntity.ok(serviceItems);
	}
}