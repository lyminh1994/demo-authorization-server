package dev.hobie.authorization_service.keys;

import java.util.List;

public interface RsaKeyPairRepository {

  List<RsaKeyPair> findKeyPairs(); // <1>

  void save(RsaKeyPair rsaKeyPair); // <2>
}
