package com.nekoimi.standalone;

import com.nekoimi.standalone.framework.devtools.FastCodeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * nekoimi  2022/2/17 11:35
 */
@SpringBootTest
public class FastCodeGeneratorTests {
    private final static String OUTPUT_DIR = "C:\\Users\\nekoimi\\Developer\\java_projects\\Nekoimi\\nk-standalone-framework\\src\\main\\java";
    @Autowired
    private FastCodeGenerator codeGenerator;

    @Test
    void testGenerator() {
        codeGenerator.execute(OUTPUT_DIR, "sys_file_info", "sys_");
    }
}
