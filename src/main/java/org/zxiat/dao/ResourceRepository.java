package org.zxiat.dao;


import org.zxiat.entity.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends CrudRepository<Resource,Long> {

  void deleteByIdIn(List<Long> resources);

  Resource findByCollection(String collection);

  List<Resource> findAllByDescriptionContainingAndOrgContaining(String description, String org);



}
