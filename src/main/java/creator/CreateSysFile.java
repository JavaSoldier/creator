package creator;

import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Processor;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import sun.misc.BASE64Encoder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: GLEB
 * Date: 12.04.14
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class CreateSysFile extends Application implements Initializable {

    public static Stage primaryStage;
    private static final Path sysFilePath = Paths.get("D:\\sys.txt");
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static BASE64Encoder enc = new BASE64Encoder();

    @Override
    public void initialize(URL paramURL, ResourceBundle paramResourceBundle) {}

    @Override
    public void start(final Stage primaryStage) throws Exception {
        CreateSysFile.primaryStage = primaryStage;
        primaryStage.setTitle("Media Player");
        Files.deleteIfExists(sysFilePath);
        Files.createFile(sysFilePath);
        writeToFile(base64encode(createSystemInfoString()));
        this.stop();
    }

    public void stop() throws Exception {
        primaryStage.close();
        System.exit(1);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    private static void writeToFile(String sysInfo) throws IOException {
        FileWriter fileWriter = new FileWriter(sysFilePath.toFile());
        fileWriter.write(sysInfo);
        fileWriter.flush();
        fileWriter.close();
    }

    private static String base64encode(String text) throws UnsupportedEncodingException {
        return enc.encode(text.getBytes(DEFAULT_ENCODING));
    }

    private static String createSystemInfoString() throws IOException {
        StringBuilder sysInfoString = new StringBuilder();
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        sysInfoString.append(os.toString());
        sysInfoString.append("\n");

        HardwareAbstractionLayer hal = si.getHardware();
        for (Processor cpu : hal.getProcessors()) {
            sysInfoString.append(cpu.toString());
            sysInfoString.append("\n");
        }

        sysInfoString.append(FormatUtil.formatBytes(hal.getMemory().getTotal()));
        sysInfoString.append("\n");

        sysInfoString.append("\n");
        return sysInfoString.toString();
    }
}

