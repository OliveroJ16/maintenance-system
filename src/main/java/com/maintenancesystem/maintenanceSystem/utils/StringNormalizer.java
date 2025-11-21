package com.maintenancesystem.maintenanceSystem.utils;

import org.springframework.stereotype.Component;

@Component
public class StringNormalizer{

    public String toTitleCase(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }
        var normalizedText = new StringBuilder();
        boolean nextTitleCase = true;

        for (char character : text.trim().toLowerCase().toCharArray()) {
            if (Character.isSpaceChar(character)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                character = Character.toTitleCase(character);
                nextTitleCase = false;
            }
            normalizedText.append(character);
        }
        return normalizedText.toString();
    }
}