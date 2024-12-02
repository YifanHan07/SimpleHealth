# SimpleHealth
## Team members
Team members: Grace Zeng, Yifan Han, Christian Fisla, Jiayi Liu, Hamza Hudhud  
Github username: Z-Grace, YifanHan07, ChristianFisla, Safariiiiiiiii, hamzahudhud

## Use cases for each user story
### User Story 1 - Account Creation
Description: Creates a personal account.  
User Interaction: User enters details and submit the details to create an account.  
Controller: AccountController handles the click made by the user and makes decisions on how to start the account creation by calling AccountCreationInteractor.  
Interactor: AccountCreationInteractor processes on creating an account by checking if the inputs are valid.  
Presenter: AccountPresenter displays a success confirmation message.

### User Story 2 - Recipe Browsing
Description: User browsing recipes by searching keywords.  
User Interaction: User types a keyword into the search field and presses “Search”.  
Controller: RecipeSearchController handles the input inputted by the user and passes the keyword to the RecipeSearchInteractor.  
Interactor: RecipeSearchInteractor filters the recipes based on the entered keyword, after checking the API, if no matching recipes are found, an empty result is returned.  
Presenter: RecipePresenter displays the list of the recipes based on the entered keyword or an error message.  

### User Stor 3：Save recipe to personal collection  

Description: Saving the selected recipe to the user’s collection.  

User Interaction: The user wants to save recipes to his personal collection so that he can easily access his favorite meals.  

Controller: In the file SaveRecipeController, handles the “Save to Collection” button clicked by user   

Presenter: In the file SaveRecipePresenter, If the recipe is successfully added to the collection, displays a message saying “Successfully added to collection.”  

Interactor: In the file SaveRecipeInteractor, receives the recipe the user wants to save and adds it to the user’s collection list   



### User Story 4: Combine Meals for Nutritional Summary  

Description: combine multiple recipes and show the total nutrition values.  

User Interaction: The user combines multiple recipes in a day to view the total nutritional information, ensuring her daily intake meets her dietary requirements.  

Controller: In the file CombineMealsController, handles the “Combine Meals” button clicked by user  

Presenter: In the file CombineMealsPresenter, formats and presents the nutritional summary.  

Interactor: In the file CombineMealsInteractor, calculates the total nutrition for selected recipes,, such as calories, protein, fat, etc.  





### User Story 5: Search Recipes by Health Label   

Description: Enable users to filter recipes by health labels.  
User Interaction:search for recipes based on specific nutritional criteria (e.g., vegan, wheat-free) to find meals that fit her health goals.  
Controller: TagController handles the input inputted by the user and passes the keyword to the TagInteractor.  
Interactor: TagInteractor searches for recipes by specified health label by calling the API.  
View:  TagSelectionView displays the avaliable and selected tags.  






### User Story 6: View detailed nutrition information  

Description:  Provide detailed nutritional information for a selected recipe.  

User Interaction: The user wants to view detailed nutritional information for each recipe to make informed meal choices.  

Controller: In the file ViewNutritionDetailsController, handles user selection of a recipe.  

Presenter: In the file ViewNutritionDetailsPresenter,  formats and displays the nutritional information.  

Interactor: In the file ViewNutritionDetailsInteractor, retrieves and processes the nutritional details of a selected recipe.



