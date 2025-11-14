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
        testDependencyCreation(
                () -> config.required(12345),
                12345,
                RelationType.REQUIRED_DEPENDENCY
        );
    }

    @Test
    void testOptionalDependencyCreation() {
        testDependencyCreation(
                () -> config.optional(67890),
                67890,
                RelationType.OPTIONAL_DEPENDENCY
        );
    }

    @Test
    void testIncompatibleDependencyCreation() {
        testDependencyCreation(
                () -> config.incompatible(11223),
                11223,
                RelationType.INCOMPATIBLE
        );
    }

    @Test
    void testEmbeddedDependencyCreation() {
        testDependencyCreation(
                () -> config.embedded(44556),
                44556,
                RelationType.EMBEDDED_LIBRARY
        );
    }

    @Test
    void testToolDependencyCreation() {
        testDependencyCreation(
                () -> config.tool(77889),
                77889,
                RelationType.TOOL
        );
    }

    private void testDependencyCreation(
            Supplier<CurseforgeDependency> creator,
            Integer projectId,
            RelationType expectedType
    ) {
        String expectedName = "curseforge-" + projectId;

        CurseforgeDependency mockDep = new CurseforgeDependency(expectedName);
        when(container.create(expectedName)).thenReturn(mockDep);

        CurseforgeDependency dep = creator.get();

        assertEquals(projectId, dep.getProjectId());
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