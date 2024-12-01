package interface_adapter.searchRecipe;

import entity.Recipe;
import use_case.searchRecipe.tagInteractor;

import java.util.List;

public class tagController {
    private final tagInteractor interactor;

    public tagController(tagInteractor interactor) {
        this.interactor = interactor;
    }

    public List<String> getAvailableTags() {
        return interactor.fetchAvailableTags();
    }

    public List<Recipe> fetchRecipes(String query, int maxResults, List<String> selectedTags) throws Exception {
        String tagQuery = interactor.buildTagQuery(selectedTags);
        return interactor.searchRecipes(query, maxResults, new StringBuilder(tagQuery));
    }
}