package org.zxiat;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.zxiat.view.DBimportView;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableAsync
public class ExcelimportApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(ExcelimportApplication.class, DBimportView.class,args);
    }


}
