package me.phoenixra.atumodcore.api.misc;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public class CharacterFilter {
    public static CharacterFilter DOUBLE_FILTER = new CharacterFilter().addAllowedCharacters("0123456789.-+");
    public static CharacterFilter INTEGER_FILTER = new CharacterFilter().addAllowedCharacters("0123456789+-");
    public static CharacterFilter FILENAME_FILTER = new CharacterFilter().addAllowedCharacters(
            "abcdefghijklmnopqrstuvwxyz-_.");
    public static CharacterFilter FILENAME_FILTER_WITH_UPPERCASE = new CharacterFilter().addAllowedCharacters(
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.");
    public static CharacterFilter URL_FILTER = new CharacterFilter().addAllowedCharacters(
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~:/?#[]@!$&'()*+,;=\\");




    private List<Character> allowed = new ArrayList<>();
    private List<Character> forbidden = new ArrayList<>();

    public boolean isAllowed(char c) {
        if (!this.allowed.isEmpty()) {
            return this.allowed.contains(c);
        }
        return !this.forbidden.contains(c);
    }

    public boolean isAllowed(@NotNull String text) {
        if (text.length() < 1) {
            return true;
        }
        return this.isAllowed(text.charAt(0));
    }
    public @NotNull String filterForAllowedChars(@NotNull String text) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (this.isAllowed(text.charAt(i))) {
                s.append(text.charAt(i));
            }
        }
        return s.toString();
    }

    public CharacterFilter addAllowedCharacters(char... chars) {
        for (char c : chars) {
            if (!this.allowed.contains(c)) {
                this.allowed.add(c);
            }
        }
        return this;
    }

    public CharacterFilter addAllowedCharacters(@NotNull String charSet) {
        for (char character : charSet.toCharArray()) {
            if (!this.allowed.contains(character)) {
                this.allowed.add(character);
            }
        }
        return this;
    }

    public CharacterFilter addForbiddenCharacters(char... chars) {
        for (char c : chars) {
            if (!this.forbidden.contains(c)) {
                this.forbidden.add(c);
            }
        }
        return this;
    }

    public CharacterFilter addForbiddenCharacters(@NotNull String charSet) {
        for (char character : charSet.toCharArray()) {
            if (!this.forbidden.contains(character)) {
                this.forbidden.add(character);
            }
        }
        return this;
    }
}
