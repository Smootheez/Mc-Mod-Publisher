package io.github.smootheez.curseforge;

import org.gradle.api.*;
import org.gradle.api.model.*;
import org.junit.jupiter.api.*;

import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class CurseforgeConfigTest {
    private NamedDomainObjectContainer<CurseforgeDependency> container;
    private CurseforgeConfig config;

    @BeforeEach
    void setup() {
        ObjectFactory objectFactory = mock(ObjectFactory.class);
        container = mock(NamedDomainObjectContainer.class);

        // Mock PublisherConfig constructor behavior
        when(objectFactory.domainObjectContainer(CurseforgeDependency.class))
                .thenReturn(container);

        config = new CurseforgeConfig(objectFactory);
    }

    @Test
    void testDependenciesActionExecutes() {
        Action<NamedDomainObjectContainer<CurseforgeDependency>> action = mock(Action.class);

        config.dependencies(action);

        verify(action).execute(container);
    }

    @Test
    void testRequiredDependencyCreation() {
        testDependencyCreation(() -> config.required("test-slug-req"), "test-slug-req", RelationType.REQUIRED_DEPENDENCY);
    }

    @Test
    void testOptionalDependencyCreation() {
        testDependencyCreation(() -> config.optional("test-slug-opt"), "test-slug-opt", RelationType.OPTIONAL_DEPENDENCY);
    }

    @Test
    void testIncompatibleDependencyCreation() {
        testDependencyCreation(() -> config.incompatible("test-slug-inc"), "test-slug-inc", RelationType.INCOMPATIBLE);
    }

    @Test
    void testEmbeddedDependencyCreation() {
        testDependencyCreation(() -> config.embedded("test-slug-emb"), "test-slug-emb", RelationType.EMBEDDED_LIBRARY);
    }

    @Test
    void testToolDependencyCreation() {
        testDependencyCreation(() -> config.tool("test-slug-tool"), "test-slug-tool", RelationType.TOOL);
    }


    private void testDependencyCreation(
            Supplier<CurseforgeDependency> creator,
            String slug,
            RelationType expectedType
    ) {
        String expectedName = "curseforge-" + slug;

        CurseforgeDependency mockDep = new CurseforgeDependency(expectedName);
        when(container.create(expectedName)).thenReturn(mockDep);

        CurseforgeDependency dep = creator.get();

        assertEquals(slug, dep.getSlug());
        assertEquals(expectedType, dep.getRelationType());
    }

    @Test
    void testDefaults() {
        assertEquals(ChangelogType.MARKDOWN, config.getChangelogType());
        assertEquals(2, config.getEnvironmentType().size());
        assertTrue(config.getEnvironmentType().contains(EnvironmentType.CLIENT));
        assertTrue(config.getEnvironmentType().contains(EnvironmentType.SERVER));
        assertFalse(config.isManualRelease());
    }
}