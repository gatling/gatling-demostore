package io.gatling.demostore.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignedUserInfoCookie extends Cookie {

    public static final String NAME = "UserInfo";
    private static final String PATH = "/";
    private static final Pattern UID_PATTERN = Pattern.compile("uid=([A-Za-z0-9]*)");
    private static final Pattern HMAC_PATTERN = Pattern.compile("hmac=([A-Za-z0-9+/=]*)");
    private static final String HMAC_SHA_512 = "HmacSHA512";

    private final Payload payload;
    private final String hmac;

    public SignedUserInfoCookie(String username, String cookieHmacKey) {
        super(NAME, "");
        this.payload = new Payload(username);
        this.hmac = computeHmac(this.payload, cookieHmacKey);
        this.setPath(PATH);
        this.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).toSeconds());
        this.setHttpOnly(true);
    }

    public SignedUserInfoCookie(Cookie cookie, String cookieHmacKey) {
        super(NAME, "");

        if (!NAME.equals(cookie.getName()))
            throw new IllegalArgumentException("No " + NAME + " Cookie");

        this.hmac = parse(cookie.getValue(), HMAC_PATTERN).orElse(null);
        if (hmac == null)
            throw new RuntimeException("Cookie not signed (no HMAC)");

        String username = parse(cookie.getValue(), UID_PATTERN).orElseThrow(() -> new IllegalArgumentException(NAME + " Cookie contains no UID"));
        this.payload = new Payload(username);

        if (!hmac.equals(computeHmac(payload, cookieHmacKey)))
            throw new RuntimeException("Cookie signature (HMAC) invalid");

        this.setPath(cookie.getPath());
        this.setMaxAge(cookie.getMaxAge());
        this.setHttpOnly(cookie.isHttpOnly());
    }

    private static Optional<String> parse(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        if (!matcher.find())
            return Optional.empty();

        if (matcher.groupCount() < 1)
            return Optional.empty();

        String match = matcher.group(1);
        if (match == null || match.trim().isEmpty())
            return Optional.empty();

        return Optional.of(match);
    }

    @Override
    public String getValue() {
        return payload.toString() + "&hmac=" + hmac;
    }

    public UserDetails getUserDetails(UserDetailsService userDetailsService) {
        return userDetailsService.loadUserByUsername(payload.username);
    }

    private String computeHmac(Payload payload, String secretKey) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = Objects.requireNonNull(payload).toString().getBytes(StandardCharsets.UTF_8);

        try {
            Mac mac = Mac.getInstance(HMAC_SHA_512);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, HMAC_SHA_512);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hmacBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Payload {
        private final String username;

        private Payload(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "uid=" + username;
        }
    }
}
