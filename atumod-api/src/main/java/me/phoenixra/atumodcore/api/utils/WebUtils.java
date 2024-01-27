package me.phoenixra.atumodcore.api.utils;
import me.phoenixra.atumodcore.api.misc.CharacterFilter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class WebUtils {

    /**
     * Checks if the given url is valid.
     * @param url the url to check
     * @return true if the url is valid, false otherwise
     */
    public static boolean isValidUrl(String url) {
        if ((url == null) || (!url.startsWith("http://") && !url.startsWith("https://"))) {
            return false;
        }

        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.addRequestProperty("User-Agent", "Mozilla/4.0");

            c.setRequestMethod("HEAD");
            int r = c.getResponseCode();

            if (r == 200) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Unable to check for valid url via HEAD request!");
            System.out.println("Trying alternative method..");
            try {
                URL u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.addRequestProperty("User-Agent", "Mozilla/4.0");

                int r = c.getResponseCode();

                if (r == 200) {
                    return true;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Gets the plain text content of a web page.
     * @param webLink the url to get the content from
     * @return the plain text content of the web page
     */
    public static List<String> getPlainTextContentOfPage(URL webLink) {
        List<String> l = new ArrayList<>();
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(webLink.openStream(), StandardCharsets.UTF_8));
            String s = r.readLine();
            while(s != null) {
                l.add(s);
                s = r.readLine();
            }
            r.close();
        } catch (Exception e) {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            l.clear();
        }
        return l;
    }

    public static String filterURL(@NotNull String url) {
       return CharacterFilter.URL_FILTER.filterForAllowedChars(url);
    }
}
