package com.nekoimi.standalone.framework.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>RowResult</p>
 *
 * @author nekoimi 2022/4/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RowResult<T> {
    private int index;
    private T result;
}
