package com.gdgnn.filatov.kriya.utils;

import java.util.List;

public class StringUtils {

    public static String join(String delimiter, List<String> chunks) {
        Integer chunksCount = chunks.size();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chunksCount; i++) {
            sb.append(chunks.get(i));
            if (i != chunksCount - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }
}
