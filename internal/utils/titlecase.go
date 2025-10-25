package utils

import (
    "strings"
    "unicode"
)

func ToTitleCase(str string) string {
    words := strings.Fields(str) 
    for i, word := range words {
        if len(word) == 0 {
            continue
        }
        runes := []rune(word)

        runes[0] = unicode.ToUpper(runes[0])
        
        for j := 1; j < len(runes); j++ {
            runes[j] = unicode.ToLower(runes[j])
        }
        words[i] = string(runes)
    }
    return strings.Join(words, " ")
}
