package org.mym.incubator.stargazer.common.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * exception when start connector failed
 *
 * @author coxon
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartConnectorException extends StargazerException {

    private static final long serialVersionUID = 635985582937882538L;

    @Builder
    public StartConnectorException(String message) {
        super(message);
        this.message = message;
    }

    @Builder
    public StartConnectorException(String code, String message) {
        super(code, message);
    }
}
