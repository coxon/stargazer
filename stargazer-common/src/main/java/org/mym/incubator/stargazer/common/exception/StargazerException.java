package org.mym.incubator.stargazer.common.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * @author coxon
 */
@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class StargazerException extends RuntimeException {

    private static final long serialVersionUID = 917099543296501414L;

    String code;

    String message;

    StargazerException() {}

    StargazerException(String message) {
        super(message);
        this.message = message;
    }

    StargazerException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
