package servlets;

import hibernate.HibernateUtils;
import hibernate.ProductEntity;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
    The database 'mystore' is created if it does not exist. Please check resources/hibernate.cfg.xml
 */

@WebServlet(name = "Products", urlPatterns = "/")
public class Products extends HttpServlet {

    Session session;

    @Override
    public void init() {
        session = HibernateUtils.buildSessionFactory().openSession();
        session.beginTransaction();

        ProductEntity productEntity = new ProductEntity();

        productEntity.setProductName("Laptop");
        productEntity.setPrice(599.99);
        productEntity.setInStock(false);
        session.save(productEntity);

        productEntity = new ProductEntity();
        productEntity.setProductName("Squishmallow");
        productEntity.setPrice(49.50);
        productEntity.setInStock(true);
        session.save(productEntity);


        productEntity = new ProductEntity();
        productEntity.setProductName("iPhone");
        productEntity.setPrice(999.99);
        productEntity.setInStock(true);
        session.save(productEntity);

        session.getTransaction().commit();
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<form action='' method='POST'>");
        out.println("<label>Enter Product ID: <input type='text' name='product-id'></input></label>");
        out.println("<input type='submit'></input>");
        out.println("</form>");
    }

    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("text/html");
        String productId = httpServletRequest.getParameter("product-id");
        PrintWriter out = httpServletResponse.getWriter();

        try {

            if (!productId.matches("[0-9]+")) {
                throw new NumberFormatException();
            }

            ProductEntity productEntity = session.get(ProductEntity.class, Long.parseLong(productId));

            if (productEntity != null) {

                String checkInStockStr;

                if (productEntity.isInStock()) {
                    checkInStockStr = " and it is in stock!";
                } else {
                    checkInStockStr = " but it is not in stock...";
                }
                out.println("Found product in database: " +
                        productEntity.getProductName() + " with price " + productEntity.getPrice() + checkInStockStr);
            } else {
                out.println("No product found for id: " + productId);
            }
        } catch (NumberFormatException e) {
            out.println("Error: only integers are allowed.");
        }



    }


}
