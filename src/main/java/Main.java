import com.glitchsoftware.monitor.Monitor;
import com.glitchsoftware.monitor.product.Product;
import com.glitchsoftware.monitor.product.impl.FootsiteProduct;
import com.glitchsoftware.monitor.product.impl.NewEggProduct;
import com.glitchsoftware.monitor.socket.notification.impl.ProductNotification;
import com.glitchsoftware.monitor.task.Task;
import com.glitchsoftware.monitor.task.callback.MonitorCallback;
import com.glitchsoftware.monitor.task.impl.footsites.Footsites;
import com.glitchsoftware.monitor.task.impl.footsites.FootsitesTask;
import com.glitchsoftware.monitor.task.impl.retail.NewEggTask;

/**
 * @author Brennan
 * @since 6/17/2021
 **/
public class Main {

    public static void main(String[] args) {
//        Monitor.INSTANCE.startServer();
//
//        while (true) {
//            Monitor.INSTANCE.sendNotification(new ProductNotification(new NewEggProduct("12345",
//                    "Test Product", "image", "google.com", 20.00, true)));
//        }

        //yeezy slides - GZ0953
        //socks - 6912010

        final Task task = new FootsitesTask(new MonitorCallback() {
            @Override
            public void onProductInStock(Product product) {
                final FootsiteProduct footsiteProduct = (FootsiteProduct) product;

            }

            @Override
            public void onFailure(String reason) {
                System.out.println(reason);
            }
        }, Footsites.FOOTLOCKER_US,"53558034");
        task.setStarted(true);

        Monitor.INSTANCE.newTask(task);
    }

}
