package io.github.smootheez;

import com.google.gson.*;
import lombok.*;
import okhttp3.*;
import org.gradle.api.*;

@RequiredArgsConstructor
public abstract class Publisher {
    protected final Project project;
    protected final McModPublisherExtension extension;
    protected final OkHttpClient client;
    protected static final Gson GSON = new Gson();

    public abstract void publish();
}
