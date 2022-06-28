package com.micropos.delivery.mapper;

import com.micropos.delivery.dto.DeliveryRecordDto;
import com.micropos.delivery.models.DeliveryRecord;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryMapper {
    DeliveryRecord toDeliveryRecord(DeliveryRecordDto deliveryDto);

    DeliveryRecordDto toDeliveryRecordDto(DeliveryRecord delivery);
}
