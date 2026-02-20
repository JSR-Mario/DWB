import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Controller controller = Controller.getInstance();

        int comando = 0;
        while (comando != -1){
            switch (comando){
            case 0:
                System.out.println("""
                        0  : Menu 
                        1  : getCategories 
                        2  : createCategory
                        3  : deleteCategory
                        -1 : exit
                        """
                        );
                break;
            case 1:
                List categories = controller.getCategories();
                String out = (categories.size()==0) ? "No existen categorias registradas" : categories.toString();
                System.out.println(out);
                break;
            case 2:
                System.out.println("Escribe la categoria");
                String categoria = sc.nextLine();
                System.out.println("Escribe el tag");
                String tag = sc.nextLine();

                try{
                    controller.createCategory(categoria,tag);
                    System.out.println("Lista modificada: " + controller.getCategories());
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }
                break;

            case 3:
                System.out.println("Escribe el id de una categoria");
                System.out.println(controller.getCategories());
                Integer id = Integer.parseInt(sc.nextLine());

                try{
                    controller.deleteCategory(id);
                    System.out.println("Lista modificada: " + controller.getCategories());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;

            default:
                comando = 0;
            }
            try {
                comando = Integer.parseInt(sc.nextLine());
                if (comando>3 || comando< -1){
                    System.out.println("Comando invalido\n");
                }
            } catch (Exception e){
                System.out.println("Comando invalido\n");
            }
        }
    }
}
