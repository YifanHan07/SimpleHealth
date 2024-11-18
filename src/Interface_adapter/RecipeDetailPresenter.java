package Interface_adapter;
import use_case.RecipeDetailInteractor;

import java.util.List;

public class RecipeDetailPresenter {
    private final RecipeDetailInteractor recipeInteractor;

    public RecipeDetailPresenter(RecipeDetailInteractor recipeInteractor) {
        this.recipeInteractor = recipeInteractor;
    }

    public void execute(String keyword, String title) throws Exception {
        RecipeDetailInteractor.execute(keyword, title);
    }
}
