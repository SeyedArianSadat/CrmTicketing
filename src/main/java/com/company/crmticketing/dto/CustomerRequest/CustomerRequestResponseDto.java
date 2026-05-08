package com.company.crmticketing.dto.CustomerRequest;

import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;

import java.util.List;

public record CustomerRequestResponseDto(
        Long requestId,
        String title,
        String description,
        Channel channel,
        RequestStatus requestStatus,
        RequestType requestType,
        int customerId,
        int ticketId,
        List<String> messages
) {
}
