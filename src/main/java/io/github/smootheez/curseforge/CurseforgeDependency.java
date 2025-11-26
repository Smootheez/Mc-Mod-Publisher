package io.github.smootheez.curseforge;

import lombok.*;

import javax.inject.*;

@Getter
@Setter
public class CurseforgeDependency {
    private String slug;
    private RelationType relationType;

    private final String name;

    @Inject
    public CurseforgeDependency(String name) {
        this.name = name;
    }
}
