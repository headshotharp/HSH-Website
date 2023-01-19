package de.headshotharp.web.plugin.dataimport;

import java.io.File;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import de.headshotharp.web.database.Role;
import de.headshotharp.web.plugin.dataimport.ImportData.ImportRole;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class DataImportService {

    private final File file;
    private final ObjectMapper mapper = new YAMLMapper();
    private final DataProvider dp;

    public DataImportService(String pluginName, DataProvider dp) {
        this(Paths.get("plugins", pluginName, "import.yml").toFile(), dp);
    }

    public DataImportService(File file, DataProvider dp) {
        this.file = file;
        this.dp = dp;
    }

    public void importData() {
        if (!file.exists()) {
            return;
        }
        try {
            ImportData data = mapper.readValue(file, ImportData.class);
            for (ImportRole role : data.getRoles()) {
                if (!dp.role().findByName(role.getName()).isPresent()) {
                    Role entity = Role.builder().name(role.getName())
                            .description(role.getDescription())
                            .prefix(role.getPrefix())
                            .power(role.getPower())
                            .permissions(role.getPermissions())
                            .build();
                    dp.role().persist(entity);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error while importing data from " + file.getPath(), e);
        }
    }
}
