package br.com.portalrastreamento.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Guarda a senha como hash SHA-256 com salt aleatório.
 * (Em um sistema real, prefira BCrypt ou Argon2.)
 */
public final class SenhaUtil {

    private static final SecureRandom ALEATORIO = new SecureRandom();

    private SenhaUtil() {
    }

    public static String novoSalt() {
        byte[] bytes = new byte[16];
        ALEATORIO.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String hash(String salt, String senha) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] resumo = sha.digest((salt + senha).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : resumo) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível", e);
        }
    }

    public static boolean confere(String salt, String senha, String hashEsperado) {
        if (salt == null || senha == null || hashEsperado == null) {
            return false;
        }
        return MessageDigest.isEqual(
                hash(salt, senha).getBytes(StandardCharsets.UTF_8),
                hashEsperado.getBytes(StandardCharsets.UTF_8));
    }
}
