package de.headshotharp.web.plugin.dataimport;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ImportData {

    private List<ImportRole> roles = new LinkedList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class ImportRole {

        private String name;
        private String description;
        private String prefix;
        private int power;
        private List<String> permissions = new LinkedList<>();
    }
}
