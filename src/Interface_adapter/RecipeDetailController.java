package Interface_adapter;
import use_case.RecipeDetailInteractor;

import java.util.List;

public class RecipeDetailController {
    private final RecipeDetailInteractor recipeInteractor;

    public RecipeDetailController(RecipeDetailInteractor recipeInteractor) {
        this.recipeInteractor = recipeInteractor;
    }

    public void execute(String keyword, String title) throws Exception {
        RecipeDetailInteractor.execute(keyword, title);
    }
}
