package com.fluffy.universe.utils;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;

public final class VelocityHelper {
    private VelocityHelper() {}

    public static String generatePaginationLink(String base, Integer pageNumber, Integer pageSize) {
        try {
            URIBuilder uri = new URIBuilder(base);
            if (pageNumber != null) {
                uri.addParameter("page", String.valueOf(pageNumber));
            }
            if (pageSize != null) {
                uri.addParameter("size", String.valueOf(pageSize));
            }
            return uri.toString();
        } catch (URISyntaxException ignored) {
            return "";
        }
    }

    public static int calculateMaxPageNumber(int recordCount, int pageSize) {
        return (int) Math.round(Math.ceil(recordCount / (double) pageSize));
    }

    public static String makeMultiline(String input) {
        return input.replace("\n", "<br>");
    }
}
