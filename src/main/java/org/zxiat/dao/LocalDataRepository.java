package org.zxiat.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.zxiat.entity.LocalData;

import java.util.List;

@Repository
public interface LocalDataRepository extends CrudRepository<LocalData, String> {

    @Query(value = "select t from LocalData t where t.mlabel like %?1%")
    List<LocalData> findByMlabelLike(String Mlabel);

}
