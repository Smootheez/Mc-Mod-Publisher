package io.github.smootheez.modrinth;

import org.gradle.api.*;
import org.gradle.api.model.*;
import org.junit.jupiter.api.*;

import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class ModrinthConfigTest {
    private NamedDomainObjectContainer<ModrinthDependency> container;
    private ModrinthConfig config;

    @BeforeEach
    void setup() {
        ObjectFactory objectFactory = mock(ObjectFactory.class);
        container = mock(NamedDomainObjectContainer.class);

        // Mock PublisherConfig constructor behavior
        when(objectFactory.domainObjectContainer(ModrinthDependency.class))
                .thenReturn(container);

        config = new ModrinthConfig(objectFactory);
    }

    @Test
    void testDependenciesActionExecutes() {
        Action<NamedDomainObjectContainer<ModrinthDependency>> action = mock(Action.class);

        config.dependencies(action);

        verify(action).execute(container);
    }

    @Test
    void testRequiredDependencyCreation() {
        testDependencyCreation(
                () -> config.required("fabric-api"),
                "fabric-api",
                DependencyType.REQUIRED
        );
    }

    @Test
    void testOptionalDependencyCreation() {
        testDependencyCreation(
                () -> config.optional("cloth-config"),
                "cloth-config",
                DependencyType.OPTIONAL
        );
    }

    @Test
    void testIncompatibleDependencyCreation() {
        testDependencyCreation(
                () -> config.incompatible("bad-mod"),
                "bad-mod",
                DependencyType.INCOMPATIBLE
        );
    }

    @Test
    void testEmbeddedDependencyCreation() {
        testDependencyCreation(
                () -> config.embedded("data-lib"),
                "data-lib",
                DependencyType.EMBEDDED
        );
    }

    private void testDependencyCreation(
            Supplier<ModrinthDependency> creator,
            String projectId,
            DependencyType expectedType
    ) {
        String expectedName = "modrinth-" + projectId;

        ModrinthDependency mockDep = new ModrinthDependency(expectedName);
        when(container.create(expectedName)).thenReturn(mockDep);

        ModrinthDependency dep = creator.get();

        assertEquals(projectId, dep.getProjectId());
        assertEquals(expectedType, dep.getDependencyType());
    }

    @Test
    void testDefaults() {
        assertTrue(config.isFeatured());
        assertEquals("listed", config.getStatus());
    }
}