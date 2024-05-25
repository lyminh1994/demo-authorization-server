package dev.hobie.authorizationservice.utils;

import dev.hobie.authorizationservice.dto.RsaKeyPair;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyUtils {

  public RsaKeyPair generateKeyPair(String keyId, Instant created) {
    var keyPair = generateRsaKey();
    var publicKey = (RSAPublicKey) keyPair.getPublic();
    var privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RsaKeyPair(keyId, created, publicKey, privateKey);
  }

  private KeyPair generateRsaKey() {
    try {
      var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      return keyPairGenerator.generateKeyPair();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
}
