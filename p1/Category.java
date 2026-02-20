public class Category{
    private Integer category_id;
    private String category;
    private String tag;
    private Integer status;

    public Category(Integer category_id, String category, String tag, Integer status){
        this.category_id = category_id;
        this.category = category;
        this.tag = tag;
        this.status = status;
    }

    public Integer getCategoryId(){
        return category_id;
    }

    public Integer getStatus(){
        return status;
    }

    public String getCategory(){
        return category;
    }

    public String getTag(){
        return tag;
    }

    public void toggleStatus(){
        this.status = (status==1) ? 0 : 1;
    }

    @Override
    public String toString(){
        return String.format("{%d,'%s','%s',%d}",category_id,category,tag,status);
    }
}
