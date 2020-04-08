package com.dna.rna.config;

public class JWtProperties {
    // BELOW SECRET IS JUST TEMPORAL VALUE
    public static final String SECRET = "1234";
    public static final int EXPIRATION_TIME = 3600; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
