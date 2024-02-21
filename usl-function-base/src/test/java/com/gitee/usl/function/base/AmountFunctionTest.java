package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.domain.Param;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmountFunctionTest {
    static USLRunner runner = new USLRunner();
    @Test
    void amount_to_capital_chi() {
        Object data = runner.run(new Param("amount_to_capital_chi(107000.53)")).getData();
        assertEquals("壹拾万零柒仟元伍角叁分", data);
    }

    @Test
    void amount_to_capital_eng() {
        Object data = runner.run(new Param("amount_to_capital_eng(107000.53)")).getData();
        assertEquals("SAY US DOLLARS ONE HUNDRED AND SEVEN THOUSAND AND CENTS FIFTY-THREE ONLY", data);
    }
}