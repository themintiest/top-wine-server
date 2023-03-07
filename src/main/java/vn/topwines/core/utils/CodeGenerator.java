package vn.topwines.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeGenerator {

    public static String generateObjectCode(String input) {
        String code = StringUtils.stripAccents(input.toLowerCase().trim());
        return code.replaceAll(" ", "-");
    }
}
