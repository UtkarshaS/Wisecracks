import java.sql.Date;

class Category{

    int categoryId;
    String categoryName;

    //Default contructor
    public Category(String categoryName){
        int result;
        this.categoryName = categoryName;
        //result = retrieve CategoryId from DB for the given CategoryName
        this.categoryId = result;
    }

    //Secondary Constructor
    public Category(int categoryId, String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}

