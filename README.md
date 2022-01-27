# RecipeAppMDP

---

Design Document  

Kerry Healey, Lilly Schott, Cassie Meyer, Christopher Brown, Zilong Chen

## Introduction 
Have you ever wanted to eat a more balanced diet and don't know what to make?  

RecipeApp can help you:
-  Find receipes based on your desired nutritional value, cuisine, time, or cooking method
-  Suggest receipes based off your previous meal choices
-  Curate a list of ingredients to add to your next grocery trip

Use your mobile device to find your next balanced meal.  Search and filter within the interface to find a receipe that best fits your individualized needs.
Received unique notifications of receipes we think you should try.

## Storyboard


## Functional Requirements

### Requirement 100.0: Search for Recipes

#### Scenario

As a user interested in cooking, I want to be able to search for recipes based on cuisine, cooking method, or nutrition.

#### Dependencies

Recipe data are available and accessible.

#### Assumptions

Recipe names are in English.

#### Examples
1.1  
**Given** a feed of recipe data is available\
**When** I search for "Japanese"\
**Then** I should receive at least one result with the following attribute:\
Cuisine: Japanese


1.2  
**Given** a feed of recipe data is available\
**When** I search for "baking"\
**Then** I should receive at least one result with the following attribute:\
Cooking Method: Baking

1.3  
**Given** a feed of recipe data is available\
**When** I search for "high protein"\
**Then** I should receive at least one result with the following attribute:\
Nutrition: High Protein\
and I should receive at least one result with the attributes:\
Nutrition: High Protein\
Vegan Friendly: Yes

### Requirement 101 Uploading a Recipe

#### Scenario

As a user interested in cooking, I want to be able to save my personal recipes and share it with others, including photos.

#### Dependencies

Recipe search data are available and accessible.
The device has a camera and the user has granted camera permissions.

#### Assumptions  

Recipe names are in English.

#### Examples  

2.1

**Given** a feed of recipe data is available\
**Given** camera is available\
**When**\
Select: "Add new recipe"\
Enter:\
Name: Tonkotsu Ramen\
Cuisine: Japanese\
Cooking Method: Boiling\
Nutrition: High Carbs, High Fat, High Protein\
Add photos: *placeholder for image?*\
Tap Save Icon\
**Then**  the new entry for "Tonkotsu Ramen" should be displayed 

2.2

**Given** a feed of recipe data is available\
**When**\
Select: "Add new recipe"\
Enter: no data is entered\
Tap save icon\
**Then** an error dialog appears showing no data was entered.

## Class Diagram

### Class Diagram Description

## Scrum Roles

- DevOps/Product Owner/Scrum Master: Kerry Healey
- Frontend Developer: Lilly Schott, Zilong Chen
- Integration Developer: Cassie Meyer, Christopher Brown

## Weekly Meeting
5pm on Tuesdays (virtual via Teams)

Meeting Link: https://teams.microsoft.com/l/meetup-join/19%3ameeting_NGYzNzk1N2ItZmU1ZS00NjcxLTk4OTItNmVjOGVkMDJhZWY0%40thread.v2/0?context=%7b%22Tid%22%3a%22f5222e6c-5fc6-48eb-8f03-73db18203b63%22%2c%22Oid%22%3a%22e0f2ca38-852a-47bc-b902-8914866936b9%22%7d

