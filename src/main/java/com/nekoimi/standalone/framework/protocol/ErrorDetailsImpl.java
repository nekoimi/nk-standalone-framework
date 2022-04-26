package com.nekoimi.standalone.framework.protocol;

import com.nekoimi.standalone.framework.error.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * nekoimi  2021/12/13 22:35
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ErrorDetailsImpl implements ErrorDetails {
    private Integer code;
    private String msg;
    private String trace;

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return msg;
    }

    @Override
    public String trace() {
        return trace;
    }
}
