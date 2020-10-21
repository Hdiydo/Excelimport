package org.zxiat.service.impl;

import org.springframework.stereotype.Service;
import org.zxiat.dao.LocalDataRepository;
import org.zxiat.entity.LocalData;
import org.zxiat.service.LocalDataService;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class LocalDataServiceImpl implements LocalDataService {

    private final LocalDataRepository localDataRepository;

    public LocalDataServiceImpl(LocalDataRepository localDataRepository) {
        this.localDataRepository = localDataRepository;
    }

    @Override
    public LocalData addLocalData(LocalData localData) {
        return localDataRepository.save(localData);
    }

    @Override
    public List<LocalData> getLocalData() {
        List<LocalData> data = (List<LocalData>)localDataRepository.findAll();
        Collections.reverse(data); // 倒序排列
        return data;
    }

    @Override
    public List<LocalData> getLocalDataByLabel(String label) {
        List<LocalData> data = localDataRepository.findByMlabelLike(label);
        Collections.reverse(data); // 倒序排列
        return data;
    }

    @Override
    public void deleteLocalDataById(String id) {
        localDataRepository.deleteById(id);
    }

    @Override
    public void deleteAllData() {
        localDataRepository.deleteAll();
    }
}
