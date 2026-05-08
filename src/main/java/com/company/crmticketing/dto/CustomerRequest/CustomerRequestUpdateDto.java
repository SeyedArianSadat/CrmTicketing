package com.company.crmticketing.dto.CustomerRequest;

import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;

public record CustomerRequestUpdateDto (
        String title,
        String description,
        Channel channel,
        RequestStatus requestStatus,
        RequestType requestType
){}
