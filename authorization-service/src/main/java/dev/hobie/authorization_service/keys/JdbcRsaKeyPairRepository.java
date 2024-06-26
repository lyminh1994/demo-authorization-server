package dev.hobie.authorization_service.keys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class JdbcRsaKeyPairRepository implements RsaKeyPairRepository {

  private final JdbcTemplate jdbc;

  private final RsaPublicKeyConverter rsaPublicKeyConverter;

  private final RsaPrivateKeyConverter rsaPrivateKeyConverter;

  private final RowMapper<RsaKeyPair> keyPairRowMapper;

  @Override // <1>
  public List<RsaKeyPair> findKeyPairs() {
    var sql = "select * from rsa_key_pairs order by created desc";
    return this.jdbc.query(sql, this.keyPairRowMapper);
  }

  @Override // <2>
  public void save(RsaKeyPair keyPair) {
    var sql = """
        insert into rsa_key_pairs (id, private_key, public_key, created)
        values (?, ?, ?, ?)
        on conflict on constraint rsa_key_pairs_id_created_key
        do nothing
        """;
    try (var privateBAOS = new ByteArrayOutputStream();
        var publicBAOS = new ByteArrayOutputStream()) {
      this.rsaPrivateKeyConverter.serialize(keyPair.privateKey(), privateBAOS);
      this.rsaPublicKeyConverter.serialize(keyPair.publicKey(), publicBAOS);
      var updated = this.jdbc.update(
          sql,
          keyPair.id(),
          privateBAOS.toString(),
          publicBAOS.toString(),
          new Date(keyPair.created().toEpochMilli()));
      Assert.state(
          updated == 0 || updated == 1, "no more than one record should have been updated");
    } catch (IOException e) {
      throw new IllegalArgumentException("there's been an exception", e);
    }
  }
}
