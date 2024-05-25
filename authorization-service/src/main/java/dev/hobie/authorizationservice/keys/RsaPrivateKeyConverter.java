package dev.hobie.authorizationservice.keys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.FileCopyUtils;

@RequiredArgsConstructor
public class RsaPrivateKeyConverter
    implements Serializer<RSAPrivateKey>, Deserializer<RSAPrivateKey> {

  private final TextEncryptor textEncryptor;

  @Override
  public void serialize(RSAPrivateKey object, OutputStream outputStream) throws IOException {
    var pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(object.getEncoded());
    var string =
        "-----BEGIN PRIVATE KEY-----\n"
            + Base64.getMimeEncoder().encodeToString(pkcs8EncodedKeySpec.getEncoded())
            + "\n-----END PRIVATE KEY-----";
    outputStream.write(textEncryptor.encrypt(string).getBytes());
  }

  @NonNull
  @Override
  public RSAPrivateKey deserialize(@NonNull InputStream inputStream) {
    try {
      var pem =
          textEncryptor.decrypt(FileCopyUtils.copyToString(new InputStreamReader(inputStream)));
      var privateKeyPEM =
          pem.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
      var encoded = Base64.getMimeDecoder().decode(privateKeyPEM);
      var keyFactory = KeyFactory.getInstance("RSA");
      var keySpec = new PKCS8EncodedKeySpec(encoded);
      return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException throwable) {
      throw new IllegalArgumentException("there's been an exception", throwable);
    }
  }
}
