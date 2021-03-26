package ru.slava62;

/*import ru.slava62.db.dao.CategoriesMapper;
import ru.slava62.db.dao.ProductsMapper;
import ru.slava62.db.model.CategoriesExample;
import ru.slava62.db.model.Products;
import ru.slava62.db.model.ProductsExample;*/

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
/*
        CategoriesMapper categoriesMapper = DbUtils.getCategoriesMapper();
        ProductsMapper productsMapper = DbUtils.getProductsMapper();
        CategoriesExample c= new CategoriesExample();
        ProductsExample p=new ProductsExample();
        p.createCriteria().andCategory_idEqualTo(1L);
        List<Products> prod=productsMapper.selectByExample(p);
        for (Products ps: prod
             ) {
            System.out.println("{\n id: " + ps.getId()
                    +"\ntitle: " + ps.getTitle()
                    +"\nprice: " + ps.getPrice()
                    +"\ncategiry_name: "+
                    categoriesMapper.selectByPrimaryKey(Math.toIntExact(ps.getCategory_id())).getTitle() +"\n}");
        }*/

    }
}
