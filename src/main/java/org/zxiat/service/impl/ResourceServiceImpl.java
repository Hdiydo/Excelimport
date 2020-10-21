package org.zxiat.service.impl;

import org.zxiat.dao.ResourceRepository;
import org.zxiat.entity.Resource;
import org.zxiat.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

  private final ResourceRepository resourceRepository;

  @Autowired
  public ResourceServiceImpl(ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
  }

  @Override
  public Resource addResource(Resource resource) {
//    resource.setDocNum(null);
//    resource.setSize(null);
    return resourceRepository.save(resource);
  }

  @Override
  public List<Resource> getResource() {
    return (List<Resource>) this.resourceRepository.findAll();
  }

  @Override
  public void deleteResource(Long id) {
    this.resourceRepository.deleteById(id);
  }

  @Override
  public Resource updateResource(Resource resource) {
    return resourceRepository.save(resource);
  }

  @Override
  public Optional<Resource> getResourceById(Long id) {
    return resourceRepository.findById(id);
  }

  @Override
  public void deleteBatchResources(List<Long> resources) {
    this.resourceRepository.deleteByIdIn(resources);
  }

  @Override
  public Resource getResourceByCollection(String collection) {
    return this.resourceRepository.findByCollection(collection);
  }

  @Override
  public List<Resource> searchResource(String description, String org) {
    return this.resourceRepository.findAllByDescriptionContainingAndOrgContaining(description,org);
  }


}
