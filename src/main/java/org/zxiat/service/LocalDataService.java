package org.zxiat.service;

import org.zxiat.entity.LocalData;

import java.util.List;

public interface LocalDataService {
    LocalData addLocalData(LocalData localData);
    List<LocalData> getLocalData();
    List<LocalData> getLocalDataByLabel(String label);
    void deleteLocalDataById(String id);
    void deleteAllData();
}
