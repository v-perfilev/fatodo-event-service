package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ReadStatus;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

public class TestReadStatus extends ReadStatus {

    @Builder
    public TestReadStatus(UUID userId, Date lastReadAt) {
        super();
        super.setUserId(userId);
        super.setLastReadAt(lastReadAt);
    }

    public static TestReadStatusBuilder defaultBuilder() {
        return TestReadStatus.builder()
                .userId(UUID.randomUUID())
                .lastReadAt(new Date(0));
    }

    public ReadStatus toParent() {
        ReadStatus readStatus = new ReadStatus();
        readStatus.setUserId(getUserId());
        readStatus.setLastReadAt(getLastReadAt());
        return readStatus;
    }

}
