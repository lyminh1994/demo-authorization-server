package dev.hobie.authorizationservice.keys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.FileCopyUtils;

@RequiredArgsConstructor
public class RsaPublicKeyConverter implements Serializer<RSAPublicKey>, Deserializer<RSAPublicKey> {

  private final TextEncryptor textEncryptor;

  @Override
  public void serialize(RSAPublicKey object, OutputStream outputStream) throws IOException {
    var x509EncodedKeySpec = new X509EncodedKeySpec(object.getEncoded());
    var pem =
        "-----BEGIN PUBLIC KEY-----\n"
            + Base64.getMimeEncoder().encodeToString(x509EncodedKeySpec.getEncoded())
            + "\n-----END PUBLIC KEY-----";
    outputStream.write(this.textEncryptor.encrypt(pem).getBytes());
  }

  @NonNull
  @Override
  public RSAPublicKey deserialize(@NonNull InputStream inputStream) {
    try {
      var pem =
          textEncryptor.decrypt(FileCopyUtils.copyToString(new InputStreamReader(inputStream)));
      var publicKeyPEM =
          pem.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
      var encoded = Base64.getMimeDecoder().decode(publicKeyPEM);
      var keyFactory = KeyFactory.getInstance("RSA");
      var keySpec = new X509EncodedKeySpec(encoded);
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException throwable) {
      throw new IllegalArgumentException("there's been an exception", throwable);
    }
  }
}
