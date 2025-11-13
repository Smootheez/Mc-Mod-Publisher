package io.github.smootheez;

import lombok.*;
import org.gradle.api.*;

@Getter
@Setter
public abstract class PublisherConfig<T, S> {
    private String token; // Required
    private String projectId; // Required

    public abstract void dependencies(Action<NamedDomainObjectContainer<T>> action);

    public abstract T required(S id);

    public abstract T optional(S id);

    public abstract T incompatible(S id);

    public abstract T embedded(S id);
}
