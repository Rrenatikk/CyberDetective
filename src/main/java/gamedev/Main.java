package gamedev;

import gamedev.controller.GameController;
import gamedev.model.Game;
import gamedev.model.Level; // ✅ Импорт
import gamedev.model.GameObject; // ✅ Импорт
import gamedev.model.ThreatType; // ✅ Импорт
import gamedev.model.view.MainMenu;
import javafx.application.Application;
import javafx.scene.effect.DropShadow; // ✅ Импорт
import javafx.scene.image.Image; // ✅ Импорт
import javafx.scene.image.ImageView; // ✅ Импорт
import javafx.scene.paint.Color; // ✅ Импорт
import javafx.stage.Stage;

import java.util.ArrayList; // ✅ Импорт
import java.util.List; // ✅ Импорт

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ✅ 1. Сначала создаем все уровни
        List<Level> allLevels = createAllLevels();

        // ✅ 2. Создаем модель Game с этими уровнями
        Game game = new Game(allLevels);

        // ✅ 3. Создаем контроллер, ПЕРЕДАВАЯ ему готовую модель
        GameController controller = new GameController(game);

        // 4. Запускаем главное меню
        MainMenu mainMenu = new MainMenu(primaryStage, controller);
        mainMenu.show();
    }


    private List<Level> createAllLevels() {
        List<Level> levels = new ArrayList<>();

        // glow effect (нужен для объектов)
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.YELLOW);
        borderGlow.setOffsetX(0);
        borderGlow.setOffsetY(0);
        borderGlow.setWidth(50);
        borderGlow.setHeight(50);

        //########################################/ LEVEL 1 /########################################//
        Image background1Image = new Image(getClass().getResource("/objects/level1/background.png").toExternalForm());
        ImageView background1 = new ImageView(background1Image);

        Image usbImage = new Image(getClass().getResource("/objects/level1/usb.png").toExternalForm());
        ImageView usb = new ImageView(usbImage);
        Image calculatorImage = new Image(getClass().getResource("/objects/level1/calculator.png").toExternalForm());
        ImageView calculator = new ImageView(calculatorImage);
        Image cdImage = new Image(getClass().getResource("/objects/level1/cd.png").toExternalForm());
        ImageView cd = new ImageView(cdImage);
        Image cupImage = new Image(getClass().getResource("/objects/level1/cup.png").toExternalForm());
        ImageView cup = new ImageView(cupImage);
        Image phishing_mailImage = new Image(getClass().getResource("/objects/level1/phishing_mail.png").toExternalForm());
        ImageView phishing_mail = new ImageView(phishing_mailImage);
        Image login_stickerImage = new Image(getClass().getResource("/objects/level1/login_sticker.png").toExternalForm());
        ImageView login_sticker = new ImageView(login_stickerImage);
        Image mouseImage = new Image(getClass().getResource("/objects/level1/mouse.png").toExternalForm());
        ImageView mouse = new ImageView(mouseImage);
        Image penImage = new Image(getClass().getResource("/objects/level1/pen.png").toExternalForm());
        ImageView pen = new ImageView(penImage);
        Image pencilsImage = new Image(getClass().getResource("/objects/level1/pencils.png").toExternalForm());
        ImageView pencils = new ImageView(pencilsImage);
        Image phishing_notificationImage = new Image(getClass().getResource("/objects/level1/phishing_notification.png").toExternalForm());
        ImageView phishing_notification = new ImageView(phishing_notificationImage);
        Image phoneImage = new Image(getClass().getResource("/objects/level1/phone.png").toExternalForm());
        ImageView phone = new ImageView(phoneImage);
        Image plantImage = new Image(getClass().getResource("/objects/level1/plant.png").toExternalForm());
        ImageView plant = new ImageView(plantImage);
        Image water_bottleImage = new Image(getClass().getResource("/objects/level1/water_bottle.png").toExternalForm());
        ImageView water_bottle = new ImageView(water_bottleImage);

        GameObject usbObj = new GameObject("USB-флешка", true, ThreatType.USB_DRIVE, usb, 1113, 827);
        GameObject calculatorObj = new GameObject("Калькулятор", false, ThreatType.NO_THREAT, calculator, 420, 842);
        GameObject cdObj = new GameObject("CD-диск", true, ThreatType.SUSPICIOUS_CD, cd, 1273, 428);
        GameObject pencilsObj = new GameObject("Олівці", false, ThreatType.NO_THREAT, pencils, 691, 542);
        GameObject plantObj = new GameObject("Рослина", false, ThreatType.NO_THREAT, plant, 627, 635);
        GameObject cupObj = new GameObject("Чашка кави", false, ThreatType.NO_THREAT, cup, 674, 708);
        GameObject phishing_mailObj = new GameObject("Фішинговий лист", true, ThreatType.PHISHING_EMAIL, phishing_mail, 748, 241);
        GameObject login_stickerObj = new GameObject("Дані для входу", true, ThreatType.STICKY_NOTE_PASSWORD, login_sticker, 1092, 449);
        GameObject mouseObj = new GameObject("Миша", false, ThreatType.NO_THREAT, mouse, 1187, 715);
        GameObject penObj = new GameObject("Ручка", false, ThreatType.NO_THREAT, pen, 844, 636);
        GameObject phishing_notificationObj = new GameObject("Фішингове повідомлення", true, ThreatType.PHISHING_NOTIFICATION, phishing_notification, 1063, 334);
        GameObject phoneObj = new GameObject("Смартфон", true, ThreatType.PHONE, phone, 1327, 722);
        GameObject water_bottleObj = new GameObject("Пляшка води", false, ThreatType.NO_THREAT, water_bottle, 497, 436);

        usb.setOnMouseEntered(e -> usb.setEffect(borderGlow));
        usb.setOnMouseExited(e -> usb.setEffect(null));
        calculator.setOnMouseEntered(e -> calculator.setEffect(borderGlow));
        calculator.setOnMouseExited(e -> calculator.setEffect(null));
        cd.setOnMouseEntered(e -> cd.setEffect(borderGlow));
        cd.setOnMouseExited(e -> cd.setEffect(null));
        pencils.setOnMouseEntered(e -> pencils.setEffect(borderGlow));
        pencils.setOnMouseExited(e -> pencils.setEffect(null));
        cup.setOnMouseEntered(e -> cup.setEffect(borderGlow));
        cup.setOnMouseExited(e -> cup.setEffect(null));
        phishing_mail.setOnMouseEntered(e -> phishing_mail.setEffect(borderGlow));
        phishing_mail.setOnMouseExited(e -> phishing_mail.setEffect(null));
        login_sticker.setOnMouseEntered(e -> login_sticker.setEffect(borderGlow));
        login_sticker.setOnMouseExited(e -> login_sticker.setEffect(null));
        mouse.setOnMouseEntered(e -> mouse.setEffect(borderGlow));
        mouse.setOnMouseExited(e -> mouse.setEffect(null));
        pen.setOnMouseEntered(e -> pen.setEffect(borderGlow));
        pen.setOnMouseExited(e -> pen.setEffect(null));
        phishing_notification.setOnMouseEntered(e -> phishing_notification.setEffect(borderGlow));
        phishing_notification.setOnMouseExited(e -> phishing_notification.setEffect(null));
        phone.setOnMouseEntered(e -> phone.setEffect(borderGlow));
        phone.setOnMouseExited(e -> phone.setEffect(null));
        plant.setOnMouseEntered(e -> plant.setEffect(borderGlow));
        plant.setOnMouseExited(e -> plant.setEffect(null));
        water_bottle.setOnMouseEntered(e -> water_bottle.setEffect(borderGlow));
        water_bottle.setOnMouseExited(e -> water_bottle.setEffect(null));

        Level level1 = new Level(1, List.of(usbObj, calculatorObj, cdObj, plantObj, pencilsObj,
                cupObj, phishing_mailObj, login_stickerObj, mouseObj, penObj, phishing_notificationObj,
                phoneObj, water_bottleObj), background1);

        levels.add(level1);

        //########################################/ LEVEL 2 /########################################//
        Image background2Image = new Image(getClass().getResource("/objects/level2/background2.png").toExternalForm());
        ImageView background2 = new ImageView(background2Image);

        Image api_keyImage = new Image(getClass().getResource("/objects/level2/api_key.png").toExternalForm());
        ImageView api_key = new ImageView(api_keyImage);
        GameObject api_keyObj = new GameObject("API ключ", true, ThreatType.API_KEY, api_key, 428, 797);

        Image flashImage = new Image(getClass().getResource("/objects/level2/usb_flash.png").toExternalForm());
        ImageView flash = new ImageView(flashImage);
        GameObject flashObj = new GameObject("USB з вірусом",true, ThreatType.USB_DRIVE, flash,973,749);

        Image db_dataImage = new Image(getClass().getResource("/objects/level2/db_data.png").toExternalForm());
        ImageView db_data = new ImageView(db_dataImage);
        GameObject db_dataObj = new GameObject("Дані бази данних", true, ThreatType.DB_DATA, db_data, 1056, 739);

        Image routerImage = new Image(getClass().getResource("/objects/level2/router.png").toExternalForm());
        ImageView router = new ImageView(routerImage);
        GameObject routerObj = new GameObject("Роутер", true, ThreatType.ROUTER, router, 1067, 455);

        Image coffee_cupImage = new Image(getClass().getResource("/objects/level2/coffee_cup.png").toExternalForm());
        ImageView coffee_cup = new ImageView(coffee_cupImage);
        GameObject coffee_cupObj = new GameObject("Чашка кави", false, ThreatType.NO_THREAT, coffee_cup, 1123, 811);

        Image web_cameraImage = new Image(getClass().getResource("/objects/level2/web_camera.png").toExternalForm());
        ImageView web_camera = new ImageView(web_cameraImage);
        GameObject web_cameraObj = new GameObject("Веб-камера", true, ThreatType.WEB_CAMERA, web_camera, 628, 636);

        Image phone2Image = new Image(getClass().getResource("/objects/level2/phone2.png").toExternalForm());
        ImageView phone2 = new ImageView(phone2Image);
        GameObject phone2Obj = new GameObject("Смартфон", true, ThreatType.PHONE, phone2, 557, 881);

        Image booksImage = new Image(getClass().getResource("/objects/level2/books.png").toExternalForm());
        ImageView books = new ImageView(booksImage);
        GameObject booksObj = new GameObject("Книжки", false, ThreatType.NO_THREAT, books, 1009, 574);

        Image laptopImage = new Image(getClass().getResource("/objects/level2/laptop.png").toExternalForm());
        ImageView laptop = new ImageView(laptopImage);
        GameObject laptopObj = new GameObject("Ноутбук", true, ThreatType.LAPTOP, laptop, 1179, 511);

        Image plant2Image = new Image(getClass().getResource("/objects/level2/plant.png").toExternalForm());
        ImageView plant2 = new ImageView(plant2Image);
        GameObject plant2Obj = new GameObject("Рослина", false, ThreatType.NO_THREAT, plant2, 863, 436);

        Image fake_prizeImage = new Image(getClass().getResource("/objects/level2/fake_prize.png").toExternalForm());
        ImageView fake_prize = new ImageView(fake_prizeImage);
        GameObject fake_prizeObj = new GameObject("Фейковий подарунок", true, ThreatType.FAKE_PRIZE, fake_prize, 585, 670);

        Image microwaveImage = new Image(getClass().getResource("/objects/level2/microwave.png").toExternalForm());
        ImageView microwave = new ImageView(microwaveImage);
        GameObject microwaveObj = new GameObject("Мікрохвильовка", false, ThreatType.NO_THREAT, microwave, 593, 344);

        Image virus_detectedImage = new Image(getClass().getResource("/objects/level2/virus_detected.png").toExternalForm());
        ImageView virus_detected = new ImageView(virus_detectedImage);
        GameObject virus_detectedObj = new GameObject("Вірус", true, ThreatType.COMPUTER_VIRUS, virus_detected, 1269, 698);

        Image pensImage = new Image(getClass().getResource("/objects/level2/pens.png").toExternalForm());
        ImageView pens = new ImageView(pensImage);
        GameObject pensObj = new GameObject("Олівці", false, ThreatType.NO_THREAT, pens, 1328, 564);

        Image boxesImage = new Image(getClass().getResource("/objects/level2/boxes.png").toExternalForm());
        ImageView boxes = new ImageView(boxesImage);
        GameObject boxesObj = new GameObject("Коробки", false, ThreatType.NO_THREAT, boxes, 1168, 218);

        Image chairImage = new Image(getClass().getResource("/objects/level2/chair.png").toExternalForm());
        ImageView chair = new ImageView(chairImage);
        GameObject chairObj = new GameObject("Крісло", false, ThreatType.NO_THREAT, chair, 1186, 371);

        Image documents2Image = new Image(getClass().getResource("/objects/level2/documents2.png").toExternalForm());
        ImageView documents2 = new ImageView(documents2Image);
        GameObject documents2Obj = new GameObject("Документи2", false, ThreatType.NO_THREAT, documents2, 484, 332);

        Image documents1Image = new Image(getClass().getResource("/objects/level2/documents1.png").toExternalForm());
        ImageView documents1 = new ImageView(documents1Image);
        GameObject documents1Obj = new GameObject("Документи1", false, ThreatType.NO_THREAT, documents1, 822, 639);

        api_key.setOnMouseEntered(e -> api_key.setEffect(borderGlow));
        api_key.setOnMouseExited(e -> api_key.setEffect(null));

        flash.setOnMouseEntered(e -> flash.setEffect(borderGlow));
        flash.setOnMouseExited(e -> flash.setEffect(null));

        db_data.setOnMouseEntered(e -> db_data.setEffect(borderGlow));
        db_data.setOnMouseExited(e -> db_data.setEffect(null));

        router.setOnMouseEntered(e -> router.setEffect(borderGlow));
        router.setOnMouseExited(e -> router.setEffect(null));

        coffee_cup.setOnMouseEntered(e -> coffee_cup.setEffect(borderGlow));
        coffee_cup.setOnMouseExited(e -> coffee_cup.setEffect(null));

        web_camera.setOnMouseEntered(e -> web_camera.setEffect(borderGlow));
        web_camera.setOnMouseExited(e -> web_camera.setEffect(null));

        phone2.setOnMouseEntered(e -> phone2.setEffect(borderGlow));
        phone2.setOnMouseExited(e -> phone2.setEffect(null));

        books.setOnMouseEntered(e -> books.setEffect(borderGlow));
        books.setOnMouseExited(e -> books.setEffect(null));

        plant2.setOnMouseEntered(e -> plant2.setEffect(borderGlow));
        plant2.setOnMouseExited(e -> plant2.setEffect(null));

        laptop.setOnMouseEntered(e -> laptop.setEffect(borderGlow));
        laptop.setOnMouseExited(e -> laptop.setEffect(null));

        fake_prize.setOnMouseEntered(e -> fake_prize.setEffect(borderGlow));
        fake_prize.setOnMouseExited(e -> fake_prize.setEffect(null));

        microwave.setOnMouseEntered(e -> microwave.setEffect(borderGlow));
        microwave.setOnMouseExited(e -> microwave.setEffect(null));

        virus_detected.setOnMouseEntered(e -> virus_detected.setEffect(borderGlow));
        virus_detected.setOnMouseExited(e -> virus_detected.setEffect(null));

        chair.setOnMouseEntered(e -> chair.setEffect(borderGlow));
        chair.setOnMouseExited(e -> chair.setEffect(null));

        pens.setOnMouseEntered(e -> pens.setEffect(borderGlow));
        pens.setOnMouseExited(e -> pens.setEffect(null));

        boxes.setOnMouseEntered(e -> boxes.setEffect(borderGlow));
        boxes.setOnMouseExited(e -> boxes.setEffect(null));

        documents2.setOnMouseEntered(e -> documents2.setEffect(borderGlow));
        documents2.setOnMouseExited(e -> documents2.setEffect(null));

        documents1.setOnMouseEntered(e -> documents1.setEffect(borderGlow));
        documents1.setOnMouseExited(e -> documents1.setEffect(null));

        Level level2 = new Level(2, List.of(api_keyObj, flashObj, db_dataObj, routerObj, coffee_cupObj, web_cameraObj, phone2Obj,
                booksObj, plant2Obj, fake_prizeObj, microwaveObj, virus_detectedObj, chairObj, laptopObj, pensObj, boxesObj, documents1Obj,
                documents2Obj), background2);

        levels.add(level2);


        //########################################/ LEVEL 3 /########################################//
        Image background3Image = new Image(getClass().getResource("/objects/level3/background3.png").toExternalForm());
        ImageView background3 = new ImageView(background3Image);

        Image backpackImage = new Image(getClass().getResource("/objects/level3/backpack.png").toExternalForm());
        ImageView backpack = new ImageView(backpackImage);
        GameObject backpackObj = new GameObject("Рюкзак", false, ThreatType.NO_THREAT, backpack, 1201, 674);

        Image bookImage  = new Image(getClass().getResource("/objects/level3/book.png").toExternalForm());
        ImageView book = new ImageView(bookImage);
        GameObject bookObj = new GameObject("Книга", false, ThreatType.NO_THREAT, book, 1062, 631);

        Image bottleImage  = new Image(getClass().getResource("/objects/level3/bottle.png").toExternalForm());
        ImageView bottle = new ImageView(bottleImage);
        GameObject bottleObj = new GameObject("Пляшка", false, ThreatType.NO_THREAT, bottle, 731, 558);

        Image cameraImage  = new Image(getClass().getResource("/objects/level3/camera.png").toExternalForm());
        ImageView camera = new ImageView(cameraImage);
        GameObject cameraObj = new GameObject("Камера", true, ThreatType.SECURITY_CAMERA, camera, 569, 115);

        Image charging_stationImage =  new Image(getClass().getResource("/objects/level3/charging_station.png").toExternalForm());
        ImageView charging_station = new ImageView(charging_stationImage);
        GameObject charging_stationObj = new GameObject("Станція заряджання", true, ThreatType.CHARGING_STATION, charging_station, 630, 215);

        Image dishImage = new Image(getClass().getResource("/objects/level3/dish.png").toExternalForm());
        ImageView dish = new ImageView(dishImage);
        GameObject dishObj = new GameObject("Солодощі", false, ThreatType.NO_THREAT, dish, 782, 602);

        Image lettersImage  = new Image(getClass().getResource("/objects/level3/letters.png").toExternalForm());
        ImageView letters = new ImageView(lettersImage);
        GameObject lettersObj = new GameObject("Листи", false, ThreatType.NO_THREAT, letters, 883, 683);

        Image menuImage = new Image(getClass().getResource("/objects/level3/menu.png").toExternalForm());
        ImageView menu = new ImageView(menuImage);
        GameObject menuObj = new GameObject("Меню", false, ThreatType.NO_THREAT, menu, 419, 13);

        Image personal_dataImage  = new Image(getClass().getResource("/objects/level3/personal_data.png").toExternalForm());
        ImageView personal_data = new ImageView(personal_dataImage);
        GameObject personal_dataObj = new GameObject("Особисті дані", true, ThreatType.PERSONAL_DATA, personal_data, 949, 645);

        Image phone3Image  = new Image(getClass().getResource("/objects/level3/phone3.png").toExternalForm());
        ImageView phone3 = new ImageView(phone3Image);
        GameObject phone3Obj = new GameObject("Смартфон", true, ThreatType.PHONE, phone3, 933, 1002);

        Image photocameraImage =  new Image(getClass().getResource("/objects/level3/photocamera.png").toExternalForm());
        ImageView photocamera = new ImageView(photocameraImage);
        GameObject photocameraObj = new GameObject("Фотокамера", false, ThreatType.NO_THREAT, photocamera, 697, 801);

        Image plant3Image  = new Image(getClass().getResource("/objects/level3/plant3.png").toExternalForm());
        ImageView plant3 = new ImageView(plant3Image);
        GameObject plant3Obj = new GameObject("Рослина", false, ThreatType.NO_THREAT, plant3, 1134, 775);

        Image qr_codeImage = new Image(getClass().getResource("/objects/level3/qr_code.png").toExternalForm());
        ImageView qr_code = new ImageView(qr_codeImage);
        GameObject qr_codeObj = new GameObject("QR код", true, ThreatType.QR_CODE, qr_code, 1224, 143);

        Image security_alertImage = new Image(getClass().getResource("/objects/level3/security_alert.png").toExternalForm());
        ImageView security_alert = new ImageView(security_alertImage);
        GameObject security_alertObj = new GameObject("Попередження про несправжню загрозу", true, ThreatType.PHISHING_NOTIFICATION, security_alert, 986, 801);

        Image terminalImage = new Image(getClass().getResource("/objects/level3/terminal.png").toExternalForm());
        ImageView terminal = new ImageView(terminalImage);
        GameObject terminalObj = new GameObject("Платіжний термінал", true, ThreatType.POS_TERMINAL, terminal, 420, 887);

        Image usb_flash3Image =  new Image(getClass().getResource("/objects/level3/usb_flash3.png").toExternalForm());
        ImageView usb_flash3 = new ImageView(usb_flash3Image);
        GameObject usb_flash3Obj = new GameObject("Флешка", true, ThreatType.USB_DRIVE, usb_flash3, 960, 1016);

        backpack.setOnMouseEntered(e -> backpack.setEffect(borderGlow));
        backpack.setOnMouseExited(e -> backpack.setEffect(null));

        book.setOnMouseEntered(e -> book.setEffect(borderGlow));
        book.setOnMouseExited(e -> book.setEffect(null));

        bottle.setOnMouseEntered(e -> bottle.setEffect(borderGlow));
        bottle.setOnMouseExited(e -> bottle.setEffect(null));

        camera.setOnMouseEntered(e -> camera.setEffect(borderGlow));
        camera.setOnMouseExited(e -> camera.setEffect(null));

        charging_station.setOnMouseEntered(e -> charging_station.setEffect(borderGlow));
        charging_station.setOnMouseExited(e -> charging_station.setEffect(null));

        dish.setOnMouseEntered(e -> dish.setEffect(borderGlow));
        dish.setOnMouseExited(e -> dish.setEffect(null));

        letters.setOnMouseEntered(e -> letters.setEffect(borderGlow));
        letters.setOnMouseExited(e -> letters.setEffect(null));

        menu.setOnMouseEntered(e -> menu.setEffect(borderGlow));
        menu.setOnMouseExited(e -> menu.setEffect(null));

        personal_data.setOnMouseEntered(e -> personal_data.setEffect(borderGlow));
        personal_data.setOnMouseExited(e -> personal_data.setEffect(null));

        phone3.setOnMouseEntered(e -> phone3.setEffect(borderGlow));
        phone3.setOnMouseExited(e -> phone3.setEffect(null));

        photocamera.setOnMouseEntered(e -> photocamera.setEffect(borderGlow));
        photocamera.setOnMouseExited(e -> photocamera.setEffect(null));

        plant3.setOnMouseEntered(e -> plant3.setEffect(borderGlow));
        plant3.setOnMouseExited(e -> plant3.setEffect(null));

        qr_code.setOnMouseEntered(e -> qr_code.setEffect(borderGlow));
        qr_code.setOnMouseExited(e -> qr_code.setEffect(null));

        security_alert.setOnMouseEntered(e -> security_alert.setEffect(borderGlow));
        security_alert.setOnMouseExited(e -> security_alert.setEffect(null));

        terminal.setOnMouseEntered(e -> terminal.setEffect(borderGlow));
        terminal.setOnMouseExited(e -> terminal.setEffect(null));

        usb_flash3.setOnMouseEntered(e -> usb_flash3.setEffect(borderGlow));
        usb_flash3.setOnMouseExited(e -> usb_flash3.setEffect(null));

        Level level3 = new Level(3, List.of(backpackObj, bookObj, bottleObj, cameraObj, charging_stationObj, dishObj, lettersObj, menuObj, personal_dataObj,
                phone3Obj, photocameraObj, plant3Obj, qr_codeObj, security_alertObj, terminalObj, usb_flash3Obj), background3);

        levels.add(level3);

        return levels;
    }


    public static void main(String[] args) {
        launch(args);
    }
}