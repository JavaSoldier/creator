package creator;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Processor;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import sun.misc.BASE64Encoder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: GLEB
 * Date: 28.03.14
 * Time: 5:59
 * To change this template use File | Settings | File Templates.
 */
public class Creator {

    private static final Path sysFilePath = Paths.get("D:\\sys.txt");
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static BASE64Encoder enc = new BASE64Encoder();

    public static void main(String[] args) throws IOException {
        Files.deleteIfExists(sysFilePath);
        Files.createFile(sysFilePath);
        writeToFile(base64encode(createSystemInfoString()));

    }

    private static void writeToFile(String sysInfo) throws IOException {
        FileWriter fileWriter = new FileWriter(sysFilePath.toFile());
        fileWriter.write(sysInfo);
        fileWriter.flush();
        fileWriter.close();
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

        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();

        for (int i = 0; i < mac.length; i++) {
            sysInfoString.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        sysInfoString.append("\n");
        return sysInfoString.toString();
    }

    private static String base64encode(String text) {
        try {
            String rez = enc.encode(text.getBytes(DEFAULT_ENCODING));
            return rez;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
