package use_case.searchRecipe;

import data_access.EdamamAPI;
import data_access.tagsAvailble;
import entity.Recipe;

import java.util.List;
import java.util.Objects;

public class tagInteractor {
    private final EdamamAPI api;

    public tagInteractor(EdamamAPI api) {
        this.api = api;
    }

    public List<String> fetchAvailableTags() {
        // Use the static list of tags
        return tagsAvailble.AVAILABLE_TAGS;
    }

    public String buildTagQuery(List<String> selectedTags) {
        StringBuilder tagQuery = new StringBuilder();

        if (selectedTags.contains("None")) {
            tagQuery.append("&health=DASH");
        }
        else {
            for (String tag : selectedTags) {
                if (tagQuery.length() > 0) {
                    tagQuery.append("&");
                }
                tagQuery.append("health=").append(tag);
            }
        }
        return tagQuery.toString();
    }

    public List<Recipe> searchRecipes(String query, int maxResults, StringBuilder tagQuery) throws Exception {
        return api.searchRecipes(query, maxResults, tagQuery);
    }
}