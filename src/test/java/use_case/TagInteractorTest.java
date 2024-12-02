import use_case.searchRecipe.tagInteractor;
import data_access.EdamamAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagInteractorTest {
    private tagInteractor tagInteractor;

    @BeforeEach
    void setUp() {
        EdamamAPI mockApi = new EdamamAPI();
        tagInteractor = new tagInteractor(mockApi);
    }

    @Test
    void testBuildTagQuery_withNoneTag() {
        List<String> selectedTags = Arrays.asList("None");
        String expected = "&health=DASH";
        String actual = tagInteractor.buildTagQuery(selectedTags);
        assertEquals(expected, actual);
    }

    @Test
    void testBuildTagQuery_withNoTags() {
        List<String> selectedTags = Arrays.asList();
        String expected = "";
        String actual = tagInteractor.buildTagQuery(selectedTags);
        assertEquals(expected, actual);
    }

    @Test
    void testBuildTagQuery_withOneTag() {
        List<String> selectedTags = Arrays.asList("vegan");
        String expected = "health=vegan";
        String actual = tagInteractor.buildTagQuery(selectedTags);
        assertEquals(expected, actual);
    }

    @Test
    void testBuildTagQuery_withMultipleTags() {
        List<String> selectedTags = Arrays.asList("gluten-free", "vegan");
        String expected = "health=gluten-free&health=vegan";
        String actual = tagInteractor.buildTagQuery(selectedTags);
        assertEquals(expected, actual);
    }

    @Test
    void testBuildTagQuery_withNoneAndOtherTags() {
        List<String> selectedTags = Arrays.asList("None", "gluten-free");
        String expected = "&health=DASH";
        String actual = tagInteractor.buildTagQuery(selectedTags);
        assertEquals(expected, actual);
    }

}
