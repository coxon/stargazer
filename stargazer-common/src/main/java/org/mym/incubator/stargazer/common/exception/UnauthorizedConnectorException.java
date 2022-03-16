package org.mym.incubator.stargazer.common.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * exception when create connector unauthorized
 *
 * @author coxon
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnauthorizedConnectorException extends StargazerException {

    private static final long serialVersionUID = 635985582937882538L;

    @Builder
    public UnauthorizedConnectorException(String message) {
        super(message);
        this.message = message;
    }

    @Builder
    public UnauthorizedConnectorException(String code, String message) {
        super(code, message);
    }
}
