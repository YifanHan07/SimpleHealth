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
