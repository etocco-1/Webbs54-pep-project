// import Controller.SocialMediaController;
// import io.javalin.Javalin;

// public class Main {
//     public static void main(String[] args) {
//         Javalin app = Javalin.create();
//         app.start(8080);
//         app.stop();
//         app.start();
//     }
// }

import Controller.SocialMediaController;
import io.javalin.Javalin;

public class Main {
    private static Javalin app;

    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        if (app == null) {
            app = controller.startAPI();
        }
    }
}

