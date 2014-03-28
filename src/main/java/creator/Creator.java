package creator;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Processor;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.LinkOption;
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

    public static void main(String[] args) throws IOException {
       if(!Files.exists(sysFilePath, LinkOption.NOFOLLOW_LINKS)) {
           Files.createFile(sysFilePath);
           writeToFile(createSystemInfoString());
       }
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
}
