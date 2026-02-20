import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

/*
 * Seria mejor utilizar maps o al menos map + list pero lo deje asi mas simple
 * */
public class Controller {
    private final List<Category> categoryList = new LinkedList<>();

    private Controller(){}

    private static class Helper {
        private static final Controller INSTANCE = new Controller();
    }

    public static Controller getInstance(){
        return Helper.INSTANCE;
    }

    public List<Category> getCategories(){
        return categoryList.stream().filter(c -> c.getStatus()==1).toList();
    }

    public Category createCategory(String category, String tag) throws Exception{
        String cat = category.trim();
        String t = tag.trim();
        // Aqui podriamos cambiar categoryList por getCategories para filtar inactivas
        for (Category c : categoryList){
            if (c.getCategory().equals(cat) || c.getTag().equals(t)){
                throw new Exception("Ya existe esta categoria");
            }
        }
        
        Category nuevaCategoria = new Category(categoryList.size(), cat, t, 1);
        categoryList.add(nuevaCategoria);

        return nuevaCategoria;
    }

    public void deleteCategory(Integer id) throws Exception{
        for (Category c : categoryList){
            if (c.getCategoryId().equals(id)){
                c.toggleStatus();
                return;
            }
        }
        throw new Exception("Categoria no encontrada");
    }
}
