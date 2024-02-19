package com.dinedynamo.services;


import com.dinedynamo.collections.menu_collections.*;
import com.dinedynamo.repositories.menu_repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenusService
{

    @Autowired
    MenusRepository menusRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    SubSubCategoryRepository subSubCategoryRepository;

    public Menus save(Menus menus){

        for(Category category: menus.getListOfCategories()){

            category.setRestaurantId(menus.getRestaurantId());
            categoryRepository.save(category);

            //Set sub categories
            if(category.getListOfSubCategories() != null){
                for(SubCategory subCategory: category.getListOfSubCategories()){
                    subCategory.setRestaurantId(menus.getRestaurantId());
                    subCategory.setCategoryId(category.getCategoryId());
                    subCategoryRepository.save(subCategory);

                    //Set sub-sub categories
                    if(subCategory.getListOfSubSubCategories() != null){
                        for(SubSubCategory subSubCategory: subCategory.getListOfSubSubCategories()){

                            subSubCategory.setSubcategoryId(subCategory.getSubcategoryId());
                            subSubCategory.setRestaurantId(menus.getRestaurantId());
                            subSubCategoryRepository.save(subSubCategory);

                            //Set menu items in sub-sub categories
                            if(subSubCategory.getListOfMenuItems() != null)
                            {
                                for(MenuItem menuItem: subSubCategory.getListOfMenuItems()){

                                    menuItem.setRestaurantId(menus.getRestaurantId());
                                    menuItem.setParentId(subSubCategory.getSubSubcategoryId());
                                    menuItem.setParentType(ParentType.SUBSUBCATEGORY);
                                    menuItemRepository.save(menuItem);
                                }
                            }

                        }

                    }


                    //Set menuitems in sub categories
                    if(subCategory.getListOfMenuItems() != null){
                        for(MenuItem menuItem: subCategory.getListOfMenuItems()){

                            menuItem.setRestaurantId(menus.getRestaurantId());
                            menuItem.setParentId(subCategory.getSubcategoryId());
                            menuItem.setParentType(ParentType.SUBCATEGORY);
                            menuItemRepository.save(menuItem);
                        }
                    }
                }
            }

            //set menu items in category
            if(category.getListOfMenuItems() != null){
                for(MenuItem menuItem: category.getListOfMenuItems()){

                    menuItem.setRestaurantId(menus.getRestaurantId());
                    menuItem.setParentId(category.getCategoryId());
                    menuItem.setParentType(ParentType.CATEGORY);
                    menuItemRepository.save(menuItem);
                }
            }
        }

        menusRepository.save(menus);

        return menus;
    }

    public boolean deleteMenus(String restaurantId){

        subCategoryRepository.deleteByRestaurantId(restaurantId);
        subCategoryRepository.deleteByRestaurantId(restaurantId);
        categoryRepository.deleteByRestaurantId(restaurantId);
        menuItemRepository.deleteByRestaurantId(restaurantId);
        menusRepository.deleteByRestaurantId(restaurantId);
        return true;
    }
}
