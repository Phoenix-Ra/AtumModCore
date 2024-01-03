package me.phoenixra.atumodcore.api.placeholders;


import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderManager {
    /**
     * All registered placeholders.
     */
    private static final HashMap<AtumMod,
                Set<RegistrablePlaceholder>> REGISTERED_PLACEHOLDERS = new HashMap<>();

    /**
     * The default PlaceholderAPI pattern; brought in for compatibility.
     */
    private static final Pattern PATTERN = Pattern.compile("%([^% ]+)%");

    /**
     * Empty injectableContext object.
     */
    public static final InjectablePlaceholderList EMPTY_INJECTABLE = new InjectablePlaceholderList() {
        @Override
        public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void clearInjectedPlaceholders() {
            // Do nothing.
        }

        @Override
        public @NotNull
        List<InjectablePlaceholder> getPlaceholderInjections() {
            return Collections.emptyList();
        }
    };

    /**
     * Register a arguments.
     *
     * @param placeholder The arguments to register.
     */
    public static void registerPlaceholder(@NotNull final RegistrablePlaceholder placeholder) {
        if (!REGISTERED_PLACEHOLDERS.containsKey(placeholder.getAtumMod())) {
            REGISTERED_PLACEHOLDERS.put(placeholder.getAtumMod(), new HashSet<>());
        }
        REGISTERED_PLACEHOLDERS.get(placeholder.getAtumMod()).add(placeholder);
    }

    /**
     * Translate all placeholders without a placeholder context.
     *
     * @param text The text that may contain placeholders to translate.
     * @return The text, translated.
     */
    @NotNull
    public static String translatePlaceholders(@NotNull AtumMod atumMod,@NotNull final String text) {
        return translatePlaceholders(atumMod, text, PlaceholderContext.EMPTY);
    }

    /**
     * Translate all placeholders in a translation context.
     *
     * @param atumMod The mod that is translating the text.
     * @param text    The text that may contain placeholders to translate.
     * @param context The translation context.
     * @return The text, translated.
     */
    @NotNull
    public static String translatePlaceholders(@NotNull AtumMod atumMod,
                                               @NotNull final String text,
                                               @NotNull final PlaceholderContext context) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
        List<Future<PairRecord<String,String>>> futures = new ArrayList<>();

        for (String textToReplace : findPlaceholdersIn(text)) {
            Future<PairRecord<String,String>> future = executor.submit(() -> {
                for (InjectablePlaceholder placeholder : context.getInjectableContext().getPlaceholderInjections()) {
                    if (textToReplace.matches(placeholder.getPattern().pattern())) {
                        String replacement = placeholder.getValue(textToReplace, context);
                        if (replacement == null) return new PairRecord<>("","");
                        return new PairRecord<>(
                                textToReplace,
                                replacement
                        );
                    }
                }
                for (RegistrablePlaceholder placeholder : REGISTERED_PLACEHOLDERS.getOrDefault(atumMod, new HashSet<>())) {
                    if (textToReplace.matches(placeholder.getPattern().pattern())) {
                        String replacement = placeholder.getValue(textToReplace, context);
                        if (replacement == null) return new PairRecord<>("","");
                        return new PairRecord<>(
                                textToReplace,
                                replacement
                        );
                    }
                }
                return new PairRecord<>("","");
            });
            futures.add(future);
        }

        String translated = text;
        for (Future<PairRecord<String,String>> future : futures) {
            try {
                PairRecord<String,String> out = future.get();
                if(out.getFirst().isEmpty()) continue;
                translated = StringUtils.replaceFast(translated,
                        out.getFirst(),
                        out.getSecond()
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        return translated.toString();
    }

    /**
     * Find all placeholders in a given text.
     *
     * @param text The text.
     * @return The placeholders.
     */
    public static List<String> findPlaceholdersIn(@NotNull final String text) {
        Set<String> found = new HashSet<>();

        Matcher matcher = PATTERN.matcher(text);
        while (matcher.find()) {
            found.add(matcher.group());
        }

        return new ArrayList<>(found);
    }

    /**
     * Get all registered placeholders for a plugin.
     *
     * @param plugin The plugin.
     * @return The placeholders.
     */
    public static Set<RegistrablePlaceholder> getRegisteredPlaceholders(@NotNull final AtumMod plugin) {
        return REGISTERED_PLACEHOLDERS.get(plugin);
    }

    private PlaceholderManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
