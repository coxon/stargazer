package org.mym.incubator.stargazer.common.domain.message;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author coxon
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payload implements Serializable {

    private static final long serialVersionUID = 8541190408828921643L;
    
    byte[] body;
    
    
}
