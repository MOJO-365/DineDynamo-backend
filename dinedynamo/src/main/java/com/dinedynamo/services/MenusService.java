package com.dinedynamo.services;


import com.dinedynamo.collections.menu_collections.*;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.menu_repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    RestaurantRepository restaurantRepository;

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

                            subSubCategory.setSubCategoryId(subCategory.getSubCategoryId());
                            subSubCategory.setRestaurantId(menus.getRestaurantId());
                            subSubCategoryRepository.save(subSubCategory);

                            //Set menu items in sub-sub categories
                            if(subSubCategory.getListOfMenuItems() != null)
                            {
                                for(MenuItem menuItem: subSubCategory.getListOfMenuItems()){

                                    menuItem.setRestaurantId(menus.getRestaurantId());
                                    menuItem.setParentId(subSubCategory.getSubSubCategoryId());
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
                            menuItem.setParentId(subCategory.getSubCategoryId());
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
        subSubCategoryRepository.deleteByRestaurantId(restaurantId);
        categoryRepository.deleteByRestaurantId(restaurantId);
        menuItemRepository.deleteByRestaurantId(restaurantId);
        menusRepository.deleteByRestaurantId(restaurantId);
        return true;
    }

    //For update
    public Menus updateWholeMenu(String  restaurantId){

        Menus menus = menusRepository.findByRestaurantId(restaurantId).orElse(null);

        if(menus == null){
            System.out.println("NO MENU PRESENT IN DB");
            return null;
        }


        List<Category> updatedListOfCategories = categoryRepository.findByRestaurantId(restaurantId);
        List<SubCategory> updatedListOfSubCategories = new ArrayList<>();
        List<SubSubCategory>updatedListOfSubSubCategories = new ArrayList<>();
        List<MenuItem> updatedListOfMenuItemsInSubCategories = new ArrayList<>();
        List<MenuItem> updatedListOfMenuItemsInSubSubCategories = new ArrayList<>();
        List<MenuItem> updatedListOfMenuItemsInCategories = new ArrayList<>();

        for(Category category: updatedListOfCategories){

            updatedListOfMenuItemsInCategories = menuItemRepository.findByRestaurantIdAndParentIdAndParentType(restaurantId, category.getCategoryId(), ParentType.CATEGORY);
            if(updatedListOfMenuItemsInCategories != null)
            {
                category.setListOfMenuItems(updatedListOfMenuItemsInCategories);
            }

            updatedListOfSubCategories = subCategoryRepository.findByRestaurantIdAndCategoryId(restaurantId, category.getCategoryId());
            for(SubCategory subCategory: updatedListOfSubCategories){

                updatedListOfMenuItemsInSubCategories = menuItemRepository.findByRestaurantIdAndParentIdAndParentType(restaurantId, subCategory.getSubCategoryId(), ParentType.SUBCATEGORY);
                if(updatedListOfMenuItemsInSubCategories != null)
                {
                    subCategory.setListOfMenuItems(updatedListOfMenuItemsInSubCategories);
                }

                updatedListOfSubSubCategories = subSubCategoryRepository.findByRestaurantIdAndSubCategoryId(restaurantId, subCategory.getSubCategoryId());
                for(SubSubCategory subSubCategory: updatedListOfSubSubCategories){

                    updatedListOfMenuItemsInSubSubCategories = menuItemRepository.findByRestaurantIdAndParentIdAndParentType(restaurantId, subSubCategory.getSubSubCategoryId(), ParentType.SUBSUBCATEGORY);
                    if(updatedListOfMenuItemsInSubSubCategories != null)
                    {
                        subSubCategory.setListOfMenuItems(updatedListOfMenuItemsInSubSubCategories);
                    }
                }
                subCategory.setListOfSubSubCategories(updatedListOfSubSubCategories);

            }
            category.setListOfSubCategories(updatedListOfSubCategories);
        }

        menus.setListOfCategories(updatedListOfCategories);
        menusRepository.save(menus);
        System.out.println("SAVED");
        return menus;

    }




    public Menus addCategoryInMenu(String restaurantId, String menuId, Category category){

        category.setRestaurantId(restaurantId);
        categoryRepository.save(category);

        List<SubCategory> subCategoryList = category.getListOfSubCategories();
        List<MenuItem> menuItemList = category.getListOfMenuItems();

        if(menuItemList!=null){
            for(MenuItem menuItem: menuItemList){

                menuItem.setRestaurantId(restaurantId);
                menuItem.setParentType(ParentType.CATEGORY);
                menuItem.setParentId(category.getCategoryId());
                menuItemRepository.save(menuItem);
            }
        }

        if(subCategoryList!=null){
            for(SubCategory subCategory: subCategoryList){

                subCategory.setCategoryId(category.getCategoryId());
                subCategory.setRestaurantId(restaurantId);
                subCategoryRepository.save(subCategory);

                if(subCategory.getListOfMenuItems()!=null){
                    for(MenuItem menuItem: subCategory.getListOfMenuItems()){

                        menuItem.setRestaurantId(restaurantId);
                        menuItem.setParentType(ParentType.SUBCATEGORY);
                        menuItem.setParentId(subCategory.getSubCategoryId());
                        menuItemRepository.save(menuItem);

                    }
                }

                if(subCategory.getListOfSubSubCategories() != null){
                    for(SubSubCategory subSubCategory: subCategory.getListOfSubSubCategories()){

                        subSubCategory.setSubCategoryId(subCategory.getSubCategoryId());
                        subSubCategory.setRestaurantId(restaurantId);
                        subSubCategoryRepository.save(subSubCategory);

                        if(subSubCategory.getListOfMenuItems() != null){
                            for(MenuItem menuItem: subSubCategory.getListOfMenuItems()){

                                menuItem.setRestaurantId(restaurantId);
                                menuItem.setParentType(ParentType.SUBSUBCATEGORY);
                                menuItem.setParentId(subSubCategory.getSubSubCategoryId());
                                menuItemRepository.save(menuItem);
                            }
                        }
                    }
                }
            }
        }

        return updateWholeMenu(restaurantId);

    }


    //DONE
    public Menus addMenuItemInCategory(MenuItem menuItem){

        if(menuItem.getRestaurantId() == null || menuItem.getRestaurantId().equals("") || menuItem.getRestaurantId().equals(" ") ||
        menuItem.getParentId() == null || menuItem.getParentId().equals("") || menuItem.getParentId().equals(" ")
        ){

            return null;
        }

        menuItem.setParentType(ParentType.CATEGORY);
        menuItemRepository.save(menuItem);
        return updateWholeMenu(menuItem.getRestaurantId());

    }

    public Menus addMenuItemInSubCategory(MenuItem menuItem){

        if(menuItem.getRestaurantId() == null || menuItem.getRestaurantId().equals("") || menuItem.getRestaurantId().equals(" ") ||
                menuItem.getParentId() == null || menuItem.getParentId().equals("") || menuItem.getParentId().equals(" ")
        ){

            return null;
        }
        menuItem.setParentType(ParentType.SUBCATEGORY);
        menuItemRepository.save(menuItem);
        return updateWholeMenu(menuItem.getRestaurantId());
    }

    public Menus addMenuItemInSubSubCategory(String restaurantId, MenuItem menuItem, String subSubCategoryParentId){

        menuItem.setParentId(subSubCategoryParentId);
        menuItem.setRestaurantId(restaurantId);
        menuItem.setParentType(ParentType.SUBSUBCATEGORY);
        menuItemRepository.save(menuItem);
        return updateWholeMenu(restaurantId);
    }
}
