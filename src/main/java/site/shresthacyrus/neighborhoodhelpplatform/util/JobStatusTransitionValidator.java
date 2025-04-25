package site.shresthacyrus.neighborhoodhelpplatform.util;

import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;

public class JobStatusTransitionValidator {

    public static boolean isValidTransition(JobStatusEnum from, JobStatusEnum to) {
        return switch (from) {
            case OPEN -> to == JobStatusEnum.IN_PROGRESS || to == JobStatusEnum.CANCELED;
            case IN_PROGRESS -> to == JobStatusEnum.COMPLETED || to == JobStatusEnum.CANCELED;
            case COMPLETED, CANCELED -> false;
        };
    }
}