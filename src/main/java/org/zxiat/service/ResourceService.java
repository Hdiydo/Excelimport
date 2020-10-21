package org.zxiat.service;

import org.zxiat.entity.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceService {

  Resource addResource(Resource resource);

  List<Resource> getResource();

  void deleteResource(Long id);

  Resource updateResource(Resource resource);

  Optional<Resource> getResourceById(Long id);

  void deleteBatchResources(List<Long> resources);

  Resource getResourceByCollection(String collection);

  List<Resource> searchResource(String description, String org);


}
