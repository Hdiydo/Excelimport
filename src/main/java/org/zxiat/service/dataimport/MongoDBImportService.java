package org.zxiat.service.dataimport;


import org.zxiat.entity.Resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MongoDBImportService {

  Map<String, String> testConnection(Resource resource) throws SQLException;

  Map<String, List<String>> getCollections(Resource resource) throws SQLException;


}
