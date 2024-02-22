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
                            subSubCategoryRepository.save(subSubCategory);

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
                    subCategoryRepository.save(subCategory);
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
            categoryRepository.save(category);
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
                        subSubCategoryRepository.save(subSubCategory);
                    }
                }
                subCategory.setListOfSubSubCategories(updatedListOfSubSubCategories);
                subCategoryRepository.save(subCategory);

            }
            category.setListOfSubCategories(updatedListOfSubCategories);
            categoryRepository.save(category);
        }

        menus.setListOfCategories(updatedListOfCategories);
        menusRepository.save(menus);
        System.out.println("SAVED");
        return menus;

    }


    //not tested
    public Menus addSubSubCategory(SubSubCategory subSubCategory){

        if(subSubCategory.getRestaurantId().equals("") || subSubCategory.getRestaurantId().equals(" ") || subSubCategory.getRestaurantId()==null
        || subSubCategory.getSubCategoryId() == null || subSubCategory.getSubCategoryId().equals("") || subSubCategory.getSubSubCategoryId().equals(" ")
        ){
            System.out.println("PASS SUB-CATEGORY-ID AND RESTAURANT-ID IN REQUEST");
            return null;
        }

        subSubCategoryRepository.save(subSubCategory);

        if(subSubCategory.getListOfMenuItems() != null){
            for(MenuItem menuItem: subSubCategory.getListOfMenuItems()){

                menuItem.setRestaurantId(subSubCategory.getRestaurantId());
                menuItem.setParentType(ParentType.SUBSUBCATEGORY);
                menuItem.setParentId(subSubCategory.getSubSubCategoryId());
                menuItemRepository.save(menuItem);
            }
        }

        return updateWholeMenu(subSubCategory.getRestaurantId());
    }

    //DONE
    public Menus addSubCategory(SubCategory subCategory){

        if(subCategory.getCategoryId().equals(" ") || subCategory.getRestaurantId() == null || subCategory.getCategoryId() == null || subCategory.getCategoryId().equals("")
        || subCategory.getRestaurantId().equals("") || subCategory.getRestaurantId().equals(" ")
        ){
            System.out.println("PASS CATEGORY-ID AND RESTAURANT-ID IN REQUEST");
            return null;
        }

        subCategoryRepository.save(subCategory);

        if(subCategory.getListOfMenuItems()!=null){
            for(MenuItem menuItem: subCategory.getListOfMenuItems()){

                menuItem.setRestaurantId(subCategory.getRestaurantId());
                menuItem.setParentType(ParentType.SUBCATEGORY);
                menuItem.setParentId(subCategory.getSubCategoryId());
                menuItemRepository.save(menuItem);

            }
        }

        if(subCategory.getListOfSubSubCategories() != null){
            for(SubSubCategory subSubCategory: subCategory.getListOfSubSubCategories()){

                subSubCategory.setSubCategoryId(subCategory.getSubCategoryId());
                subSubCategory.setRestaurantId(subCategory.getRestaurantId());
                subSubCategoryRepository.save(subSubCategory);

                if(subSubCategory.getListOfMenuItems() != null){
                    for(MenuItem menuItem: subSubCategory.getListOfMenuItems()){

                        menuItem.setRestaurantId(subCategory.getRestaurantId());
                        menuItem.setParentType(ParentType.SUBSUBCATEGORY);
                        menuItem.setParentId(subSubCategory.getSubSubCategoryId());
                        menuItemRepository.save(menuItem);
                    }
                }
            }
        }

        return updateWholeMenu(subCategory.getRestaurantId());
    }


    //DONE
    public Menus addCategoryInMenu(Category category){

        if(category.getRestaurantId() == null || category.getRestaurantId().equals("") || category.getRestaurantId().equals(" ")){
            System.out.println("RESTAURANT-ID NOT IN CATEGORY-REQUEST BODY");
            return null;
        }

        categoryRepository.save(category);
        List<SubCategory> subCategoryList = category.getListOfSubCategories();
        List<MenuItem> menuItemList = category.getListOfMenuItems();

        if(menuItemList!=null){
            for(MenuItem menuItem: menuItemList){

                menuItem.setRestaurantId(category.getRestaurantId());
                menuItem.setParentType(ParentType.CATEGORY);
                menuItem.setParentId(category.getCategoryId());
                menuItemRepository.save(menuItem);
            }
        }

        if(subCategoryList!=null){
            for(SubCategory subCategory: subCategoryList){

                subCategory.setCategoryId(category.getCategoryId());
                subCategory.setRestaurantId(category.getRestaurantId());
                subCategoryRepository.save(subCategory);

                if(subCategory.getListOfMenuItems()!=null){
                    for(MenuItem menuItem: subCategory.getListOfMenuItems()){

                        menuItem.setRestaurantId(category.getRestaurantId());
                        menuItem.setParentType(ParentType.SUBCATEGORY);
                        menuItem.setParentId(subCategory.getSubCategoryId());
                        menuItemRepository.save(menuItem);

                    }
                }

                if(subCategory.getListOfSubSubCategories() != null){
                    for(SubSubCategory subSubCategory: subCategory.getListOfSubSubCategories()){

                        subSubCategory.setSubCategoryId(subCategory.getSubCategoryId());
                        subSubCategory.setRestaurantId(category.getRestaurantId());
                        subSubCategoryRepository.save(subSubCategory);

                        if(subSubCategory.getListOfMenuItems() != null){
                            for(MenuItem menuItem: subSubCategory.getListOfMenuItems()){

                                menuItem.setRestaurantId(category.getRestaurantId());
                                menuItem.setParentType(ParentType.SUBSUBCATEGORY);
                                menuItem.setParentId(subSubCategory.getSubSubCategoryId());
                                menuItemRepository.save(menuItem);
                            }
                        }

                    }
                }
            }
        }

        return updateWholeMenu(category.getRestaurantId());

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


    //DONE
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



    //DONE
    public Menus addMenuItemInSubSubCategory(MenuItem menuItem){

        if(menuItem.getRestaurantId() == null || menuItem.getRestaurantId().equals("") || menuItem.getRestaurantId().equals(" ") ||
                menuItem.getParentId() == null || menuItem.getParentId().equals("") || menuItem.getParentId().equals(" ")
        ){

            return null;
        }
        menuItem.setParentType(ParentType.SUBSUBCATEGORY);
        menuItemRepository.save(menuItem);
        return updateWholeMenu(menuItem.getRestaurantId());
    }


    //DONE
    public Menus deleteMenuItem(MenuItem menuItem){

        menuItemRepository.delete(menuItem);
        return updateWholeMenu(menuItem.getRestaurantId());
    }



    //DONE
    public Menus deleteSubSubCategory(SubSubCategory subSubCategory){

        String parentSubSubCategoryId = subSubCategory.getSubSubCategoryId();

        menuItemRepository.deleteByParentId(parentSubSubCategoryId);

        subSubCategoryRepository.delete(subSubCategory);

        return updateWholeMenu(subSubCategory.getRestaurantId());

    }


    //DONE
    public Menus deleteSubCategory(SubCategory subCategory){

        String subCategoryId = subCategory.getSubCategoryId();

        menuItemRepository.deleteByParentId(subCategoryId);

        List<SubSubCategory> subSubCategoryList = subCategory.getListOfSubSubCategories();

        if(subSubCategoryList!=null){
            for(SubSubCategory subSubCategory: subSubCategoryList){

                String subSubCategoryId = subSubCategory.getSubSubCategoryId();
                menuItemRepository.deleteByParentId(subSubCategoryId);

            }
        }


        subSubCategoryRepository.deleteBySubCategoryId(subCategoryId);

        subCategoryRepository.delete(subCategory);

        return updateWholeMenu(subCategory.getRestaurantId());
    }


    //DONE
    public Menus deleteCategory(Category category){

        category = categoryRepository.findById(category.getCategoryId()).orElse(null);

        if(category==null){
            System.out.println("CATEGORY ID DOES NOT EXIST IN DB");
            return null;
        }

        if(category.getListOfMenuItems()!=null && !category.getListOfMenuItems().isEmpty()){
            menuItemRepository.deleteByParentId(category.getCategoryId());
        }

        if(category.getListOfSubCategories()!=null && !category.getListOfSubCategories().isEmpty()){

            for(SubCategory subCategory: category.getListOfSubCategories()){

                if(subCategory.getListOfMenuItems()!=null){
                    menuItemRepository.deleteByParentId(subCategory.getSubCategoryId());
                }

                if(subCategory.getListOfSubSubCategories()!=null && !subCategory.getListOfSubSubCategories().isEmpty()){

                    for(SubSubCategory subSubCategory: subCategory.getListOfSubSubCategories()){

                        System.out.println("SSC NAME: "+subSubCategory.getSubSubCategoryName());
                        if(subSubCategory.getListOfMenuItems()!=null && !subSubCategory.getListOfMenuItems().isEmpty()){


                            menuItemRepository.deleteByParentId(subSubCategory.getSubSubCategoryId());
                        }
                        System.out.println("SSC ID: "+subSubCategory.getSubSubCategoryId());
                        System.out.println("SSC-sc NAME: "+subSubCategory.getSubCategoryId());
                        subSubCategoryRepository.delete(subSubCategory);
                    }
                }
                subCategoryRepository.delete(subCategory);
            }
        }

        categoryRepository.delete(category);
        return updateWholeMenu(category.getRestaurantId());
    }


    //DONE
    //parentType of menuItem should be sent from frontend itself
    public Menus editMenuItem(String menudItemId, MenuItem menuItem){

        menuItem.setItemId(menudItemId);
        menuItemRepository.save(menuItem);
        return updateWholeMenu(menuItem.getRestaurantId());
    }

    public void editCategory(Category category){

    }

    public void editSubCategory(SubSubCategory subSubCategory){

    }

    public void editSubSubCategory(SubSubCategory subSubCategory){

    }
}
