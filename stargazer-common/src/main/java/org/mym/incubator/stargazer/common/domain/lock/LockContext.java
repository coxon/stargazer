package org.mym.incubator.stargazer.common.domain.lock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

/**
 * @author coxon
 */
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LockContext<T> {

    @NonNull
    final T lock;
}
