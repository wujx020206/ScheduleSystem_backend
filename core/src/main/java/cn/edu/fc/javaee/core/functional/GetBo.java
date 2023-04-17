package cn.edu.fc.javaee.core.functional;

import java.util.Optional;

@FunctionalInterface
public interface GetBo<B, P>{
    B getBo(P po, Optional<String> redisKey);
}
