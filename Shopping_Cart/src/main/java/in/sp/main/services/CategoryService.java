package in.sp.main.services;


import java.util.List;

import in.sp.main.model.Category;

public interface CategoryService  {

	
	public Category saveCategory(Category category);

	public Boolean existCategory(String name);
      
	public List <Category> getallCategory();
	
	public Boolean deleteCategory(int id);
	
	public Category getCategoryById(int id);
	
	public List <Category> getallActiveCategory();

}
