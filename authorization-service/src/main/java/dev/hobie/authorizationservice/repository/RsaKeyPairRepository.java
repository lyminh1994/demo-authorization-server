package dev.hobie.authorizationservice.repository;

import dev.hobie.authorizationservice.dto.RsaKeyPair;
import java.util.List;

public interface RsaKeyPairRepository {

  List<RsaKeyPair> findKeyPairs();

  void save(RsaKeyPair rsaKeyPair);
}
