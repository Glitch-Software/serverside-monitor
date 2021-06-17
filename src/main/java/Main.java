import com.glitchsoftware.monitor.Monitor;
import com.glitchsoftware.monitor.product.Product;
import com.glitchsoftware.monitor.product.impl.NewEggProduct;
import com.glitchsoftware.monitor.socket.notification.impl.ProductNotification;
import com.glitchsoftware.monitor.task.Task;
import com.glitchsoftware.monitor.task.callback.MonitorCallback;
import com.glitchsoftware.monitor.task.impl.retail.NewEggTask;

/**
 * @author Brennan
 * @since 6/17/2021
 **/
public class Main {

    public static void main(String[] args) {
        Monitor.INSTANCE.startServer();

        while (true) {
            Monitor.INSTANCE.sendNotification(new ProductNotification(new NewEggProduct("12345",
                    "Test Product", "image", "google.com", 20.00, true)));
        }

//        final Task task = new NewEggTask(new MonitorCallback() {
//            @Override
//            public void onProductInStock(Product product) {
//                final NewEggProduct newEggProduct = (NewEggProduct) product;
//
//                Monitor.INSTANCE.sendNotification(new ProductNotification(newEggProduct));
//            }
//
//            @Override
//            public void onFailure(String reason) {
//                System.out.println(reason);
//            }
//        }, "N82E16886816048");
//        task.setStarted(true);
//
//        Monitor.INSTANCE.newTask(task);
    }

}
