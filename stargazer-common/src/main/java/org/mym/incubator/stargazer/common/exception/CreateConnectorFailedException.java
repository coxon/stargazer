package org.mym.incubator.stargazer.common.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * exception when create connector failed
 *
 * @author coxon
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateConnectorFailedException extends StargazerException {

    private static final long serialVersionUID = 635985582937882538L;

    @Builder
    public CreateConnectorFailedException(String message) {
        super(message);
        this.message = message;
    }

    @Builder
    public CreateConnectorFailedException(String code, String message) {
        super(code, message);
    }
}
